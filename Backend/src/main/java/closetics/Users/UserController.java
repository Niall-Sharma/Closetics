package closetics.Users;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

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

    @PostMapping(path = "/users")
    public User SignUp(@RequestBody User user) {
        return userRepo.save(user);
    }

    @PutMapping(path = "/users/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        User u = userRepo.findById(id);
        if(u == null) {
            return null;
        }
        userRepo.save(user);
        return userRepo.findById(id);
    }

    @DeleteMapping(path = "/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userRepo.deleteById(id);
    }

}
