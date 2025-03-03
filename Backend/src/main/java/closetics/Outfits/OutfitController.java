package closetics.Outfits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class OutfitController {

    @Autowired
    OutfitRepository outfitRepo;


    @GetMapping(path = "/outfit/{outfitId}")
    public Optional<Outfit> getAllOutfitItems(@PathVariable long outfitId) {
        return outfitRepo.findById(outfitId);
    }

    @PostMapping(path = "/createOutfit")
    public Outfit saveClothing(@RequestBody Outfit outfit) {
        if (outfit.getOutfitItems() == null) {
            outfit.setOutfitItems(new ArrayList<>()); // Ensure the list is initialized
        }
        return outfitRepo.save(outfit);
    }

    @PutMapping(path = "/updateOutfit")
    public Outfit updateClothing(@RequestBody Outfit outfit){
        return outfitRepo.save(outfit);
    }

    @DeleteMapping(path = "/outfit/{outfitId}")
    public void deleteClothing(@PathVariable long outfitId) {
        outfitRepo.deleteById(outfitId);
    }



}
