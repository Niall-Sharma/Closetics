package closetics.Users.UserProfile;


import java.util.List;

import closetics.Users.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  @Operation(summary = "Return a specific user profile by user ID")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDTO.class))),
          @ApiResponse(responseCode = "404", description = "User profile not found", content = @Content)
  })
  @GetMapping(path = "/userprofile/{id}")
  public UserProfileDTO GetUserProfile(@PathVariable long id){
    return new UserProfileDTO(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Profile not found")).GetUserProfile(), 2);
  }

  @Operation(summary = "Return all user profiles")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved all user profiles",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDTO.class)))
  })
  @GetMapping(path = "/userprofile")
  public List<UserProfileDTO> GetAllUserProfiles(){
    return uRepository.findAll().stream().map(p -> new UserProfileDTO(p, 2)).toList();
  }


  @Operation(summary = "Create a user profile (not linked to a user)")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Successfully created user profile",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDTO.class))),
          @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
  })
  @PostMapping(path = "/userprofile")
  public UserProfileDTO CreateUserProfile(@RequestBody UserProfile userProfile){
    uRepository.save(userProfile);
    return new UserProfileDTO(userProfile, 2);
  }

  @Operation(summary = "Add a following relationship between two users")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully added following",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDTO.class))),
          @ApiResponse(responseCode = "404", description = "User profile not found", content = @Content)
  })
  @PutMapping(path = "/addFollowing/{id}/{followingId}")
  public UserProfileDTO AddFollowingToProfile(@PathVariable("id") long id, @PathVariable("followingId") long followingId){
    UserProfile userProfile = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Profile Not Found")).GetUserProfile();
    UserProfile followingUser = userRepository.findById(followingId).orElseThrow(() -> new RuntimeException("Following User Profile Not Found")).GetUserProfile();
    if(id != followingId){
      userProfile.addFollowing(followingUser);
      followingUser.addFollower(userProfile);
      uRepository.save(userProfile);
      uRepository.save(followingUser);
    }

    return new UserProfileDTO(userProfile, 2);
  }

  @Operation(summary = "Remove a following relationship between two users")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully removed following",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDTO.class))),
          @ApiResponse(responseCode = "404", description = "User profile not found", content = @Content)
  })
  @PutMapping(path = "/removeFollowing/{id}/{followingId}")
  public UserProfileDTO RemoveFollowingFromProfile(@PathVariable("id") long id, @PathVariable("followingId") long followingId){
    UserProfile userProfile = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Profile Not Found")).GetUserProfile();
    UserProfile followingUser = userRepository.findById(followingId).orElseThrow(() -> new RuntimeException("Following User Profile Not Found")).GetUserProfile();
    userProfile.removeFollowing(followingUser);
    uRepository.save(userProfile);
    return new UserProfileDTO(userProfile, 2);
  }



  @Operation(summary = "Get a list of followers for a user")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved followers list",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDTO.class))),
          @ApiResponse(responseCode = "404", description = "User profile not found", content = @Content)
  })
  @GetMapping("/userprofile/followers/{id}")
  public List<UserProfileDTO> GetFollowers(@PathVariable("id") Long id){
    UserProfile userProfile = uRepository.findById(id).orElseThrow(() -> new RuntimeException("User Profile Not Found"));
    UserProfileDTO userProfileDTO = new UserProfileDTO(userProfile, 2);
    return userProfileDTO.getFollowers();
  }

  @Operation(summary = "Get a list of users that the user is following")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved following list",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDTO.class))),
          @ApiResponse(responseCode = "404", description = "User profile not found", content = @Content)
  })
  @GetMapping("/userprofile/following/{id}")
  public List<UserProfileDTO> GetFollowing(@PathVariable("id") Long id){
    UserProfile userProfile = uRepository.findById(id).orElseThrow(() -> new RuntimeException("User Profile Not Found"));
    UserProfileDTO userProfileDTO = new UserProfileDTO(userProfile, 2);
    return userProfileDTO.getFollowing();

  }

  @Operation(summary = "Get a user's outfits")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved user's outfits",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = Outfit.class))),
          @ApiResponse(responseCode = "404", description = "User profile not found", content = @Content)
  })
  @GetMapping("/userprofile/outfits/{id}")
  public List<Outfit> GetOutfits(@PathVariable("id") Long id){
    return uRepository.findById(id).get().getOutfits();
  }

  @Operation(summary = "Toggle the public/private setting of a user profile")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully changed public setting",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDTO.class))),
          @ApiResponse(responseCode = "404", description = "User profile not found", content = @Content)
  })
  @PutMapping("/userprofile/swappublicsetting/{id}")
  public UserProfileDTO swapFavorite(@PathVariable long id) {
    UserProfile userProfile = uRepository.findById(id).orElseThrow(() -> new RuntimeException("User Profile Not Found"));
    if (userProfile != null) {
      userProfile.setIsPublic(!userProfile.getIsPublic());
      uRepository.save(userProfile);
      return new UserProfileDTO(userProfile, 2);
    }else{
      throw new RuntimeException();
      }
    }

  @Operation(summary = "Check if a user is following another user")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully checked following status",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
          @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
          @ApiResponse(responseCode = "404", description = "User profile not found", content = @Content)
  })
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
