package closetics.Outfits;

import closetics.Clothes.Clothing;
import closetics.Clothes.ClothingRepository;
import closetics.Statistics.*;
import closetics.Users.User;
import closetics.Users.UserRepository;
import closetics.Users.UserProfile.UserProfile;
import closetics.Users.UserProfile.UserProfileRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Outfits", description = "Endpoints for managing user outfits")
public class OutfitController {

    @Autowired
    OutfitRepository outfitRepository;

    @Autowired
    ClothingRepository clothingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OutfitStatRepository outfitStatRepository;

    @Autowired
    ClothingStatRepository clothingStatRepository;

    @Autowired
    UserProfileRepository uProfileRepository;

    @Operation(summary = "Get an outfit by ID", description = "Retrieves a specific outfit by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the outfit",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Outfit.class))),
            @ApiResponse(responseCode = "404", description = "Outfit not found for the given ID", content = @Content)
    })
    @GetMapping(path = "/getOutfit/{outfitId}")
    public ResponseEntity<Outfit> getOutfit(@Parameter(description = "ID of the outfit to retrieve") @PathVariable long outfitId) {
        return outfitRepository.findById(outfitId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Operation(summary = "Get all outfits for a user", description = "Retrieves a list of all outfits belonging to a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of outfits",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))) // Specify Outfit within List if possible
    })
    @GetMapping(path = "/getAllUserOutfits/{userId}")
    public List<Outfit> getAllUserOutfits(@Parameter(description = "ID of the user whose outfits to retrieve") @PathVariable long userId) {
        return outfitRepository.findAllByUserId(userId);
    }

    @Operation(summary = "Create a new outfit", description = "Creates a new outfit for a user, subject to user tier limits.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Outfit created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Outfit.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "User has reached the outfit limit for their tier", content = @Content)
    })
    @PostMapping(path = "/createOutfit")
    public ResponseEntity<Outfit> createOutfit(
            @RequestBody(description = "Details of the outfit to create. Requires userId, outfitName, favorite status. outfitItems are ignored here.", required = true,
                    content = @Content(schema = @Schema(implementation = OutfitMinimal.class)))
            @org.springframework.web.bind.annotation.RequestBody OutfitMinimal request) {
        long user_id = request.getUserId();
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        long numOfUserOutfits = outfitRepository.countByUserUserId(user_id);
        if ((user.getUserTier().equals("Free") && numOfUserOutfits >= 15) || (user.getUserTier().equals("Basic") && numOfUserOutfits >= 30)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Outfit outfit = new Outfit();
        outfit.setUser(user);
        outfit.setCreationDate(LocalDate.now());
        outfit.setOutfitName(request.getOutfitName());
        outfit.setFavorite(request.getFavorite());
        if (outfit.getOutfitItems() == null) {
            outfit.setOutfitItems(new ArrayList<>()); // Ensure the list is initialized
        }

        Outfit savedOutfit =  outfitRepository.save(outfit);
        UserProfile uProfile = outfit.getUser().GetUserProfile();
        OutfitStats outfitStats = outfitStatRepository.save(new OutfitStats(savedOutfit.getOutfitId()));
        Outfit statOutfit = savedOutfit.setOutfitStats(outfitStats);
        Outfit outfitWithStats =  outfitRepository.save(statOutfit);
        uProfile.addOutfit(outfitWithStats);
        uProfileRepository.save(uProfile);
        return ResponseEntity.ok(outfitWithStats);
    }

    @Operation(summary = "Update an existing outfit", description = "Updates the name, favorite status, and list of items for an existing outfit.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Outfit updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Outfit.class))),
            @ApiResponse(responseCode = "404", description = "Outfit or a specified clothing item not found", content = @Content)
    })
    @PutMapping(path = "/updateOutfit")
    public ResponseEntity<Outfit> updateOutfit(
            @RequestBody(description = "Details of the outfit to update. Requires outfitId. Include outfitName, favorite, and/or outfitItems (list of clothing IDs) to modify them.", required = true,
                    content = @Content(schema = @Schema(implementation = OutfitMinimal.class)))
            @org.springframework.web.bind.annotation.RequestBody OutfitMinimal request) {
        try {
            Outfit outfit = outfitRepository.findById(request.getOutfitId())
                    .orElseThrow(() -> new RuntimeException("Outfit Item not found"));

            List<Clothing> items = new ArrayList<>();
            if (request.getOutfitItems() != null) {
                for (Long id : request.getOutfitItems()) {
                    clothingRepository.findById(id).ifPresent(items::add);
                }
            }

            outfit.setOutfitItems(items);
            outfit.setOutfitName(request.getOutfitName());
            outfit.setFavorite(request.getFavorite());

            outfitRepository.save(outfit);
            return ResponseEntity.ok(outfit);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Delete an outfit", description = "Deletes an outfit and its associated statistics by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Outfit deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Outfit or associated User Profile not found", content = @Content)
    })
    @DeleteMapping(path = "/deleteOutfit/{outfitId}")
    public void deleteOutfit(@Parameter(description = "ID of the outfit to delete") @PathVariable long outfitId) {
        Outfit outfit = outfitRepository.findById(outfitId).orElseThrow( () -> new RuntimeException("Failed To Find Outfit"));
        UserProfile userProfile = outfit.getUser().GetUserProfile();

        userProfile.removeOutfit(outfit);

        outfitRepository.deleteById(outfitId);
        outfitStatRepository.deleteById(outfitId);
    }

    @Operation(summary = "Add a clothing item to an outfit", description = "Adds a specified clothing item to a specified outfit and updates the clothing item's outfit count.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Outfit.class))),
            @ApiResponse(responseCode = "404", description = "Outfit, Clothing item, or Clothing statistics not found", content = @Content)
    })
    @PutMapping("/addItemToOutfit/{outfitId}/{clothingId}")
    public ResponseEntity<Outfit> addItemToOutfit(
            @Parameter(description = "ID of the outfit to modify") @PathVariable long outfitId,
            @Parameter(description = "ID of the clothing item to add") @PathVariable long clothingId) {
        Optional<Outfit> outfitOptional = outfitRepository.findById(outfitId);

        if (outfitOptional.isPresent()) {
            Outfit outfit = outfitOptional.get();
            Clothing clothing = clothingRepository.findById(clothingId).orElseThrow(() -> new RuntimeException("Clothing Not Found"));
            if (!outfit.getOutfitItems().contains(clothing)) { // Prevent duplicate entries
                outfit.getOutfitItems().add(clothing);
                outfitRepository.save(outfit);
            }

            ClothingStats clothingStats = clothingStatRepository.findById(clothingId)
                    .orElseThrow(() -> new RuntimeException("Clothing Stat Item not found"));
            long numOfOutfitsIn = outfitRepository.countDistinctOutfitsContainingClothing(clothingId);
            clothingStats.setNumberOfOutfitsIn(numOfOutfitsIn);
            clothingStatRepository.save(clothingStats);
            return ResponseEntity.ok(outfit);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Remove a clothing item from an outfit", description = "Removes a specified clothing item from a specified outfit and updates the clothing item's outfit count.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item removed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Outfit.class))),
            @ApiResponse(responseCode = "404", description = "Outfit, Clothing item, or Clothing statistics not found", content = @Content)
    })
    @PutMapping("/removeItemFromOutfit/{outfitId}/{clothingId}")
    public ResponseEntity<Outfit> removeItemFromOutfit(
            @Parameter(description = "ID of the outfit to modify") @PathVariable long outfitId,
            @Parameter(description = "ID of the clothing item to remove") @PathVariable long clothingId) {
        Optional<Outfit> outfitOptional = outfitRepository.findById(outfitId);

        if (outfitOptional.isPresent()) {
            Outfit outfit = outfitOptional.get();
            Clothing clothing = clothingRepository.findById(clothingId).orElseThrow(() -> new RuntimeException("Clothing Not Found"));
            if (outfit.getOutfitItems().contains(clothing)) { // Check if it exists before removing
                outfit.getOutfitItems().remove(clothing);
                outfitRepository.save(outfit);
            }

            ClothingStats clothingStats = clothingStatRepository.findById(clothingId)
                    .orElseThrow(() -> new RuntimeException("Clothing Stat Item not found"));
            long numOfOutfitsIn = outfitRepository.countDistinctOutfitsContainingClothing(clothingId);
            clothingStats.setNumberOfOutfitsIn(numOfOutfitsIn);
            clothingStatRepository.save(clothingStats);
            return ResponseEntity.ok(outfit);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Get all clothing items in an outfit", description = "Retrieves a list of all clothing items belonging to a specific outfit.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of clothing items",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))), // Specify Clothing within List if possible
            @ApiResponse(responseCode = "404", description = "Outfit not found", content = @Content)
    })
    @GetMapping(path = "/getAllOutfitItems/{outfitId}")
    public List<Clothing> getAllOutfitItems(@Parameter(description = "ID of the outfit whose items to retrieve") @PathVariable long outfitId) {
        Outfit outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new RuntimeException("Outfit Item not found"));
        List<Clothing> clothes = new ArrayList<>();
        if (outfit != null) {
           clothes = outfit.getOutfitItems();
        }
        return clothes;
    }

    @Operation(summary = "Toggle the favorite status of an outfit", description = "Sets an outfit's favorite status to true if false, and vice versa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorite status toggled successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Outfit.class))),
            @ApiResponse(responseCode = "404", description = "Outfit not found", content = @Content)
    })
    @PutMapping("/swapFavoriteOnOutfit/{outfitId}")
    public ResponseEntity<Outfit> swapFavorite(@Parameter(description = "ID of the outfit to modify") @PathVariable long outfitId) {
        Optional<Outfit> outfitOptional = outfitRepository.findById(outfitId);

        if (outfitOptional.isPresent()) {
            Outfit outfit = outfitOptional.get();
            if (outfit.getFavorite()) {
                outfit.setFavorite(false);
                outfitRepository.save(outfit);
            } else {
                outfit.setFavorite(true);
                outfitRepository.save(outfit);
            }
            return ResponseEntity.ok(outfit);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
