package closetics.Outfits;

import closetics.Users.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "outfit_table")
public class Outfit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long outfitId;

    private long userId;

    private String outfitName;
    private Date creationDate;
    private boolean isFavorite;

    @ElementCollection
    @CollectionTable(name = "outfit_items", joinColumns = @JoinColumn(name = "outfit_id"))
    @Column(name = "clothing_id")
    private List<Long> outfitItems = new ArrayList<>();

    public Outfit(long outfitId, long userId, String outfitName, Date creationDate, boolean isFavorite, List<Long> outfitItems) {
        this.outfitId = outfitId;
        this.userId = userId;
        this.outfitName = outfitName;
        this.creationDate = creationDate;
        this.isFavorite = isFavorite;
        this.outfitItems = outfitItems;
    }

    public Outfit(){}

    public long getOutfitId() {return outfitId;}
    public void setOutfitId(long outfitId) {this.outfitId = outfitId;}

    public long getUserId() {return userId;}
    public void setUserId(long userId) {this.userId = userId;}

    public String getOutfitName() {return outfitName;}
    public void setOutfitName(String outfitName) {this.outfitName = outfitName;}

    public Date getCreationDate() {return creationDate;}
    public void setCreationDate(Date creationDate) {this.creationDate = creationDate;}

    public boolean isFavorite() {return isFavorite;}
    public void setFavorite(boolean favorite) {isFavorite = favorite;}

    public List<Long> getOutfitItems() {return outfitItems;}
    public void setOutfitItems(List<Long> outfitItems) {this.outfitItems = outfitItems;}
}
