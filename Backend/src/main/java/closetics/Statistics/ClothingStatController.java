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
public class ClothingStatController {

    @Autowired
    ClothingStatRepository clothingStatRepository;

    @GetMapping(path = "/getClothingStats/{id}")
    public Optional<ClothingStats> getClothingStats(@PathVariable long id) {
        return clothingStatRepository.findById(id);
    }

    @PutMapping(path = "/wornClothingToday/{id}")
    public ResponseEntity<ClothingStats> addWornClothingToday(@PathVariable long id) {
        try {
            ClothingStats clothingStats = clothingStatRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("ClothingStats Item not found"));
            clothingStats.incrementTimesWorn();
            WornRecord record = WeatherFetcher.fetchWeatherData(LocalDate.now());
            clothingStats.addWornRecord(record);
            clothingStatRepository.save(clothingStats);
            return ResponseEntity.ok(clothingStats);
        } catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
