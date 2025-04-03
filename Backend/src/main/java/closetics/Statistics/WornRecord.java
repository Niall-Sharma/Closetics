package closetics.Statistics;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class WornRecord {
    private LocalDate dateWorn;
    private Float highTemp;
    private Float lowTemp;

    public WornRecord() {}

    public WornRecord(LocalDate dateWorn, Float highTemp, Float lowTemp) {
        this.dateWorn = dateWorn;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
    }

    // Getters and Setters
    public LocalDate getDateWorn() {
        return dateWorn;
    }

    public void setDateWorn(LocalDate dateWorn) {
        this.dateWorn = dateWorn;
    }

    public float getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(Float highTemp) {
        this.highTemp = highTemp;
    }

    public float getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(Float lowTemp) {
        this.lowTemp = lowTemp;
    }
}
