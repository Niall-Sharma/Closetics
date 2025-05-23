package closetics.Outfits;

import closetics.Clothes.Clothing;
import closetics.Statistics.OutfitStats;
import closetics.Users.User;
import closetics.Users.UserProfile.UserProfile;
import closetics.Users.UserProfile.UserProfileDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity(name = "outfit_table")
public class Outfit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long outfitId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"email", "password", "userTier",
            "sQA1", "sQID1", "sQA2", "sQID2", "sQA3", "sQID3"})
    private User user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "stat_id")
    @JsonIgnoreProperties("datesWorn")
    private OutfitStats outfitStats;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "outfit_items",
            joinColumns = @JoinColumn(name = "outfit_id"),
            inverseJoinColumns = @JoinColumn(name = "clothing_id")
    )
    @JsonIgnoreProperties({"favorite", "size", "color",
            "dateBought", "brand", "imagePath", "itemName", "material", "price", "type", "specialType", "creationDate", "clothingType"})
    private List<Clothing> outfitItems;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_likes",
            joinColumns = @JoinColumn(name = "outfit_id"),
            inverseJoinColumns = @JoinColumn(name = "user_profile_id")
    )
    private List<UserProfile> userProfileLikes = new ArrayList<>();

    private String outfitName;
    private LocalDate creationDate;
    private boolean favorite;

    public Outfit(long outfitId, User user, String outfitName, boolean favorite, List<Clothing> outfitItems) {
        this.outfitId = outfitId;
        this.user = user;
        this.outfitName = outfitName;
        this.favorite = favorite;
        this.outfitItems = outfitItems;
    }

    public Outfit(){}

    public long getOutfitId() {return outfitId;}
    public void setOutfitId(long outfitId) {this.outfitId = outfitId;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    public String getOutfitName() {return outfitName;}
    public void setOutfitName(String outfitName) {this.outfitName = outfitName;}

    public LocalDate getCreationDate() {return creationDate;}
    public void setCreationDate(LocalDate creationDate) {this.creationDate = creationDate;}

    public boolean getFavorite() {return favorite;}
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;}

    public List<Clothing> getOutfitItems() {return outfitItems;}
    public void setOutfitItems(List<Clothing> outfitItems) {this.outfitItems = outfitItems;}

    public OutfitStats getOutfitStats() {
        return outfitStats;
    }

    public Outfit setOutfitStats(OutfitStats outfitStats) {
        this.outfitStats = outfitStats;
        return this;
    }

    public List<UserProfileDTO> getUserProfileLikes() {
        return this.userProfileLikes
                .stream()
                .map(p -> new UserProfileDTO(p, 1))
                .toList();
    }

    public void setUserProfileLikes(List<UserProfile> userProfileLikes) {
        this.userProfileLikes = userProfileLikes;
    }

    public void addUserProfileLike(UserProfile user){
        this.userProfileLikes.add(user);
    }

    public void removeUserProfileLike(UserProfile user){
        this.userProfileLikes.remove(user);
    }
}
