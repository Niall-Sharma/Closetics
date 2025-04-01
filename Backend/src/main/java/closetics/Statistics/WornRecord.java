package closetics.Statistics;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class WornRecord {
    private LocalDateTime dateWorn;
    private float highTemp;
    private float lowTemp;

    // Constructors
    public WornRecord() {}

    public WornRecord(LocalDateTime dateWorn, float highTemp, float lowTemp) {
        this.dateWorn = dateWorn;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
    }

    // Getters and Setters
    public LocalDateTime getDateWorn() {
        return dateWorn;
    }

    public void setDateWorn(LocalDateTime dateWorn) {
        this.dateWorn = dateWorn;
    }

    public float getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(float highTemp) {
        this.highTemp = highTemp;
    }

    public float getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(float lowTemp) {
        this.lowTemp = lowTemp;
    }
}
