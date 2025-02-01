package coms309.Controller;

import coms309.Entities.User;
import coms309.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService user_service;

    @PostMapping("/addUser")
    public User postInfo(@RequestBody User user) {
        return user_service.saveInfo(user);
    }
}
