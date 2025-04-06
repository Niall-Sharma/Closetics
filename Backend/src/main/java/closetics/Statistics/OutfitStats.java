package closetics.Statistics;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity(name = "outfit_stats_table")
public class OutfitStats {

    @Id
    private long outfitStatsId;

    @ElementCollection
    @CollectionTable(name = "outfit_days_worn", joinColumns = @JoinColumn(name = "outfit_stats_id"))
    private List<WornRecord> datesWorn;

    private long timesWorn;
    private Float avgHighTemp;
    private Float avgLowTemp;

    public OutfitStats(long outfitStatsId){
        this.outfitStatsId = outfitStatsId;
        datesWorn = new ArrayList<WornRecord>();
        timesWorn = 0;
        avgLowTemp = -1000f;
        avgHighTemp = -1000f;
    }

    public OutfitStats(){}

    public long getTimesWorn(){
        return timesWorn;
    }

    public void setTimesWorn(long timesWorn){
        this.timesWorn = timesWorn;
    }

    public void incrementTimesWorn(){
        timesWorn+=1;
    }

    public void decrementTimesWorn(){
        timesWorn-=1;
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




