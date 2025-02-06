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

    public User getInfoByUUID(UUID user_id) {
        return userRepo.findById(user_id).orElse(null);
    }

    public User getInfoByUsername(String username) {
        return userRepo.findByUsername(username).orElse(null);
    }

    public User updateUserInfo(User user) {
        User updateInfo = userRepo.findById(user.getUser_id()).orElse(null);
        if(updateInfo!=null){
            updateInfo.setUsername(user.getUsername());
            updateInfo.setPassword(user.getPassword());
            updateInfo.setFirst_name(user.getFirst_name());
            updateInfo.setLast_name(user.getLast_name());
            userRepo.save(updateInfo);
            return updateInfo;
        }
       return null;
    }

    public void deleteUserInfo(String username){
        userRepo.deleteByUsername(username);
    }
}
