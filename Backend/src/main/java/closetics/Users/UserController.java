package closetics.Users;

import java.util.List;
import closetics.Users.Auth.AuthService;
import closetics.Users.Tokens.Token;
import closetics.Users.Tokens.TokenRepository;
import closetics.Users.Tokens.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
import java.util.HashMap;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TokenRepository tokenRepo;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthService authService;

    //This error is meant to trigger whenever a duplicate entry is added into the MySql database
    @ResponseStatus(value = HttpStatus.CONFLICT,
            reason = "Data integrity violation")  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String conflict() {
        return "ACCESS VIOLATION";
    }

    @GetMapping(path = "/users")
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @GetMapping(path = "/users/{id}")
    public User getUser(@PathVariable int id) {
        return userRepo.findById(id);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation")
    @GetMapping(path = "/users/username/{id}")
    public User getUserByUsername(@PathVariable String id) {
        return userRepo.findByUsername(id);
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        if(!User.validateUsername(user.getUsername())){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Username does not meet criteria");
        }
        if(!User.validatePassword(user.getPasswordHash())){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Password does not meet criteria");
        }
        // Encrypt sensitive data before storing
        user.setPasswordHash(User.encryptString(user.getPasswordHash()));
        user.setSecurityQuestion1(User.encryptString(user.getSecurityQuestion1()));
        user.setSecurityQuestion2(User.encryptString(user.getSecurityQuestion2()));
        user.setSecurityQuestion3(User.encryptString(user.getSecurityQuestion3()));
        userRepo.save(user);
        Token token = tokenService.createToken(user);
        Map<String, String> response = new HashMap<>();
        response.put("token", token.getTokenValue());
        response.put("message", "Login successful");
        response.put("username", user.getUsername());
        return ResponseEntity.ok(response);

    }

    @DeleteMapping(path = "/users/{id}")
    public void deleteUser(@PathVariable int id) {
        tokenRepo.deleteByUserId(id);
        userRepo.deleteById(id);
    }


    /*
    * Provide a JSON request containing the id of the user your updating and fields name, username and email only
    * include the fields that need updated realistically only one would be updated at a time but all 3 can if need be
    * Example:
    * {
    * "id": "24",
    * "name": "new name"
    * }
    */
    @PutMapping(path = "/updateUser")
    public User updateUser(@RequestBody User updatedUser) {
        User existingUser = userRepo.findById(updatedUser.getId());
        // Update only if the field is provided (not null)
        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }
        if (updatedUser.getEmailId() != null) {
            existingUser.setEmailId(updatedUser.getEmailId());
        }
        return userRepo.save(existingUser);
    }


    /*
     * Provide a JSON request containing the id of the user your updating if the user remembers there old password
     * you provide that if not you provide an answer to a security question
     * Example:
     * {
     * "id": "1",
     * "oldPassword":"Password!23" or "securityQuestionAnswer":"value"
     * "newPassword":"Password!234"
     * }
     */
    @PutMapping(path = "/updatePassword")
    public  ResponseEntity<?> updatePassword(@RequestBody ResetPasswordRequest passwordRequest) {
        User existingUser = userRepo.findById(passwordRequest.getId());
        String oldpass = passwordRequest.getOldPassword();
        String securityQuestion = passwordRequest.getSecurityQuestionAnswer();
        Map<String, String> response = new HashMap<>();
        if (!User.validatePassword(passwordRequest.getNewPassword())){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Password does not meet criteria");
        }

        if (oldpass != null && !existingUser.compareHashedPassword(passwordRequest.getOldPassword())){
            response.put("message", "Incorrect password provided");
            return ResponseEntity.ok(response);
        } else if (oldpass != null && existingUser.compareHashedPassword(passwordRequest.getOldPassword())) {
            existingUser.setPasswordHash(User.encryptString(passwordRequest.getNewPassword()));
            userRepo.save(existingUser);
            response.put("message", "Password change successful");
            return ResponseEntity.ok(response);
        }

        if (securityQuestion != null && existingUser.compareHashedSQ(passwordRequest.getSecurityQuestionAnswer())) {
            existingUser.setPasswordHash(User.encryptString(passwordRequest.getNewPassword()));
            userRepo.save(existingUser);
            response.put("message", "Password change successful");
            return ResponseEntity.ok(response);
        } else if (securityQuestion != null) {
            response.put("message", "Incorrect security question answer provided");
            return ResponseEntity.ok(response);
        }

        response.put("message", "Invalid request");
        return ResponseEntity.ok(response);
    }


    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userRepo.findByUsername(loginRequest.getUsername());

            if (user != null && user.compareHashedPassword(loginRequest.getPassword())) {
                Token token = tokenService.createToken(user);
                Map<String, String> response = new HashMap<>();
                response.put("token", token.getTokenValue());
                response.put("message", "Login successful");
                response.put("username", user.getUsername());
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during login");
        }
    }
}
