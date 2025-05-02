package closetics.Outfits;

import closetics.Clothes.Clothing;
import closetics.Clothes.ClothingRepository;
import closetics.Statistics.*;
import closetics.Users.User;
import closetics.Users.UserProfile.UserProfileDTO;
import closetics.Users.UserRepository;
import closetics.Users.UserProfile.UserProfile;
import closetics.Users.UserProfile.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
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

    @GetMapping(path = "/getOutfit/{outfitId}")
    public ResponseEntity<Outfit> getOutfit(@PathVariable long outfitId) {
        return outfitRepository.findById(outfitId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping(path = "/getAllUserOutfits/{userId}")
    public List<Outfit> getAllUserOutfits(@PathVariable long userId) {
        return outfitRepository.findAllByUserId(userId);
    }

    @PostMapping(path = "/createOutfit")
    public ResponseEntity<Outfit> createOutfit(@RequestBody OutfitMinimal request) {
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

    @PutMapping(path = "/updateOutfit")
    public ResponseEntity<Outfit> updateOutfit(@RequestBody OutfitMinimal request) {
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

    @DeleteMapping(path = "/deleteOutfit/{outfitId}")
    public void deleteOutfit(@PathVariable long outfitId) {

        Outfit outfit = outfitRepository.findById(outfitId).orElseThrow( () -> new RuntimeException("Failed To Find Outfit"));
        UserProfile userProfile = outfit.getUser().GetUserProfile();

        userProfile.removeOutfit(outfit);

        outfitRepository.deleteById(outfitId);
        outfitStatRepository.deleteById(outfitId);
    }

    @PutMapping("/addItemToOutfit/{outfitId}/{clothingId}")
    public ResponseEntity<Outfit> addItemToOutfit(@PathVariable long outfitId, @PathVariable long clothingId) {
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

    @PutMapping("/removeItemFromOutfit/{outfitId}/{clothingId}")
    public ResponseEntity<Outfit> removeItemFromOutfit(@PathVariable long outfitId, @PathVariable long clothingId) {
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

    @GetMapping(path = "/getAllOutfitItems/{outfitId}")
    public List<Clothing> getAllOutfitItems(@PathVariable long outfitId) {
        Outfit outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new RuntimeException("Outfit Item not found"));
        List<Clothing> clothes = new ArrayList<>();
        if (outfit != null) {
           clothes = outfit.getOutfitItems();
        }
        return clothes;
    }

    @PutMapping("/swapFavoriteOnOutfit/{outfitId}")
    public ResponseEntity<Outfit> swapFavorite(@PathVariable long outfitId) {
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

    @PutMapping("/addLike/{outfitId}/{userId}")
    public ResponseEntity<Outfit> addLike(@PathVariable long outfitId, @PathVariable long userId){
        Outfit outfit = outfitRepository.findById(outfitId).orElseThrow(() -> new RuntimeException("Outfit Not Found"));
        UserProfile userProfile = uProfileRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Profile Not Found"));

        outfit.addUserProfileLike(userProfile);

        outfitRepository.save(outfit);
        return ResponseEntity.ok(outfit);
    }

    @PutMapping("/removeLike/{outfitId}/{userId}")
    public ResponseEntity<Outfit> removeLike(@PathVariable long outfitId, @PathVariable long userId){
        Outfit outfit = outfitRepository.findById(outfitId).orElseThrow(() -> new RuntimeException("Outfit Not Found"));
        UserProfile userProfile = uProfileRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Profile Not Found"));

        outfit.removeUserProfileLike(userProfile);

        outfitRepository.save(outfit);
        return ResponseEntity.ok(outfit);
    }

    @GetMapping("/likedOutfit/{outfitId}/{userId}")
    public ResponseEntity<Boolean> checkLike(@PathVariable("outfitId") long outfitId, @PathVariable("userId") long userId){
        Outfit outfit = outfitRepository.findById(outfitId).orElseThrow(() -> new RuntimeException("Outfit Not Found"));
        for(UserProfileDTO user : outfit.getUserProfileLikes()){
            if(user.getId() == userId){
                return ResponseEntity.ok().body(true);
            }
        }
        return ResponseEntity.ok().body(false);
    }

}
