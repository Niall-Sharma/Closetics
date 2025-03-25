package closetics.Users.UserProfile;



import java.util.List;

import closetics.Outfits.Outfit;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

public class UserProfile{
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long userProfileId;

  boolean isPublic;
  String username;
  long UUID;
  
  @OneToMany
  @JoinColumn(name = "outfit_id")
  private List<Outfit> outfits;

  @OneToMany
  @JoinColumn(name = "following_id")
  private List<UserProfile> following_id;

  @ManyToOne
  @JoinColumn(name = "followers_id")
  private List<UserProfile> followers_id;

  public UserProfile(boolean isPublic, String username, long UUID){
    this.UUID = UUID;
    this.username = username;
    this.isPublic = isPublic;
  }

  public List<Outfit> GetOutfits(){
    return outfits;
  }

  public List<UserProfile> GetFollowing(){
    return following_id;
  }

  public List<UserProfile> GetFollowers(){
    return followers_id;
  }

}
