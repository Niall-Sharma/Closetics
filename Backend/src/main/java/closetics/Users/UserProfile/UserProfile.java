package closetics.Users.UserProfile;



import java.util.ArrayList;
import java.util.List;

import closetics.Outfits.Outfit;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import org.springframework.data.repository.cdi.Eager;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Entity(name = "user_profiles_table")
public class UserProfile{
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private boolean isPublic;
  private String username;

  @OneToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "users_outfits",
          joinColumns = @JoinColumn(name = "UID"),
          inverseJoinColumns = @JoinColumn(name = "outfit_id")
  )
  private List<Outfit> outfits = new ArrayList<>();



  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "user_following",
          joinColumns = @JoinColumn(name = "UID", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "following_id")
  )
  private List<UserProfile> following_id = new ArrayList<>();;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "user_followers",
          joinColumns = @JoinColumn(name = "UID", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "follower_id")
  )
  private List<UserProfile> followers_id = new ArrayList<>();;


  public UserProfile(boolean isPublic, String username){
    this.username = username;
    this.isPublic = isPublic;
  }

  public UserProfile(){}

  public List<Outfit> getOutfits(){
    return outfits;
  }
  public void addOutfit(Outfit outfit){
    outfits.add(outfit);
  }
  public List<UserProfile> getFollowing(){
    return following_id;
  }
  public List<UserProfile> getFollowers(){
    return followers_id;
  }
  public void addFollower(UserProfile follower){
    followers_id.add(follower);
  }
  public void removeFollower(UserProfile follower){
    followers_id.remove(follower);
  }
  public void addFollowing(UserProfile following){
    following_id.add(following);
  }
  public void removeFollowing(UserProfile following){
    following_id.remove(following);
  }
  public boolean getIsPublic()
  {
    return isPublic;
  }
  public void setIsPublic(boolean b){
    isPublic = b;
  }
  public String getUsername(){
    return username;
  }
  public void setUsername(String username){
    this.username = username;
  }
  public long getId() {
    return id;
  }
  public void removeOutfit(Outfit outfit){
    outfits.remove(outfit);
  }
}
