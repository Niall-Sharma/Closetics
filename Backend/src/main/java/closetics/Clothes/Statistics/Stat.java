package closetics.Clothes.Statistics;

import java.sql.Date;
import java.util.ArrayList;

import jakarta.persistence.*;

@Entity(name = "stats_table")
public class Stat {
  ArrayList<Date> dateWorn;
  long timesWorn;
  long highTemp;
  long lowTemp;


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long id;

  public Stat(long timesWorn, long highTemp, long lowTemp){
    this.timesWorn = timesWorn;
    this.dateWorn = new ArrayList<>();
    this.highTemp = highTemp;
    this.lowTemp = lowTemp;
  }
  
  public Stat(){
    dateWorn = new ArrayList<>();
    timesWorn = 0;
    highTemp = 0;
    lowTemp = 0;
  }

  public long getLowTemp(){
    return lowTemp;
  }

  public void setLowTemp(long temp){
    lowTemp = temp;
  }

  public long getHighTemp(){
    return highTemp;
  }

  public void setHighTemp(long temp){
    highTemp = temp;
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

  public void addLastWorn(Date lastWorn){
    this.dateWorn.add(lastWorn);
  }

  public ArrayList<Date> getLastWorn(){
    return dateWorn;
  }



}



