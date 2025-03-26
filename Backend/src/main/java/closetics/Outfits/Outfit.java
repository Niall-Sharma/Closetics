package closetics.Outfits;

import closetics.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private String outfitName;
    private LocalDateTime creationDate;
    private boolean favorite;

    @ElementCollection
    @CollectionTable(name = "outfit_items", joinColumns = @JoinColumn(name = "outfit_id"))
    @Column(name = "clothing_id")
    private List<Long> outfitItems = new ArrayList<>();

    public Outfit(long outfitId, User user, String outfitName, LocalDateTime creationDate, boolean favorite, List<Long> outfitItems) {
        this.outfitId = outfitId;
        this.user = user;
        this.outfitName = outfitName;
        this.creationDate = creationDate;
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

    public LocalDateTime getCreationDate() {return creationDate;}
    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}

    public boolean getFavorite() {return favorite;}
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;}

    public List<Long> getOutfitItems() {return outfitItems;}
    public void setOutfitItems(List<Long> outfitItems) {this.outfitItems = outfitItems;}
}
