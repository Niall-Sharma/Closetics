package closetics.Users;

import java.util.List;
import closetics.Users.Auth.AuthService;
import closetics.Users.Auth.RequiresAuth;
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
    public User signUp(@RequestBody User user) {
        if(!User.validateUsername(user.getUsername())){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Username does not meet criteria");
        }
        if(!User.validatePassword(user.getPasswordHash())){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Password does not meet criteria");
        }
        user.setPasswordHash(User.encryptPassword(user.getPasswordHash()));
        return userRepo.save(user);
    }
    @RequiresAuth
    @DeleteMapping(path = "/users/{id}")
    public void deleteUser(@PathVariable int id) {
        tokenRepo.deleteByUserId(id);
        userRepo.deleteById(id);
    }

    @PutMapping(path = "/updateUser")
    public User updateUser(@RequestBody User user) {
        user.setPasswordHash(User.encryptPassword(user.getPasswordHash()));
        return userRepo.save(user);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userRepo.findByUsername(loginRequest.getUsername());

            if (user != null && user.comparePasswordHash(loginRequest.getPassword())) {
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
