package closetics.Statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

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
            outfitStatRepository.save(outfitStats);
            return ResponseEntity.ok(outfitStats);
        } catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
