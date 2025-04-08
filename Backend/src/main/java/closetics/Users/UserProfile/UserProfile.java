package closetics.Users.UserProfile;



import java.util.ArrayList;
import java.util.List;

import closetics.Outfits.Outfit;
import closetics.Users.User;
import jakarta.persistence.*;

@Entity(name = "user_profiles_table")
public class UserProfile{
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long id;

  boolean isPublic;
  String username;

  @OneToOne
  @JoinColumn(name = "user_id")
  User user;

  @OneToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "users_outfits",
          joinColumns = @JoinColumn(name = "UID"),
          inverseJoinColumns = @JoinColumn(name = "outfit_id")
  )
  private List<Outfit> outfits;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "user_following",
          joinColumns = @JoinColumn(name = "UID"),
          inverseJoinColumns = @JoinColumn(name = "following_id")
  )
  private List<UserProfile> following_id;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "user_followers",
          joinColumns = @JoinColumn(name = "UID"),
          inverseJoinColumns = @JoinColumn(name = "follower_id")
  )
  private List<UserProfile> followers_id;


  public UserProfile(boolean isPublic, String username){
    this.username = username;
    this.isPublic = isPublic;
  }

  public UserProfile(){}

  public List<Outfit> GetOutfits(){
    return outfits;
  }
  
  public void AddOutfit(Outfit outfit){
    outfits.add(outfit);
  }

  public List<UserProfile> GetFollowing(){
    return following_id;
  }

  public List<UserProfile> GetFollowers(){
    return followers_id;
  }
  public void AddFollower(UserProfile follower){
    followers_id.add(follower);
  }

  public void RemoveFollower(UserProfile follower){
    followers_id.remove(followers_id.indexOf(follower));  
  }

  public void AddFollowing(UserProfile following){
    following_id.add(following);
  }
  public void RemoveFollowing(UserProfile following){
    following_id.remove(following);
  }
  public boolean GetIsPublic()
  {
    return isPublic;
  }
  public void SetIsPublic(boolean b){
    isPublic = b;
  }
  public void SetUser(User user){
    this.user = user;
  }
}
