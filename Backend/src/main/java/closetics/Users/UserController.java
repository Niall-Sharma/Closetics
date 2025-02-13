package closetics.Users;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
        return userRepo.save(user);
    }

    @DeleteMapping(path = "/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userRepo.deleteById(id);
    }

}
