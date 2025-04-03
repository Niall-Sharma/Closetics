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
    long outfitStatsId;

    @ElementCollection
    @CollectionTable(name = "outfit_days_worn", joinColumns = @JoinColumn(name = "outfit_stats_id"))
    private List<WornRecord> datesWorn;

    long timesWorn;

    public OutfitStats(long outfitStatsId){
        this.outfitStatsId = outfitStatsId;
        datesWorn = new ArrayList<WornRecord>();
        timesWorn = 0;
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
}




