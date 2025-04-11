package closetics.Statistics;

import closetics.Clothes.Clothing;
import closetics.Outfits.Outfit;
import closetics.Outfits.OutfitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class OutfitStatController {
    @Autowired
    OutfitStatRepository outfitStatRepository;

    @Autowired
    OutfitRepository outfitRepository;

    @Autowired
    ClothingStatRepository clothingStatRepository;


    @GetMapping(path = "/getOutfitStats/{id}")
    public OutfitStats getClothingStats(@PathVariable long id) {
        return outfitStatRepository.findById(id).get();
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

            // Add worn record to clothing items in outfit as well
            Outfit outfit = outfitRepository.findById(id).orElseThrow(() -> new RuntimeException("Outfit not found"));
            List<Clothing> clothes = outfit.getOutfitItems();
            for (Clothing clothingItem : clothes) {
                ClothingStats clothingItemStats = clothingStatRepository.findById(clothingItem.getClothesId())
                        .orElseThrow(() -> new RuntimeException("Clothing item not found"));
                clothingItemStats.incrementTimesWorn();
                clothingItemStats.addWornRecord(record);
                calculateAvgTemps(clothingItemStats);
                clothingStatRepository.save(clothingItemStats);
            }
            return ResponseEntity.ok(outfitStats);
        } catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(path = "/getUsersMostExpensiveOutfit/{userId}")
    public Map mostExpOutfit(@PathVariable long userId) {
        return outfitRepository.findMostExpensiveOutfitByUserId(userId);
    }

    @GetMapping(path = "/getUsersMostWornOutfit/{userId}")
    public Outfit mostWornOutfit(@PathVariable long userId) {
        return outfitRepository.findTopByUser_userIdOrderByOutfitStats_timesWornDesc(userId)
                .orElse(null);
    }

    @GetMapping(path = "/getUsersColdestAvgOutfit/{userId}")
    public Outfit coldestAvgOutfit(@PathVariable long userId) {
        Pageable pageable = PageRequest.of(0, 1);
        List<Outfit> outfits = outfitRepository.findTopByUserIdOrderByAvgLowTempAsc(userId, pageable);
        return outfits.isEmpty() ? null : outfits.get(0);
    }

    @GetMapping(path = "/getUsersWarmestAvgOutfit/{userId}")
    public Outfit warmestAvgOutfit(@PathVariable long userId) {
        Pageable pageable = PageRequest.of(0, 1);
        List<Outfit> outfits = outfitRepository.findTopByUserIdOrderByAvgHighTempDesc(userId, pageable);
        return outfits.isEmpty() ? null : outfits.get(0);

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
