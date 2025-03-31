package closetics.Outfits;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OutfitMinimal {
    private long outfitId;
    private long userId; // Accept userId directly instead of needing whole object
    private String outfitName;
    private LocalDateTime creationDate;
    private boolean favorite;
    private List<Long> outfitItems = new ArrayList<>();


    public long getOutfitId() {
        return outfitId;
    }

    public long getUserId() {
        return userId;
    }

    public String getOutfitName() {
        return outfitName;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public boolean getFavorite() {
        return favorite;
    }

    public List<Long> getOutfitItems() {
        return outfitItems;
    }
}
