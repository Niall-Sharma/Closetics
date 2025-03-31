package closetics.Outfits;

import closetics.Clothes.Clothing;
import closetics.Clothes.ClothingRepository;
import closetics.Users.UserRepository;
import closetics.Users.UserProfile.UserProfile;
import closetics.Users.UserProfile.UserProfileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    UserProfileRepository uProfileRepository;

    @GetMapping(path = "/getOutfit/{outfitId}")
    public Optional<Outfit> getOutfit(@PathVariable long outfitId) {
        return outfitRepository.findById(outfitId);
    }

    @GetMapping(path = "/getAllUserOutfits/{userId}")
    public List<Outfit> getAllUserOutfits(@PathVariable long userId) {
        return outfitRepository.findAllByUserId(userId);
    }

    @PostMapping(path = "/createOutfit")
    public Outfit saveClothing(@RequestBody Outfit outfit) {
      if (outfit.getOutfitItems() == null) {
          outfit.setOutfitItems(new ArrayList<>()); // Ensure the list is initialized
      }
      outfit.setCreationDate(LocalDateTime.now());
      UserProfile uProfile = uProfileRepository.findByUID(outfit.getUser().getUserId());
      uProfile.AddOutfit(outfit);
      return outfitRepository.save(outfit);
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
                outfit.getOutfitItems().add(clothingId);
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
                outfit.getOutfitItems().remove(clothingId);
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
            List<Long> items = outfit.getOutfitItems();
            for (Long itemId : items) {
                clothes.add(clothingRepository.findById(itemId));
            }
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
