package coms309.Service;

import coms309.EntityCreation.User;
import coms309.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public User postInfo(User user) {
        return userRepo.save(user);
    }

    public User getInfoByUUID(UUID userId) {
        return userRepo.findById(userId).orElse(null);
    }

    public User getInfoByUsername(String username) {
        return userRepo.findByUsername(username).orElse(null);
    }

    public User updateUserInfo(User user) {
        User updateInfo = userRepo.findById(user.getUserId()).orElse(null);
        if(updateInfo!=null){
            updateInfo.setUsername(user.getUsername());
            updateInfo.setPassword(user.getPassword());
            updateInfo.setFirstName(user.getFirstName());
            updateInfo.setLastName(user.getLastName());
            userRepo.save(updateInfo);
            return updateInfo;
        }
       return null;
    }

    public void deleteUserInfo(String username){
        userRepo.deleteByUsername(username);
    }
}
