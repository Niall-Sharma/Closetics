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
    return userRepository.findById(id).get().GetUserProfile();
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
      userProfile.AddFollowing(followingUser);
      followingUser.AddFollower(userProfile);
      uRepository.save(userProfile);
      uRepository.save(followingUser);

    }
    return userProfile;
  }
  @PutMapping(path = "/removeFollowing/{id}/{followingId}")
  public UserProfile RemoveFollowingFromProfile(@PathVariable("id") long id, @PathVariable("followingId") long followingId){
    UserProfile userProfile = userRepository.findById(id).get().GetUserProfile();
    UserProfile followingUser = userRepository.findById(followingId).get().GetUserProfile();
    userProfile.RemoveFollowing(followingUser);
    followingUser.RemoveFollower(userProfile);
    uRepository.save(userProfile);
    uRepository.save(followingUser);
    return userProfile;
  }


  @GetMapping("/userprofile/followers/{userName}")
  public List<UserProfile> GetFollowers(String userName){
    return uRepository.findByUsername(userName).GetFollowers();
  }

  @GetMapping("/userprofile/following/{userName}")
  public List<UserProfile> GetFollowing(String userName){
    return uRepository.findByUsername(userName).GetFollowing();
  }

  @GetMapping("/userprofile/outfits/{userName}")
  public List<Outfit> GetOutfits(String userName){
    return uRepository.findByUsername(userName).GetOutfits();
  }
  @PutMapping("/userprofile/swappublicsetting/{id}")
  public UserProfile swapFavorite(@PathVariable long id) {
    UserProfile userProfile = uRepository.findById(id).get();
    if (userProfile != null) {
      userProfile.SetIsPublic(!userProfile.GetIsPublic());
      uRepository.save(userProfile);
      return userProfile;
    }else{
      throw new RuntimeException();
      }
    }
}
