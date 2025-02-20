package closetics.Users;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {

    //This error is meant to trigger whenever a duplicate entry is added into the MySql database
    @ResponseStatus(value=HttpStatus.CONFLICT,
            reason="Data integrity violation")  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String conflict() {
        return "ACCESS VIOLATION";
    }

    @Autowired
    UserRepository userRepo;

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
    public User getUserByUsername(@PathVariable String id){
        return userRepo.findByUsername(id);
    }

    @PostMapping(path = "/users")
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

    @DeleteMapping(path = "/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userRepo.deleteById(id);
    }

    @PutMapping(path = "/users")
    public User updateUser(@RequestBody User user) {
        return userRepo.save(user);
    }
    //Was doing some testing using spring security to and json web token where this login API call would generate a token that
    // would be saved passed to other API calls and without it you would be unable but having quite figured out the best approach for that yet
    @GetMapping(path = "/login/{username}/{password}")
    public String userLogin(@PathVariable String username, @PathVariable String password) {
        User user = userRepo.findByUsername(username);
        if (user != null && user.getPasswordHash().equals(password)) {
            // Passwords match, login successful
            return "Login successful for user: " + username;
        } else {
            // User not found or incorrect password
            return "Invalid username or password!";
        }
    }
}
