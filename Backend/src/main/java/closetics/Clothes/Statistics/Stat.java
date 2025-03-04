package closetics.Clothes.Statistics;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import closetics.Clothes.Clothing;
import jakarta.persistence.*;

@Entity(name = "stats_table")
public class Stat {
  Date lastWorn;
  int timesWorn;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  @OneToOne
  @JsonIgnore
  @JoinColumn(name = "clothes_id")
  Clothing clothesId;

  public Stat(Clothing clothesId, int timesWorn, Date lastWorn){
    this.timesWorn = timesWorn;
    this.lastWorn = lastWorn;

    this.clothesId = clothesId;
  }

}



