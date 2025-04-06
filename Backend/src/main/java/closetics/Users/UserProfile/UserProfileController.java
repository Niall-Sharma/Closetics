package closetics.Users.UserProfile;


import java.util.List;
import java.util.stream.Collectors;

import closetics.Users.User;
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
  public UserProfileDTO GetUserProfile(@PathVariable long id){
    return new UserProfileDTO(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Profile not found")).GetUserProfile(), 2);
  }
  @GetMapping(path = "/userprofile")
  public List<UserProfileDTO> GetAllUserProfiles(){
    return uRepository.findAll().stream().map(p -> new UserProfileDTO(p, 2)).toList();
  }

  @PostMapping(path = "/userprofile")
  public UserProfileDTO CreateUserProfile(@RequestBody UserProfile userProfile){
    uRepository.save(userProfile);
    return new UserProfileDTO(userProfile, 2);
  }

  @PutMapping(path = "/addFollowing/{id}/{followingId}")
  public UserProfileDTO AddFollowingToProfile(@PathVariable("id") long id, @PathVariable("followingId") long followingId){
    UserProfile userProfile = userRepository.findById(id).get().GetUserProfile();
    UserProfile followingUser = userRepository.findById(followingId).get().GetUserProfile();
    if(id != followingId){
      userProfile.addFollowing(followingUser);
      uRepository.save(userProfile);
    }

    return new UserProfileDTO(userProfile, 2);
  }
  @PutMapping(path = "/removeFollowing/{id}/{followingId}")
  public UserProfileDTO RemoveFollowingFromProfile(@PathVariable("id") long id, @PathVariable("followingId") long followingId){
    UserProfile userProfile = userRepository.findById(id).get().GetUserProfile();
    UserProfile followingUser = userRepository.findById(followingId).get().GetUserProfile();
    userProfile.removeFollowing(followingUser);
    uRepository.save(userProfile);
    return new UserProfileDTO(userProfile, 2);
  }


//  @GetMapping("/userprofile/followers/{id}")
//  public List<UserProfile> GetFollowers(@PathVariable("id") Long id){
//    return uRepository.findById(id).get().getFollowers();
//  }

  @GetMapping("/userprofile/following/{id}")
  public List<UserProfileDTO> GetFollowing(@PathVariable("id") Long id){
    UserProfile userProfile = uRepository.findById(id).get();
    UserProfileDTO userProfileDTO = new UserProfileDTO(userProfile, 2);
    return userProfileDTO.getFollowing();

  }
  //ADD ENDPOINT TO RETURN OR FALSE IF YOU ARE FOLLOWING THEM.
  @GetMapping("/userprofile/outfits/{id}")
  public List<Outfit> GetOutfits(@PathVariable("id") Long id){
    return uRepository.findById(id).get().getOutfits();
  }

  @PutMapping("/userprofile/swappublicsetting/{id}")
  public UserProfileDTO swapFavorite(@PathVariable long id) {
    UserProfile userProfile = uRepository.findById(id).get();
    if (userProfile != null) {
      userProfile.setIsPublic(!userProfile.getIsPublic());
      uRepository.save(userProfile);
      return new UserProfileDTO(userProfile, 2);
    }else{
      throw new RuntimeException();
      }
    }

    @GetMapping("/userprofile/isFollowing/{id}/{followingId}")
    public boolean IsFollowing(@PathVariable long id, @PathVariable long followingId){
      UserProfile userProfile = uRepository.findById(id).orElseThrow(() -> new RuntimeException("User Profile Not Found"));
      for (int i = 0; i < userProfile.getFollowing().size(); i++)
      {
        UserProfile follower = userProfile.getFollowing().get(i);
        if(follower.getId() == followingId){
          return true;
        }
      }
      return false;
    }
}
