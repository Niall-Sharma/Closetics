package closetics.Clothes.Statistics;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
  @JoinColumn(name = "clothesId")
  int clothesId;

  public Stat(int timesWorn, Date lastWorn){
    this.timesWorn = timesWorn;
    this.lastWorn = lastWorn;
  }

}



