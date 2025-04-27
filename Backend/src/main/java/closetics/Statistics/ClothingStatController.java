package closetics.Statistics;

import closetics.Clothes.Clothing;
import closetics.Clothes.ClothingRepository;
import closetics.Outfits.OutfitRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Clothing Statistics", description = "Endpoints for managing and retrieving clothing statistics")
public class ClothingStatController {

    @Autowired
    ClothingStatRepository clothingStatRepository;

    @Autowired
    OutfitRepository outfitRepository;

    @Autowired
    ClothingRepository clothingRepository;

    @Operation(summary = "Get clothing statistics by ID", description = "Retrieves the statistics record for a specific clothing item.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved clothing statistics",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClothingStats.class))),
            @ApiResponse(responseCode = "404", description = "Clothing statistics not found for the given ID", content = @Content)
    })
    @GetMapping(path = "/getClothingStats/{id}")
    public ClothingStats getClothingStats(@Parameter(description = "ID of the clothing item statistics to retrieve") @PathVariable long id) {
        return clothingStatRepository.findById(id).orElseThrow(() -> new RuntimeException("ClothingStats Item not found"));
    }

    @Operation(summary = "Mark clothing item as worn today", description = "Increments the times worn count, adds a worn record with today's weather, and recalculates average temperatures.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated clothing statistics",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClothingStats.class))),
            @ApiResponse(responseCode = "404", description = "Clothing statistics not found for the given ID", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error fetching weather data", content = @Content)
    })
    @PutMapping(path = "/wornClothingToday/{id}")
    public ResponseEntity<ClothingStats> addWornClothingToday(@Parameter(description = "ID of the clothing item worn") @PathVariable long id) {
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

    @Operation(summary = "Calculate the number of outfits a clothing item is in", description = "Updates and returns the count of distinct outfits containing the specified clothing item.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated and updated the outfit count",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "404", description = "Clothing statistics not found for the given ID", content = @Content)
    })
    @PutMapping(path = "/numberOfOutfitsIn/{id}")
    public long calcNumberOfOutfitsIn(@Parameter(description = "ID of the clothing item") @PathVariable long id) {
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

    @Operation(summary = "Get the most expensive clothing item for a user", description = "Finds the clothing item with the highest price for the specified user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the most expensive clothing item (or null if none found)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clothing.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping(path = "/getUsersMostExpensiveClothing/{userId}")
    public Clothing mostExpClothing(@Parameter(description = "ID of the user") @PathVariable long userId) {
        return clothingRepository.findMostExpensiveClothingByUserId(userId)
                .orElse(null);
    }

    @Operation(summary = "Get the most worn clothing item for a user", description = "Finds the clothing item with the highest 'times worn' count for the specified user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the most worn clothing item (or null if none found)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clothing.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping(path = "/getUsersMostWornClothing/{userId}")
    public Clothing mostWornClothing(@Parameter(description = "ID of the user") @PathVariable long userId) {
        return clothingRepository.findTopByUser_userIdOrderByClothingStats_timesWornDesc(userId)
                .orElse(null);
    }

    @Operation(summary = "Get the clothing item worn in the coldest average temperatures for a user", description = "Finds the clothing item with the lowest average low temperature based on worn records.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the 'coldest' clothing item (or null if none found)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clothing.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping(path = "/getUsersColdestAvgClothing/{userId}")
    public Clothing coldestAvgClothing(@Parameter(description = "ID of the user") @PathVariable long userId) {
        return clothingRepository.findTopByUserIdOrderByAvgLowTempAsc(userId)
                .orElse(null);
    }

    @Operation(summary = "Get the clothing item worn in the warmest average temperatures for a user", description = "Finds the clothing item with the highest average high temperature based on worn records.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the 'warmest' clothing item (or null if none found)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clothing.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping(path = "/getUsersWarmestAvgClothing/{userId}")
    public Clothing warmestAvgClothing(@Parameter(description = "ID of the user") @PathVariable long userId) {
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

