package coms309.Service;

import coms309.Entities.User;
import coms309.DatabaseTables.UserTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserTable userEntity;

    public User saveInfo(User user) {
        return userEntity.save(user);
    }
}
