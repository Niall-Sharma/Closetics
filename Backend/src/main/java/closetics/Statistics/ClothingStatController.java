package closetics.Statistics;

import closetics.Outfits.OutfitRepository;
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

    @Autowired
    OutfitRepository outfitRepository;

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

    @PutMapping(path = "/numberOfOutfitsIn/{id}")
    public long calcNumberOfOutfitsIn(@PathVariable long id){
        try {
            ClothingStats clothingStats = clothingStatRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("ClothingItem Item not found"));
            long numOfOutfitsIn = outfitRepository.countDistinctOutfitsContainingClothing(id);
            clothingStats.setNumberOfOutfitsIn(numOfOutfitsIn);
            clothingStatRepository.save(clothingStats);
            return numOfOutfitsIn;
        } catch(RuntimeException e) {
            return 0;
        }
    }
}
