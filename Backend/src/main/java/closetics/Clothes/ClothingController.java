package closetics.Clothes;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import closetics.Clothes.ClothingImages.Image;
import closetics.Clothes.ClothingImages.ImageRepository;
import closetics.Statistics.ClothingStats;
import closetics.Users.User;
import closetics.Users.UserRepository;
import closetics.Outfits.Outfit;
import closetics.Outfits.OutfitRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import closetics.Statistics.ClothingStatRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Tag(name = "Clothing Items", description = "Endpoints for managing individual clothing items")
public class ClothingController {
    @Autowired
    ClothingRepository clothingRepository;

    @Autowired
    ClothingStatRepository clothingStatRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageRepository imageRepository;

    @Operation(summary = "Get all clothing items (globally)", description = "Retrieves a list of all clothing items across all users. Use with caution, potentially large response.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of all clothing",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))) // Specify Clothing in List
    })
    @GetMapping(path = "/getAllClothing")
    public List<Clothing> getAllClothing() {
        return clothingRepository.findAll();
    }

    @Operation(summary = "Get a clothing item by ID", description = "Retrieves a specific clothing item by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved clothing item",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clothing.class))),
            @ApiResponse(responseCode = "404", description = "Clothing item not found", content = @Content)
    })
    @GetMapping(path = "/getClothing/{id}")
    public Clothing getClothing(@Parameter(description = "ID of the clothing item to retrieve") @PathVariable long id) {
        return clothingRepository.findById(id).get();
    }

    @Operation(summary = "Get clothing items by special type for a user", description = "Retrieves clothing items of a specific 'special type' (e.g., seasonal) for a given user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of clothing items",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))) // Specify Clothing in List
    })
    @GetMapping(path = "/getClothing/special_type/{userId}/{type}")
    public List<Clothing> getClothingBySpecialType(
            @Parameter(description = "ID of the user") @PathVariable("userId") long userId,
            @Parameter(description = "Special type identifier (numeric)") @PathVariable("type") long type){
        return clothingRepository.findBySpecialType(userId, type);
    }

    @Operation(summary = "Get clothing items by clothing type for a user", description = "Retrieves clothing items of a specific clothing type (e.g., shirt, pants) for a given user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of clothing items",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))) // Specify Clothing in List
    })
    @GetMapping(path = "/getClothing/type/{userId}/{type}")
    public List<Clothing> getClothingByType(
            @Parameter(description = "ID of the user") @PathVariable("userId") long userId,
            @Parameter(description = "Clothing type identifier (numeric)") @PathVariable("type") long type){
        return clothingRepository.findByType(userId, type);
    }

    @Operation(summary = "Get all clothing items for a specific user", description = "Retrieves all clothing items belonging to a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of user's clothing items",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))) // Specify Clothing in List
    })
    @GetMapping(path = "/getClothing/user/{userId}")
    public List<Clothing> getClothingByUser(@Parameter(description = "ID of the user") @PathVariable long userId){
      return clothingRepository.findByUserId(userId);
    }

    @Operation(summary = "Create a new clothing item", description = "Adds a new clothing item for the specified user and initializes its statistics.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clothing item created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clothing.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PostMapping(path = "/createClothing")
    public ResponseEntity<Clothing> createClothing(
            @RequestBody(description = "Details of the clothing item to create. Requires userId and other clothing properties.", required = true,
                    content = @Content(schema = @Schema(implementation = ClothingMinimal.class)))
            @org.springframework.web.bind.annotation.RequestBody ClothingMinimal request) {
        long user_id = request.getUserId();
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        Clothing clothing = new Clothing();
        clothing.setBrand(request.getBrand());
        clothing.setColor(request.getColor());
        clothing.setDateBought(request.getDateBought());
        clothing.setFavorite(request.getFavorite());
        clothing.setItemName(request.getItemName());
        clothing.setMaterial(request.getMaterial());
        clothing.setPrice(request.getPrice());
        clothing.setSize(request.getSize());
        clothing.setSpecialType(request.getSpecialType());
        clothing.setClothingType(request.getClothingType());
        clothing.setCreationDate(LocalDate.now());
        clothing.setUser(user);

        Clothing savedClothing = clothingRepository.save(clothing);
        ClothingStats clothingStats = clothingStatRepository.save(new ClothingStats(savedClothing.getClothesId()));
        Clothing statClothing = savedClothing.setClothingStats(clothingStats);
        Clothing clothingWithStats = clothingRepository.save(statClothing);
        return ResponseEntity.ok(clothingWithStats);
    }

    @Operation(summary = "Delete a clothing item", description = "Deletes a clothing item and its associated statistics by its ID. Also removes the item from any outfits it is part of.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clothing item deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Clothing item or statistics not found (deletion might partially succeed)", content = @Content)
    })
    @DeleteMapping(path = "/deleteClothing/{itemId}")
    public ResponseEntity<Void> deleteClothing(@Parameter(description = "ID of the clothing item to delete") @PathVariable long itemId) {
        Optional<Clothing> clothingOptional = clothingRepository.findById(itemId);
        if (clothingOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Clothing clothingToDelete = clothingOptional.get();

        // Find all outfits containing this clothing item
        List<Long> outfitIds = outfitRepository.findOutfitIdsByClothingId(itemId);

        for (Long outfitId : outfitIds) {
            Optional<Outfit> outfitOptional = outfitRepository.findById(outfitId);
            if (outfitOptional.isPresent()) {
                Outfit outfit = outfitOptional.get();
                // Remove the specific clothing item from the outfit's list
                boolean removed = outfit.getOutfitItems().removeIf(item -> item.getClothesId() == itemId);
                if (removed) {
                    outfitRepository.save(outfit);
                }
            }
        }

        // Now delete the clothing item and its stats
        clothingRepository.deleteById(itemId);
        clothingStatRepository.deleteById(itemId); // Assuming clothingStatId is same as clothingId
        
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update an existing clothing item", description = "Updates the properties of an existing clothing item.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clothing item updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clothing.class))),
            @ApiResponse(responseCode = "404", description = "Clothing item not found", content = @Content)
    })
    @PutMapping (path = "/updateClothing")
    public ResponseEntity<Clothing> updateClothing(
            @RequestBody(description = "Details of the clothing item to update. Requires clothingId. Include other properties to modify them.", required = true,
                    content = @Content(schema = @Schema(implementation = ClothingMinimal.class)))
            @org.springframework.web.bind.annotation.RequestBody ClothingMinimal request){
        try {
            Clothing clothing = clothingRepository.findById(request.getClothingId())
                    .orElseThrow(() -> new RuntimeException("Clothing Item not found"));
            clothing.setBrand(request.getBrand());
            clothing.setColor(request.getColor());
            clothing.setDateBought(request.getDateBought());
            clothing.setFavorite(request.getFavorite());
            clothing.setItemName(request.getItemName());
            clothing.setMaterial(request.getMaterial());
            clothing.setPrice(request.getPrice());
            clothing.setSize(request.getSize());
            clothing.setSpecialType(request.getSpecialType());
            clothing.setClothingType(request.getClothingType());
            clothingRepository.save(clothing);
            return ResponseEntity.ok(clothing);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @Operation(summary = "Add an image to a clothing item")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Image succesfully saved"),
                    @ApiResponse(responseCode = "404", description = "Clothing not found"),

            }
    )
    @PutMapping("/addImage/{clothing_Id}")
    public ResponseEntity<Clothing> addImage(
            @Parameter(description = "ID of the clothing item to modify") @PathVariable long clothing_Id,
            @org.springframework.web.bind.annotation.RequestBody MultipartFile imageFile){
        try {
            Clothing clothing = clothingRepository.findById(clothing_Id).orElseThrow(() -> new RuntimeException("Clothing not found"));

            File destinationFile = new File("/images/"+File.separator + clothing_Id + "_" + clothing.getUser().getUserId() + "_" + imageFile.getOriginalFilename());
            if(!Files.exists(destinationFile.toPath().getParent())){
                Files.createDirectories(destinationFile.toPath().getParent());
            }

            try (InputStream is = imageFile.getInputStream()){
                Files.copy(is, destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            Image image = new Image();
            image.setFilePath(destinationFile.getAbsolutePath());
            imageRepository.save(image);

            clothing.setImage(image);
            clothingRepository.save(clothing);

            return ResponseEntity.ok().body(clothing);
        }catch (IOException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @Operation(summary = "Returns the clothing image")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Image returned"),
                    @ApiResponse(responseCode = "404", description = "Image not found")
            }
    )
    @GetMapping(value = "/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@Parameter(description = "ID of the clothing item") @PathVariable long id) throws IOException {
        Image image = imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));
        File imageFile = new File(image.getFilePath());
        return ResponseEntity.ok().body(Files.readAllBytes(imageFile.toPath()));
    }

    @Operation(summary = "Returns the clothing image")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Image returned"),
                    @ApiResponse(responseCode = "404", description = "Image not found")
            }
    )
    @GetMapping(value = "/clothingImages/{clothing_id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageByClothing(@Parameter(description = "ID of the clothing item") @PathVariable long clothing_id) throws IOException {
        Image image = clothingRepository.findById(clothing_id).orElseThrow(() -> new RuntimeException("User Not Found")).getImagePath();
        File imageFile = new File(image.getFilePath());
        return ResponseEntity.ok().body(Files.readAllBytes(imageFile.toPath()));
    }
    @Operation(summary = "Toggle the favorite status of a clothing item", description = "Sets a clothing item's favorite status to true if false, and vice versa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorite status toggled successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clothing.class))),
            @ApiResponse(responseCode = "404", description = "Clothing item not found", content = @Content)
    })
    @PutMapping("/swapFavoriteOnClothing/{clothing_Id}")
    public ResponseEntity<Clothing> swapFavorite(@Parameter(description = "ID of the clothing item to modify") @PathVariable long clothing_Id) {
        Optional<Clothing> clothingOptional = clothingRepository.findById(clothing_Id);

        if (clothingOptional.isPresent()) {
            Clothing clothing = clothingOptional.get();
            if (clothing.getFavorite()) {
                clothing.setFavorite(false);
                clothingRepository.save(clothing);
            } else {
                clothing.setFavorite(true);
                clothingRepository.save(clothing);
            }
            return ResponseEntity.ok(clothing);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}

