package closetics.Statistics;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity(name = "clothing_stats_table")
public class ClothingStats {

    @Id
    long clothingStatsId;

    @ElementCollection
    @CollectionTable(name = "clothing_item_days_worn", joinColumns = @JoinColumn(name = "clothing_stats_id"))
    @Column(name = "date_worn")
    private List<WornRecord> datesWorn;

    long timesWorn;
    long numberOfOutfitsIn;


    public ClothingStats(long timesWorn, long numberOfOutfitsIn) {
        this.timesWorn = timesWorn;
        this.datesWorn = new ArrayList<>();
        this.numberOfOutfitsIn = numberOfOutfitsIn;

    }

    public ClothingStats(long clothingStatsId) {
        this.clothingStatsId = clothingStatsId;
        datesWorn = new ArrayList<>();
        timesWorn = 0;
        numberOfOutfitsIn = 0;
    }

    public ClothingStats(){}

    public long getTimesWorn() {
        return timesWorn;
    }

    public void setTimesWorn(long timesWorn) {
        this.timesWorn = timesWorn;
    }

    public void incrementTimesWorn() {
        timesWorn += 1;
    }

    public void decrementTimesWorn() {
        timesWorn -= 1;
    }

    public long numberOfOutfitsIn() {
        return numberOfOutfitsIn;
    }

    public void setNumberOfOutfitsIn(long numberOfOutfitsIn) {
        this.numberOfOutfitsIn = numberOfOutfitsIn;
    }
}



