package closetics.Clothes.Statistics;

import java.sql.Date;


import jakarta.persistence.*;

@Entity(name = "stats_table")
public class Stat {
  Date lastWorn;
  long timesWorn;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long id;

  public Stat(long timesWorn, Date lastWorn){
    this.timesWorn = timesWorn;
    this.lastWorn = lastWorn;

  }
  
  public Stat(){
    lastWorn = null;
    timesWorn = 0;
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



