package closetics.Outfits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OutfitController {

    @Autowired
    OutfitRepository outfitRepo;


    @GetMapping(path = "/outfit/{id}")
    public Outfit getAllOutfitItems(@PathVariable int outfitId) {
        return outfitRepo.findOutfitItemsById(outfitId);
    }

    @PostMapping(path = "/createOutfit")
    public Outfit saveClothing(@RequestBody Outfit outfit) {
            return outfitRepo.save(outfit);
    }

    @PutMapping(path = "/updateOutfit")
    public Outfit updateClothing(@RequestBody Outfit outfit){
        return outfitRepo.save(outfit);
    }

    @DeleteMapping(path = "/outfit/{outfitId}")
    public void deleteClothing(@PathVariable int outfitId) {
        outfitRepo.deleteByOufitId(outfitId);
    }



}
