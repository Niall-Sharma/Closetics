package closetics.Clothes.Statistics;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import closetics.Clothes.Clothing;
import jakarta.persistence.*;

@Entity(name = "stats_table")
public class Stat {
  Date lastWorn;
  long timesWorn;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  @OneToOne
  @JsonIgnore
  @JoinColumn(name = "clothes_id")
  Clothing clothesId;

  public Stat(Clothing clothesId, long timesWorn, Date lastWorn){
    this.timesWorn = timesWorn;
    this.lastWorn = lastWorn;

    this.clothesId = clothesId;
  }

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

  public void setLastWorn(Date lastWorn){
    this.lastWorn = lastWorn;
  }

  public Date getLastWorn(){
    return lastWorn;
  }



}



