package closetics.Users.UserProfile;



import java.util.List;

import closetics.Outfits.Outfit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity(name = "user_profiles_table")
public class UserProfile{
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long userProfileId;

  boolean isPublic;
  String username;

  @Column(name = "UID")
  long UUID;
  
  //SEPERATE ENDPOINT
  @OneToMany
  @JoinColumn(name = "outfit_id")
  private List<Outfit> outfits;

  //SEPERATE ENDPOINT
  @OneToMany
  @JoinColumn(name = "following_user_id")
  private List<UserProfile> following_id;
  //SEPERATE ENDPOINT
  @ManyToOne
  @JoinColumn(name = "user_followers_id")
  private List<UserProfile> followers_id;

  public UserProfile(boolean isPublic, String username, long UUID){
    this.UUID = UUID;
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
    following_id.remove(following_id.indexOf(following));
  }
  public boolean GetIsPublic()
  {
    return isPublic;
  }
  public void SetIsPublic(boolean b){
    isPublic = b;
  }
}
