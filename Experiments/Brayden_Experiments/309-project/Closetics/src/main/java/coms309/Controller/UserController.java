package coms309.Controller;

import coms309.EntityCreation.User;
import coms309.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService user_service;

    @PostMapping("/addUser")
    public User postInfo(@RequestBody User user) {
        return user_service.postInfo(user);
    }

    @GetMapping("/getUserById/{user_id}")
    public User getInfoByUUID(@PathVariable UUID user_id) {
        return user_service.getInfoByUUID(user_id);
    }

    @GetMapping("/getUser/{username}")
    public User getInfoByUsername(@PathVariable String username) {
        return user_service.getInfoByUsername(username);
    }

    @PutMapping("/updateUser")
    public User updateUserInfo(@RequestBody User user) {
        return user_service.updateUserInfo(user);
    }

    @DeleteMapping("/deleteUser/{username}")
    public void deleteUserInfo(@PathVariable String username){
        user_service.deleteUserInfo(username);
    }
}
