package closetics.Users;

import java.util.List;
import closetics.Users.Auth.AuthService;
import closetics.Users.Tokens.Token;
import closetics.Users.Tokens.TokenRepository;
import closetics.Users.Tokens.TokenService;
import closetics.Users.UserProfile.UserProfile;
import closetics.Users.UserProfile.UserProfileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthService authService;
    

    //Maybe move this in the future so that User doesn't have to deal with it and only makes a call towards UserProfile
    @Autowired
    private UserProfileRepository userProfileRepository;

    //This error is meant to trigger whenever a duplicate entry is added into the MySql database
    @ResponseStatus(value = HttpStatus.CONFLICT,
            reason = "Data integrity violation")  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String conflict() {
        return "ACCESS VIOLATION";
    }

    @GetMapping(path = "/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    public User getUser(@PathVariable long id) {
        return userRepository.findById(id).get();
    }

    @GetMapping(path = "/users/username/{id}")
    public User getUserByUsername(@PathVariable String id) {
        return userRepository.findByUsername(id);
    }


    /*
     * Provide a JSON request containing user info collected during signup you have to at a minimum provide a
     * username, email and password or the request will be invalid
     * Example:
     * {
     *"emailId":"test@hotmail.com",
     *"name":"John Doe",
     *"password":"Password!23",
     *"userTier":"Free",
     *"username":"user1",
     *"sQA1":"test",
     *"sQID1":"1",
     *"sQA2":"test",
     *"sQID2":"3",
     *"sQA3":"test",
     *"sQID3":"5"
     * }
     */
    @PostMapping(path = "/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        if(user.getUsername()== null || user.getEmail() == null || user.getPassword() == null) {
            response.put("Error", "Please fill out all fields");
            return ResponseEntity.ok(response);
        }
        if(!User.validateUsername(user.getUsername())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username does not meet criteria");
        }
        if(!User.validatePassword(user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password does not meet criteria");
        }
        if(!User.validateEmail(user.getEmail())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please enter a valid email address");
        }
        // Encrypt sensitive data before storing
        user.setPassword(User.encryptString(user.getPassword()));
        user.setsQA1(User.encryptString(user.getsQA1()));
        user.setsQA2(User.encryptString(user.getsQA2()));
        user.setsQA3(User.encryptString(user.getsQA3()));
        UserProfile userProfile = new UserProfile(false, user.getUsername());
        userProfileRepository.save(userProfile);
        user.SetUserProfile(userProfile);
        userRepository.save(user);
        userProfileRepository.save(userProfile);
        Token token = tokenService.createToken(user);
        response.put("token", token.getTokenValue());
        response.put("message", "Login successful");
        response.put("user_id", String.valueOf(user.getUserId()));
        return ResponseEntity.ok(response);

    }

    @DeleteMapping(path = "/users/{id}")
    public void deleteUser(@PathVariable int id) {
        tokenRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }


    /*
    * Provide a JSON request containing the id of the user your updating and fields name, username and email only
    * include the fields that need updated realistically only one would be updated at a time but all 3 can if need be
    * Example:
    * {
    * "userId": "24",
    * "name": "new name"
    * }
    */
    @PutMapping(path = "/updateUser")
    public User updateUser(@RequestBody User updatedUser) {
        long user_id = updatedUser.getUserId();
        User existingUser = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        // Update only if the field is provided (not null)
        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getUserTier() != null) {
            existingUser.setUserTier(updatedUser.getUserTier());
        }
        return userRepository.save(existingUser);
    }


    /*
     * Provide a JSON request containing the id of the user your updating if the user remembers there old password
     * you provide that if not you provide an answer to a security question
     * Example:
     * {
     * "userId": "1",
     * "securityQuestionId":"1" if resetting via SQ otherwise it's not needed
     * "oldPassword":"Password!23" or "securityQuestionAnswer":"value"
     * "newPassword":"Password!234"
     * }
     */
    @PutMapping(path = "/updatePassword")
    public  ResponseEntity<?> updatePassword(@RequestBody ResetPasswordRequest passwordRequest) {
        long user_id = passwordRequest.getUserId();
        User existingUser = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        String oldpass = passwordRequest.getOldPassword();
        String securityQuestion = passwordRequest.getSecurityQuestionAnswer();
        Map<String, String> response = new HashMap<>();
        if (!User.validatePassword(passwordRequest.getNewPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password does not meet criteria");
        }

        if (oldpass != null && !existingUser.compareHashedPassword(passwordRequest.getOldPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect old password provided");

        } else if (oldpass != null && existingUser.compareHashedPassword(passwordRequest.getOldPassword())) {
            existingUser.setPassword(User.encryptString(passwordRequest.getNewPassword()));
            userRepository.save(existingUser);
            response.put("message", "Password change successful");
            return ResponseEntity.ok(response);
        }

        if (securityQuestion != null && existingUser.compareHashedSQ(passwordRequest.getSecurityQuestionAnswer()
                , passwordRequest.getSecurityQuestionId())) {
            existingUser.setPassword(User.encryptString(passwordRequest.getNewPassword()));
            userRepository.save(existingUser);
            response.put("message", "Password change successful");
            return ResponseEntity.ok(response);
        } else if (securityQuestion != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect security question answer provided");
        }

        response.put("message", "Invalid request");
        return ResponseEntity.ok(response);
    }


    /*
     * Provide a JSON request containing the users email or username and there password
     * Example:
     * {
     * "email":"testemail@gmail.com" or "username":"user1"
     * "password":"Password!23"
     * }
     */
    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Map<String, String> response = new HashMap<>();
        User user = null;
        try {
            if (loginRequest.getUsername() != null) {
                user = userRepository.findByUsername(loginRequest.getUsername());
                if(user == null){
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Invalid Username, Email or Password"); // Username doesn't exist
                }
            } else if (loginRequest.getEmail() != null) {
                user = userRepository.findByEmail(loginRequest.getEmail());
                if(user == null){
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Invalid Username, Email or Password"); // Email doesn't exist
                }
            } else {
                response.put("message", "Invalid Request");
                return ResponseEntity.ok(response); // Isn't provided a username or email in request
            }

            if (user.compareHashedPassword(loginRequest.getPassword())) {
                Token token = tokenService.createToken(user);
                response.put("token", token.getTokenValue());
                response.put("message", "Login successful");
                response.put("user_id", String.valueOf(user.getUserId()));
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid Username, Email or Password"); // Password is Wrong
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during login");
        }
    }
    @GetMapping(path = "/searchUsersByUsername/{username}")
    public List<User> searchUsersByUsername(@PathVariable String username) {
        return userRepository.findByUsernameIgnoreCaseContaining(username);
    }
}
