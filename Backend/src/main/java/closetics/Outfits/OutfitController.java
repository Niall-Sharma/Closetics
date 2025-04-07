package closetics.Outfits;

import closetics.Clothes.Clothing;
import closetics.Clothes.ClothingRepository;
import closetics.Statistics.OutfitStatRepository;
import closetics.Statistics.OutfitStats;
import closetics.Users.User;
import closetics.Users.UserRepository;
import closetics.Users.UserProfile.UserProfile;
import closetics.Users.UserProfile.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    UserProfileRepository uProfileRepository;

    @GetMapping(path = "/getOutfit/{outfitId}")
    public Outfit getOutfit(@PathVariable long outfitId) {
        return outfitRepository.findById(outfitId).get();
    }

    @GetMapping(path = "/getAllUserOutfits/{userId}")
    public List<Outfit> getAllUserOutfits(@PathVariable long userId) {
        return outfitRepository.findAllByUserId(userId);
    }

    @PostMapping(path = "/createOutfit")
    public ResponseEntity<Outfit> createOutfit(@RequestBody OutfitMinimal request) {
        long user_id = request.getUserId();
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
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
        uProfile.addOutfit(savedOutfit);
        uProfileRepository.save(uProfile);
        OutfitStats outfitStats = outfitStatRepository.save(new OutfitStats(savedOutfit.getOutfitId()));
        Outfit statOutfit = savedOutfit.setOutfitStats(outfitStats);
        Outfit outfitWithStats =  outfitRepository.save(statOutfit);
        return ResponseEntity.ok(outfitWithStats);
    }

    @PutMapping(path = "/updateOutfit")
    public ResponseEntity<Outfit> updateClothing(@RequestBody OutfitMinimal request){
        try {
            Outfit outfit = outfitRepository.findById(request.getOutfitId())
                    .orElseThrow(() -> new RuntimeException("Outfit Item not found"));
            outfit.setOutfitItems(request.getOutfitItems());
            if (outfit.getOutfitItems() == null) {
                outfit.setOutfitItems(new ArrayList<>()); // Ensure the list is initialized
            }
            outfit.setOutfitName(request.getOutfitName());
            outfit.setFavorite(request.getFavorite());
            outfitRepository.save(outfit);
            return ResponseEntity.ok(outfit);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping(path = "/deleteOutfit/{outfitId}")
    public void deleteClothing(@PathVariable long outfitId) {
        outfitRepository.deleteById(outfitId);
        outfitStatRepository.deleteById(outfitId);
    }

    @PutMapping(path = "/addItemToOutfit/{outfitId}")
    public void addItemToOutfit(@PathVariable long outfitId) {

    }

    @PutMapping("/addItemToOutfit/{outfitId}/{clothingId}")
    public ResponseEntity<Outfit> addItemToOutfit(@PathVariable long outfitId, @PathVariable long clothingId) {
        Optional<Outfit> outfitOptional = outfitRepository.findById(outfitId);

        if (outfitOptional.isPresent()) {
            Outfit outfit = outfitOptional.get();
            if (!outfit.getOutfitItems().contains(clothingId)) { // Prevent duplicate entries
                Clothing clothing = clothingRepository.findById(clothingId).get();
                outfit.getOutfitItems().add(clothing);
                outfitRepository.save(outfit);
            }
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
            if (outfit.getOutfitItems().contains(clothingId)) { // Check if it exists before removing
                Clothing clothing = clothingRepository.findById(clothingId).get();
                outfit.getOutfitItems().remove(clothing);
                outfitRepository.save(outfit);
            }
            return ResponseEntity.ok(outfit);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(path = "/getAllOutfitItems/{outfitId}")
    public ResponseEntity<List<Optional<Clothing>>> getAllOutfitItems(@PathVariable long outfitId) {
        Optional<Outfit> outfitOptional = outfitRepository.findById(outfitId);
        List<Optional<Clothing>> clothes = new ArrayList<>();
        if (outfitOptional.isPresent()) {
            Outfit outfit = outfitOptional.get();
            List<Clothing> items = outfit.getOutfitItems();
        }
        return ResponseEntity.ok(clothes);
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

}
