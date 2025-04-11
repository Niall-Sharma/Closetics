package closetics.Statistics;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity(name = "clothing_stats_table")
public class ClothingStats {

    @Id
    private long clothingStatsId;

    @ElementCollection
    @CollectionTable(name = "clothing_item_days_worn", joinColumns = @JoinColumn(name = "clothing_stats_id"))
    @Column(name = "date_worn")
    private List<WornRecord> datesWorn;

    private long timesWorn;
    private long numberOfOutfitsIn;
    private Float avgHighTemp;
    private Float avgLowTemp;


    public ClothingStats(long timesWorn, long numberOfOutfitsIn) {
        this.timesWorn = timesWorn;
        this.datesWorn = new ArrayList<WornRecord>();
        this.numberOfOutfitsIn = numberOfOutfitsIn;

    }

    public ClothingStats(long clothingStatsId) {
        this.clothingStatsId = clothingStatsId;
        datesWorn = new ArrayList<WornRecord>();
        timesWorn = 0;
        numberOfOutfitsIn = 0;
        avgHighTemp = -1000f;
        avgLowTemp = -1000f;
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

    public List<WornRecord> getDatesWorn() {
        return datesWorn;
    }

    public void addWornRecord(WornRecord record) {
        this.datesWorn.add(record);
    }

    public Float getAvgLowTemp() {
        return avgLowTemp;
    }

    public void setAvgLowTemp(Float avgLowTemp) {
        this.avgLowTemp = avgLowTemp;
    }

    public Float getAvgHighTemp() {
        return avgHighTemp;
    }

    public void setAvgHighTemp(Float avgHighTemp) {
        this.avgHighTemp = avgHighTemp;
    }
}



