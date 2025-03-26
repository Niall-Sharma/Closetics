package closetics.Clothes.Statistics;

import java.sql.Date;
import java.util.ArrayList;

import jakarta.persistence.*;

@Entity(name = "clothing_stats_table")
public class ClothingStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long clothingStatsId;

    @OneToOne
    @JoinColumn(name = "clothing_id", nullable = false)
    long clothingId;

    @ElementCollection
    @CollectionTable(name = "clothing_item_days_worn", joinColumns = @JoinColumn(name = "clothing_stats_id"))
    @Column(name = "clothing_stats_id")
    ArrayList<Date> datesWorn;

    long timesWorn;
    float highTemp;
    float lowTemp;
    long numberOfOutfitsIn;


    public ClothingStats(long timesWorn, float highTemp, float lowTemp, long numberOfOutfitsIn) {
        this.timesWorn = timesWorn;
        this.datesWorn = new ArrayList<>();
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
        this.numberOfOutfitsIn = numberOfOutfitsIn;

    }

    public ClothingStats() {
        datesWorn = new ArrayList<>();
        timesWorn = 0;
        highTemp = 0;
        lowTemp = 0;
        numberOfOutfitsIn = 0;
    }

    public float getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(float temp) {
        lowTemp = temp;
    }

    public float getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(float temp) {
        highTemp = temp;
    }

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

    public void addLastWorn(Date lastWorn) {
        this.datesWorn.add(lastWorn);
    }

    public ArrayList<Date> getLastWorn() {
        return datesWorn;
    }

    public long numberOfOutfitsIn() {
        return numberOfOutfitsIn;
    }

    public void setNumberOfOutfitsIn(long numberOfOutfitsIn) {
        this.numberOfOutfitsIn = numberOfOutfitsIn;
    }
}



