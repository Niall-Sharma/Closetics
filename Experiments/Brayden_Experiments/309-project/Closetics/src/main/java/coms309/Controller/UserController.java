package coms309.Controller;

import coms309.EntityCreation.User;
import coms309.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/addUser")
    public User postInfo(@RequestBody User user) {
        return userService.postInfo(user);
    }

    @GetMapping("/getUserById/{user_id}")
    public User getInfoByUUID(@PathVariable UUID userId) {
        return userService.getInfoByUUID(userId);
    }

    @GetMapping("/getUser/{username}")
    public User getInfoByUsername(@PathVariable String username) {
        return userService.getInfoByUsername(username);
    }

    @PutMapping("/updateUser")
    public User updateUserInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }

    @DeleteMapping("/deleteUser/{username}")
    public void deleteUserInfo(@PathVariable String username){
        userService.deleteUserInfo(username);
    }
}
