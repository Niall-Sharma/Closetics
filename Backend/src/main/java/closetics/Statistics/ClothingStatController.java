package closetics.Statistics;

import closetics.Clothes.Clothing;
import closetics.Clothes.ClothingRepository;
import closetics.Outfits.OutfitRepository;
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
public class ClothingStatController {

    @Autowired
    ClothingStatRepository clothingStatRepository;

    @Autowired
    OutfitRepository outfitRepository;

    @Autowired
    ClothingRepository clothingRepository;

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
            calculateAvgTemps(clothingStats);
            clothingStatRepository.save(clothingStats);
            return ResponseEntity.ok(clothingStats);
        } catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping(path = "/numberOfOutfitsIn/{id}")
    public long calcNumberOfOutfitsIn(@PathVariable long id) {
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

    @GetMapping(path = "/getUsersMostExpensiveClothing/{userId}")
    public Clothing mostExpClothing(@PathVariable long userId) {
        return clothingRepository.findTopByUser_userIdOrderByPriceAsc(userId)
                .orElse(null);
    }

    @GetMapping(path = "/getUsersMostWornClothing/{userId}")
    public Clothing mostWornClothing(@PathVariable long userId) {
        return clothingRepository.findTopByUser_userIdOrderByClothingStats_timesWornDesc(userId)
                .orElse(null);
    }

    @GetMapping(path = "/getUsersColdestAvgClothing/{userId}")
    public Clothing coldestAvgClothing(@PathVariable long userId) {
        return clothingRepository.findTopByUserIdOrderByAvgLowTempAsc(userId)
                .orElse(null);
    }

    @GetMapping(path = "/getUsersWarmestAvgClothing/{userId}")
    public Clothing warmestAvgClothing(@PathVariable long userId) {
        return clothingRepository.findTopByUserIdOrderByAvgHighTempDesc(userId)
                .orElse(null);
    }


    private void calculateAvgTemps(ClothingStats clothingStats) {
        List<WornRecord> validRecords = clothingStats.getDatesWorn().stream()
                .filter(record -> record.getHighTemp() != -1000 && record.getLowTemp() != -1000)
                .collect(Collectors.toList());

        // Prevent division by zero
        if (validRecords.isEmpty()) {
            clothingStats.setAvgHighTemp(-1000f);
            clothingStats.setAvgLowTemp(-1000f);
            return;
        }

        // Calculate new averages
        double avgHighTemp = validRecords.stream().mapToDouble(WornRecord::getHighTemp).average().orElse(0);
        double avgLowTemp = validRecords.stream().mapToDouble(WornRecord::getLowTemp).average().orElse(0);

        // Set updated values
        clothingStats.setAvgHighTemp((float) avgHighTemp);
        clothingStats.setAvgLowTemp((float) avgLowTemp);
    }
}

