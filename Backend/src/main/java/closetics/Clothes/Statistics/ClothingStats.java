package closetics.Clothes.Statistics;

import java.sql.Date;
import java.util.ArrayList;

import jakarta.persistence.*;

@Entity(name = "clothing_stats_table")
public class ClothingStats {
  ArrayList<Date> dateWorn;
  long timesWorn;
  float highTemp;
  float lowTemp;
  long numberOfOutfitsIn;


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long clothingStatsId;

  public ClothingStats(long timesWorn, float highTemp, float lowTemp, long numberOfOutfitsIn){
    this.timesWorn = timesWorn;
    this.dateWorn = new ArrayList<>();
    this.highTemp = highTemp;
    this.lowTemp = lowTemp;
    this.numberOfOutfitsIn =numberOfOutfitsIn;

  }
  
  public ClothingStats(){
    dateWorn = new ArrayList<>();
    timesWorn = 0;
    highTemp = 0;
    lowTemp = 0;
    numberOfOutfitsIn = 0;
  }

  public float getLowTemp(){
    return lowTemp;
  }

  public void setLowTemp(float temp){
    lowTemp = temp;
  }

  public float getHighTemp(){
    return highTemp;
  }

  public void setHighTemp(float temp){
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

  public long numberOfOutfitsIn() {
    return numberOfOutfitsIn;
  }

  public void setNumberOfOutfitsIn(long numberOfOutfitsIn) {
    this.numberOfOutfitsIn = numberOfOutfitsIn;
  }
}



