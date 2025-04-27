package closetics.Statistics;

import closetics.Clothes.Clothing;
import closetics.Outfits.Outfit;
import closetics.Outfits.OutfitRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Outfit Statistics", description = "Endpoints for managing and retrieving outfit statistics")
public class OutfitStatController {
    @Autowired
    OutfitStatRepository outfitStatRepository;

    @Autowired
    OutfitRepository outfitRepository;

    @Autowired
    ClothingStatRepository clothingStatRepository;


    @Operation(summary = "Get outfit statistics by ID", description = "Retrieves the statistics record for a specific outfit.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved outfit statistics",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OutfitStats.class))),
            @ApiResponse(responseCode = "404", description = "Outfit statistics not found for the given ID", content = @Content)
    })
    @GetMapping(path = "/getOutfitStats/{id}")
    public OutfitStats getClothingStats(@Parameter(description = "ID of the outfit statistics to retrieve") @PathVariable long id) {
        return outfitStatRepository.findById(id).orElseThrow(() -> new RuntimeException("OutfitStats Item not found"));
    }

    @Operation(summary = "Mark outfit as worn today", description = "Increments the times worn count for the outfit and all its clothing items, adds a worn record with today's weather to each, and recalculates average temperatures.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated outfit and associated clothing statistics",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OutfitStats.class))),
            @ApiResponse(responseCode = "404", description = "Outfit statistics, Outfit, or associated Clothing statistics not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error fetching weather data", content = @Content)
    })
    @PutMapping(path = "/wornOutfitToday/{id}")
    public ResponseEntity<OutfitStats> addWornOutfitToday(@Parameter(description = "ID of the outfit worn") @PathVariable long id) {
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

    @Operation(summary = "Get the most expensive outfit for a user", description = "Finds the outfit with the highest total price of its items for the specified user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the most expensive outfit details (Map might contain outfit ID and total price)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "User not found or no outfits found for the user", content = @Content)
    })
    @GetMapping(path = "/getUsersMostExpensiveOutfit/{userId}")
    public Map mostExpOutfit(@Parameter(description = "ID of the user") @PathVariable long userId) {
        return outfitRepository.findMostExpensiveOutfitByUserId(userId);
    }

    @Operation(summary = "Get the most worn outfit for a user", description = "Finds the outfit with the highest 'times worn' count for the specified user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the most worn outfit (or null if none found)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Outfit.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping(path = "/getUsersMostWornOutfit/{userId}")
    public Outfit mostWornOutfit(@Parameter(description = "ID of the user") @PathVariable long userId) {
        return outfitRepository.findTopByUser_userIdOrderByOutfitStats_timesWornDesc(userId)
                .orElse(null);
    }

    @Operation(summary = "Get the outfit worn in the coldest average temperatures for a user", description = "Finds the outfit with the lowest average low temperature based on worn records.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the 'coldest' outfit (or null if none found)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Outfit.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping(path = "/getUsersColdestAvgOutfit/{userId}")
    public Outfit coldestAvgOutfit(@Parameter(description = "ID of the user") @PathVariable long userId) {
        Pageable pageable = PageRequest.of(0, 1);
        List<Outfit> outfits = outfitRepository.findTopByUserIdOrderByAvgLowTempAsc(userId, pageable);
        return outfits.isEmpty() ? null : outfits.get(0);
    }

    @Operation(summary = "Get the outfit worn in the warmest average temperatures for a user", description = "Finds the outfit with the highest average high temperature based on worn records.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the 'warmest' outfit (or null if none found)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Outfit.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping(path = "/getUsersWarmestAvgOutfit/{userId}")
    public Outfit warmestAvgOutfit(@Parameter(description = "ID of the user") @PathVariable long userId) {
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
