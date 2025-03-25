package closetics.Users.UserProfile;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserProfileController{

  @Autowired
  UserProfileRepository uRepository;

  @GetMapping(path = "/userprofile/{id}")
  public UserProfile GetUserProfile(@PathVariable long id){
    return uRepository.findById(id);
  }

  @PostMapping(path = "/userprofile")
  public UserProfile CreateUserProfile(@RequestBody UserProfile userProfile){
    uRepository.save(userProfile);
    return userProfile;
  }
}
