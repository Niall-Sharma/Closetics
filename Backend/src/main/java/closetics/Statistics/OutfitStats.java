package closetics.Statistics;

import java.sql.Date;
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

    public OutfitStats(long timesWorn, float highTemp, float lowTemp){
        this.timesWorn = timesWorn;
        this.datesWorn = new ArrayList<>();

    }

    public OutfitStats(long outfitStatsId){
        this.outfitStatsId = outfitStatsId;
        datesWorn = new ArrayList<>();
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

}




