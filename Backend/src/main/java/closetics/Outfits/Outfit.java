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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String outfitName;
    private Date creationDate;
    private boolean isFavorite;

    @ElementCollection
    @CollectionTable(name = "outfit_items", joinColumns = @JoinColumn(name = "outfit_id"))
    @Column(name = "clothing_id")
    private List<Long> outfitItems = new ArrayList<>();

    public Outfit(long outfitId, User user, String outfitName, Date creationDate, boolean isFavorite) {
        this.outfitId = outfitId;
        this.user = user;
        this.outfitName = outfitName;
        this.creationDate = creationDate;
        this.isFavorite = isFavorite;
    }

    public long getOutfitId() {return outfitId;}
    public void setOutfitId(long outfitId) {this.outfitId = outfitId;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    public String getOutfitName() {return outfitName;}
    public void setOutfitName(String outfitName) {this.outfitName = outfitName;}

    public Date getCreationDate() {return creationDate;}
    public void setCreationDate(Date creationDate) {this.creationDate = creationDate;}

    public boolean isFavorite() {return isFavorite;}
    public void setFavorite(boolean favorite) {isFavorite = favorite;}
}
