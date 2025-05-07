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
import closetics.Users.User;
import closetics.Users.UserRepository;

@RestController
@Tag(name = "Clothing Statistics", description = "Endpoints for managing and retrieving clothing statistics and related weather data")
public class ClothingStatController {

    @Autowired
    ClothingStatRepository clothingStatRepository;

    @Autowired
    OutfitRepository outfitRepository;

    @Autowired
    ClothingRepository clothingRepository;

    @Autowired
    UserRepository userRepository;

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
            @ApiResponse(responseCode = "403", description = "Forbidden for Free tier users", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping(path = "/getUsersMostExpensiveClothing/{userId}")
    public ResponseEntity<?> mostExpClothing(@Parameter(description = "ID of the user") @PathVariable long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        if ("Free".equalsIgnoreCase(user.getUserTier())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Clothing clothing = clothingRepository.findMostExpensiveClothingByUserId(userId)
                .orElse(null);
        return ResponseEntity.ok(clothing);
    }

    @Operation(summary = "Get the most worn clothing item for a user", description = "Finds the clothing item with the highest 'times worn' count for the specified user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the most worn clothing item (or null if none found)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clothing.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden for Free tier users", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping(path = "/getUsersMostWornClothing/{userId}")
    public ResponseEntity<?> mostWornClothing(@Parameter(description = "ID of the user") @PathVariable long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        if ("Free".equalsIgnoreCase(user.getUserTier())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Clothing clothing = clothingRepository.findTopByUser_userIdOrderByClothingStats_timesWornDesc(userId)
                .orElse(null);
        return ResponseEntity.ok(clothing);
    }

    @Operation(summary = "Get the clothing item worn in the coldest average temperatures for a user", description = "Finds the clothing item with the lowest average low temperature based on worn records.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the 'coldest' clothing item (or null if none found)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clothing.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden for Free tier users", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping(path = "/getUsersColdestAvgClothing/{userId}")
    public ResponseEntity<?> coldestAvgClothing(@Parameter(description = "ID of the user") @PathVariable long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        if ("Free".equalsIgnoreCase(user.getUserTier())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Pageable pageable = PageRequest.of(0, 1);
        List<Clothing> clothingList = clothingRepository.findTopByUserIdOrderByAvgLowTempAsc(userId, pageable);
        Clothing clothing = clothingList.isEmpty() ? null : clothingList.get(0);
        return ResponseEntity.ok(clothing);
    }

    @Operation(summary = "Get the clothing item worn in the warmest average temperatures for a user", description = "Finds the clothing item with the highest average high temperature based on worn records.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the 'warmest' clothing item (or null if none found)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clothing.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden for Free tier users", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping(path = "/getUsersWarmestAvgClothing/{userId}")
    public ResponseEntity<?> warmestAvgClothing(@Parameter(description = "ID of the user") @PathVariable long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        if ("Free".equalsIgnoreCase(user.getUserTier())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Pageable pageable = PageRequest.of(0, 1);
        List<Clothing> clothingList = clothingRepository.findTopByUserIdOrderByAvgHighTempDesc(userId, pageable);
        Clothing clothing = clothingList.isEmpty() ? null : clothingList.get(0);
        return ResponseEntity.ok(clothing);
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

    @Operation(summary = "Calculate Cost Per Wear for a clothing item", description = "Calculates the cost per wear (price / timesWorn) for a given clothing item on demand. Premium users only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated cost per wear (can be null if price is unavailable or timesWorn is 0)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Float.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: This feature is for Premium users only.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Clothing item or its statistics not found", content = @Content)
    })
    @GetMapping(path = "/getCostPerWear/{clothingId}")
    public ResponseEntity<Float> getCostPerWear(@Parameter(description = "ID of the clothing item") @PathVariable long clothingId) {
        Optional<Clothing> clothingOpt = clothingRepository.findById(clothingId);
        
        if (clothingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Clothing clothingItem = clothingOpt.get();
        User user = clothingItem.getUser();

        if (user == null) { // Should not happen if data integrity is maintained
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Or a more specific error
        }

        if ("Free".equalsIgnoreCase(user.getUserTier()) == false && "Basic".equalsIgnoreCase(user.getUserTier()) == false) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        Optional<ClothingStats> statsOpt = clothingStatRepository.findById(clothingId);
        if (statsOpt.isEmpty()) {
            // If stats are missing for a premium user's item, this might be a data issue or an item never worn.
            // Depending on desired behavior, could return 404 for stats or null for CPW.
            // For now, let's assume stats should exist if clothing exists and user is premium, 
            // so 404 for stats is consistent with "not found" meaning for stats part of the calculation.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
        }
        ClothingStats clothingStats = statsOpt.get();

        String priceString = clothingItem.getPrice();
        long timesWorn = clothingStats.getTimesWorn();
        Float priceValue = null;

        if (priceString != null && !priceString.isBlank()) {
            try {
                priceValue = Float.parseFloat(priceString);
            } catch (NumberFormatException e) {
                // Price is not a valid float, so costPerWear cannot be calculated.
                System.err.println("Error parsing price string for clothing ID " + clothingId + ": " + priceString);
                return ResponseEntity.ok(null);
            }
        }

        if (priceValue != null && timesWorn > 0) {
            return ResponseEntity.ok(priceValue / timesWorn);
        } else {
            // If price is null or timesWorn is 0, cost per wear is null.
            return ResponseEntity.ok(null);
        }
    }
}

