package closetics.Outfits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController

public class OutfitController {

    @Autowired
    OutfitRepository outfitRepo;

    @GetMapping(path = "/getOutfit/{outfitId}")
    public Optional<Outfit> getOutfit(@PathVariable long outfitId) {
        return outfitRepo.findById(outfitId);
    }

    @GetMapping(path = "/getAllUserOutfits/{userId}")
    public List<Outfit> getAllUserOutfits(@PathVariable long userId) {
        return outfitRepo.findAllByUserId(userId);
    }


    @PostMapping(path = "/createOutfit")
    public Outfit saveClothing(@RequestBody Outfit outfit) {
        if (outfit.getOutfitItems() == null) {
            outfit.setOutfitItems(new ArrayList<>()); // Ensure the list is initialized
        }
        outfit.setCreationDate(LocalDateTime.now());
        return outfitRepo.save(outfit);
    }

    @PutMapping(path = "/updateOutfit")
    public Outfit updateClothing(@RequestBody Outfit outfit){
        return outfitRepo.save(outfit);
    }

    @DeleteMapping(path = "/deleteOutfit/{outfitId}")
    public void deleteClothing(@PathVariable long outfitId) {
        outfitRepo.deleteById(outfitId);
    }

    @PutMapping(path = "/addItemToOutfit/{outfitId}")
    public void addItemToOutfit(@PathVariable long outfitId) {

    }

    @PutMapping("/addItemToOutfit/{outfitId}/{clothingId}")
    public ResponseEntity<Outfit> addItemToOutfit(@PathVariable long outfitId, @PathVariable long clothingId) {
        Optional<Outfit> outfitOptional = outfitRepo.findById(outfitId);

        if (outfitOptional.isPresent()) {
            Outfit outfit = outfitOptional.get();
            if (!outfit.getOutfitItems().contains(clothingId)) { // Prevent duplicate entries
                outfit.getOutfitItems().add(clothingId);
                outfitRepo.save(outfit);
            }
            return ResponseEntity.ok(outfit);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/removeItemFromOutfit/{outfitId}/{clothingId}")
    public ResponseEntity<Outfit> removeItemFromOutfit(@PathVariable long outfitId, @PathVariable long clothingId) {
        Optional<Outfit> outfitOptional = outfitRepo.findById(outfitId);

        if (outfitOptional.isPresent()) {
            Outfit outfit = outfitOptional.get();
            if (outfit.getOutfitItems().contains(clothingId)) { // Check if it exists before removing
                outfit.getOutfitItems().remove(clothingId);
                outfitRepo.save(outfit);
            }
            return ResponseEntity.ok(outfit);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
