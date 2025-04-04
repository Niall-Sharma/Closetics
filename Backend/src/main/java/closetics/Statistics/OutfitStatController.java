package closetics.Statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class OutfitStatController {
    @Autowired
    OutfitStatRepository outfitStatRepository;

    @GetMapping(path = "/getOutfitStats/{id}")
    public Optional<OutfitStats> getClothingStats(@PathVariable long id) {
        return outfitStatRepository.findById(id);
    }

    @PutMapping(path = "/wornOutfitToday/{id}")
    public ResponseEntity<OutfitStats> addWornOutfitToday(@PathVariable long id) {
        try {
            OutfitStats outfitStats = outfitStatRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("OutfitStats Item not found"));
            outfitStats.incrementTimesWorn();
            WornRecord record = WeatherFetcher.fetchWeatherData(LocalDate.now());
            outfitStats.addWornRecord(record);
            calculateAvgTemps(outfitStats);
            outfitStatRepository.save(outfitStats);
            return ResponseEntity.ok(outfitStats);
        } catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private void calculateAvgTemps(OutfitStats outfitStats) {
        List<WornRecord> validRecords = outfitStats.getDatesWorn().stream()
                .filter(record -> record.getHighTemp() != -1000 && record.getLowTemp() != -1000)
                .collect(Collectors.toList());

        // Prevent division by zero
        if (validRecords.isEmpty()) {
            outfitStats.setAvgHighTemp(-1000f);
            outfitStats.setAvgLowTemp(-1000f);
            return;
        }

        // Calculate new averages
        double avgHighTemp = validRecords.stream().mapToDouble(WornRecord::getHighTemp).average().orElse(0);
        double avgLowTemp = validRecords.stream().mapToDouble(WornRecord::getLowTemp).average().orElse(0);

        // Set updated values
        outfitStats.setAvgHighTemp((float) avgHighTemp);
        outfitStats.setAvgLowTemp((float) avgLowTemp);
    }
}
