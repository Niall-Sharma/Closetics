package closetics.Outfits;

import closetics.Statistics.OutfitStats;
import closetics.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.time.LocalDate;
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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "stat_id")
    @JsonIgnoreProperties("datesWorn")
    private OutfitStats outfitStats;

    @ElementCollection
    @CollectionTable(name = "outfit_items", joinColumns = @JoinColumn(name = "outfit_id"))
    @Column(name = "clothing_id")
    private List<Long> outfitItems = new ArrayList<>();

    private String outfitName;
    private LocalDate creationDate;
    private boolean favorite;

    public Outfit(long outfitId, User user, String outfitName, boolean favorite, List<Long> outfitItems) {
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

    public List<Long> getOutfitItems() {return outfitItems;}
    public void setOutfitItems(List<Long> outfitItems) {this.outfitItems = outfitItems;}

    public OutfitStats getOutfitStats() {
        return outfitStats;
    }

    public Outfit setOutfitStats(OutfitStats outfitStats) {
        this.outfitStats = outfitStats;
        return this;
    }
}
