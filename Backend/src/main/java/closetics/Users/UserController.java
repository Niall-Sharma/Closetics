package closetics.Users;

import java.util.List;

import closetics.Outfits.OutfitRepository;
import closetics.Users.Auth.AuthService;
import closetics.Users.Tokens.Token;
import closetics.Users.Tokens.TokenRepository;
import closetics.Users.Tokens.TokenService;
import closetics.Users.UserProfile.UserProfile;
import closetics.Users.UserProfile.UserProfileRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@Tag(name = "Users", description = "Endpoints for user management, authentication, and account operations")
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

    //Maybe move this in the future so that User doesn't have to deal with it and only makes a call towards UserProfile
    @Autowired
    private OutfitRepository outfitRepository;


    //This error is meant to trigger whenever a duplicate entry is added into the MySql database
    @ResponseStatus(value = HttpStatus.CONFLICT,
            reason = "Data integrity violation")  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String conflict() {
        return "ACCESS VIOLATION";
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users. Sensitive data (like password hash) should ideally be excluded.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of all users",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class))))
            // Note: Exposing all user data like this might be a security risk.
    })
    @GetMapping(path = "/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a specific user by their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping(path = "/users/{id}")
    public User getUser(@Parameter(description = "ID of the user to retrieve") @PathVariable long id) {
        return userRepository.findById(id).get();
    }

    @Operation(summary = "Get user by username", description = "Retrieves a specific user by their unique username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found for the given username", content = @Content)
    })
    @GetMapping(path = "/users/username/{id}") // Path parameter name is 'id' but refers to username
    public User getUserByUsername(@Parameter(description = "Username of the user to retrieve") @PathVariable String id) {
        User user = userRepository.findByUsername(id);
        if (user == null) {
            // Throw an exception or return ResponseEntity.notFound()
        }
        return user;
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
    @Operation(summary = "User signup", description = "Registers a new user account, creates a profile, encrypts sensitive data, and returns an auth token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signup successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SignUpResponseSchema.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (missing fields, invalid format)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))), // For the explicit check
            @ApiResponse(responseCode = "401", description = "Validation failed (username/password/email criteria not met)",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "Conflict (e.g., username or email already exists)",
                    content = @Content(mediaType = "text/plain")) // From the @ExceptionHandler
    })
    @PostMapping(path = "/signup")
    public ResponseEntity<?> signUp(
            @RequestBody(description = "User details for signup. Requires username, email, password, and security questions/answers.", required = true,
                    content = @Content(schema = @Schema(implementation = User.class)))
            @org.springframework.web.bind.annotation.RequestBody User user) {
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
        if(!User.validateEmail(user.getEmail().toLowerCase())){
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

    @Operation(summary = "Delete user by ID", description = "Deletes a user account and their associated auth tokens.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
            // Consider response codes for deletion failure
    })
    @DeleteMapping(path = "/users/{id}")
    public void deleteUser(@Parameter(description = "ID of the user to delete") @PathVariable int id) {
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
    @Operation(summary = "Update user details", description = "Updates non-sensitive user details like name, username, email, or user tier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping(path = "/updateUser")
    public User updateUser(
            @RequestBody(description = "User details to update. Requires userId. Include name, username, email, or userTier to modify them.", required = true,
                    content = @Content(schema = @Schema(implementation = User.class))) // Schema shows all User fields, but only some are used.
            @org.springframework.web.bind.annotation.RequestBody User updatedUser) {
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
    @Operation(summary = "Update user password", description = "Updates a user's password, either by verifying the old password or a security question answer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password change successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized (Incorrect old password or security answer, or new password format invalid)",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping(path = "/updatePassword")
    public  ResponseEntity<?> updatePassword(
            @RequestBody(description = "Request to reset password. Requires userId and newPassword. Requires EITHER oldPassword OR (securityQuestionId and securityQuestionAnswer).", required = true,
                    content = @Content(schema = @Schema(implementation = ResetPasswordRequest.class)))
            @org.springframework.web.bind.annotation.RequestBody ResetPasswordRequest passwordRequest) {
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
    @Operation(summary = "User login", description = "Authenticates a user using username/email and password, returns an auth token upon success.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseSchema.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request (missing username/email)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))), // For the explicit check
            @ApiResponse(responseCode = "401", description = "Unauthorized (Invalid username, email, or password)",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error during login",
                    content = @Content(mediaType = "text/plain"))
    })
    @PostMapping(path = "/login")
    public ResponseEntity<?> login(
            @RequestBody(description = "Login credentials. Requires password and EITHER username OR email.", required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequest.class)))
            @org.springframework.web.bind.annotation.RequestBody LoginRequest loginRequest) {
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
    @Operation(summary = "Search users by username (partial match)", description = "Finds users whose usernames contain the provided string (case-insensitive).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of matching users",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class))))
            // Consider adding 404 if no users match?
    })
    @GetMapping(path = "/searchUsersByUsername/{username}")
    public List<User> searchUsersByUsername(@Parameter(description = "Partial username to search for") @PathVariable String username) {
        return userRepository.findByUsernameIgnoreCaseContaining(username);
    }

    // --- Helper Schemas for Responses ---

    @Schema(description = "Response body for successful signup or login")
    private static class AuthResponseBaseSchema {
        @Schema(description = "Authentication token", example = "some-jwt-token-value")
        public String token;
        @Schema(description = "Success message", example = "Login successful")
        public String message;
        @Schema(description = "ID of the authenticated user", example = "123")
        public String user_id;
    }

    @Schema(description = "Response body for successful signup")
    private static class SignUpResponseSchema extends AuthResponseBaseSchema {}

    @Schema(description = "Response body for successful login")
    private static class LoginResponseSchema extends AuthResponseBaseSchema {}

}
