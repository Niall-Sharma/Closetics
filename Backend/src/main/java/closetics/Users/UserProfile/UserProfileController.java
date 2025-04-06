package closetics.Users.UserProfile;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import closetics.Outfits.Outfit;
import closetics.Users.UserRepository;

@RestController
public class UserProfileController{

  @Autowired
  UserProfileRepository uRepository;

  @Autowired
  UserRepository userRepository;

  @GetMapping(path = "/userprofile/{id}")
  public UserProfile GetUserProfile(@PathVariable long id){
    return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Profile not found")).GetUserProfile();
  }
  @GetMapping(path = "/userprofile")
  public List<UserProfile> GetAllUserProfiles(){
    return uRepository.findAll();
  }

  @PostMapping(path = "/userprofile")
  public UserProfile CreateUserProfile(@RequestBody UserProfile userProfile){
    uRepository.save(userProfile);
    return userProfile;
  }

  @PutMapping(path = "/addFollowing/{id}/{followingId}")
  public UserProfile AddFollowingToProfile(@PathVariable("id") long id, @PathVariable("followingId") long followingId){
    UserProfile userProfile = userRepository.findById(id).get().GetUserProfile();
    UserProfile followingUser = userRepository.findById(followingId).get().GetUserProfile();
    if(id != followingId){
      userProfile.addFollowing(followingUser);
      followingUser.addFollower(userProfile);
      uRepository.save(userProfile);
      uRepository.save(followingUser);

    }
    return userProfile;
  }
  @PutMapping(path = "/removeFollowing/{id}/{followingId}")
  public UserProfile RemoveFollowingFromProfile(@PathVariable("id") long id, @PathVariable("followingId") long followingId){
    UserProfile userProfile = userRepository.findById(id).get().GetUserProfile();
    UserProfile followingUser = userRepository.findById(followingId).get().GetUserProfile();
    userProfile.removeFollowing(followingUser);
    followingUser.removeFollower(userProfile);
    uRepository.save(userProfile);
    uRepository.save(followingUser);
    return userProfile;
  }


  @GetMapping("/userprofile/followers/{id}")
  public List<UserProfile> GetFollowers(@PathVariable("id") Long id){
    return uRepository.findById(id).get().getFollowers();
  }

  @GetMapping("/userprofile/following/{id}")
  public List<UserProfile> GetFollowing(@PathVariable("id") Long id){
    return uRepository.findById(id).get().getFollowing();
  }

  @GetMapping("/userprofile/outfits/{id}")
  public List<Outfit> GetOutfits(@PathVariable("id") Long id){
    return uRepository.findById(id).get().getOutfits();
  }

  @PutMapping("/userprofile/swappublicsetting/{id}")
  public UserProfile swapFavorite(@PathVariable long id) {
    UserProfile userProfile = uRepository.findById(id).get();
    if (userProfile != null) {
      userProfile.setIsPublic(!userProfile.getIsPublic());
      uRepository.save(userProfile);
      return userProfile;
    }else{
      throw new RuntimeException();
      }
    }
}
