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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Clothing Statistics", description = "Endpoints for managing and retrieving clothing statistics and related weather data")
public class ClothingStatController {

    @Autowired
    ClothingStatRepository clothingStatRepository;

    @Autowired
    OutfitRepository outfitRepository;

    @Autowired
    ClothingRepository clothingRepository;

    @GetMapping(path = "/getClothingStats/{id}")
    public ClothingStats getClothingStats(@PathVariable long id) {
        return clothingStatRepository.findById(id).get();
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
        return clothingRepository.findMostExpensiveClothingByUserId(userId)
                .orElse(null);
    }

    @GetMapping(path = "/getUsersMostWornClothing/{userId}")
    public Clothing mostWornClothing(@PathVariable long userId) {
        return clothingRepository.findTopByUser_userIdOrderByClothingStats_timesWornDesc(userId)
                .orElse(null);
    }

    @GetMapping(path = "/getUsersColdestAvgClothing/{userId}")
    public Clothing coldestAvgClothing(@PathVariable long userId) {
        Pageable pageable = PageRequest.of(0, 1);
        List<Clothing> results = clothingRepository.findTopByUserIdOrderByAvgLowTempAsc(userId, pageable);
        return results.isEmpty() ? null : results.get(0);
    }

    @GetMapping(path = "/getUsersWarmestAvgClothing/{userId}")
    public Clothing warmestAvgClothing(@PathVariable long userId) {
        Pageable pageable = PageRequest.of(0, 1);
        List<Clothing> results = clothingRepository.findTopByUserIdOrderByAvgHighTempDesc(userId, pageable);
        return results.isEmpty() ? null : results.get(0);
    }

    @Operation(summary = "Get today's high and low temperature forecast", description = "Fetches the current day's forecast high and low temperatures using the configured location.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved today's forecast temperatures",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "object"))),
            @ApiResponse(responseCode = "503", description = "Service Unavailable: Could not fetch weather data", content = @Content)
    })
    @GetMapping(path = "/todaysWeather")
    public ResponseEntity<Map<String, Float>> getTodaysWeather() {
        WornRecord todayRecord = WeatherFetcher.fetchWeatherData(LocalDate.now());
        float highTemp = todayRecord.getHighTemp();
        float lowTemp = todayRecord.getLowTemp();

        // Check if the fetcher returned error values
        if (highTemp == -1000f || lowTemp == -1000f) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }

        Map<String, Float> temps = new HashMap<>();
        temps.put("highTemp", highTemp);
        temps.put("lowTemp", lowTemp);
        return ResponseEntity.ok(temps);
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

