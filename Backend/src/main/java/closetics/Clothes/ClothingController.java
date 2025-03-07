package closetics.Clothes;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import closetics.Clothes.Statistics.Stat;
import closetics.Clothes.Statistics.StatRepository;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClothingController {
    @Autowired
    ClothingRepository clothingRepository;

    @Autowired
    StatRepository statRepository;

    @GetMapping(path = "/clothes")
    public List<Clothing> getAllClothing() {
        return clothingRepository.findAll();
    }

    @GetMapping(path = "/clothes/{id}")
    public Optional<Clothing> getClothing(@PathVariable long id) {
        return clothingRepository.findById(id);
    }

    @GetMapping(path = "/clothes/special_type/{userId}/{type}")
    public List<Clothing> getClothingBySpecialType(@PathVariable("userId") long userId, @PathVariable("type") long type){
        return clothingRepository.findBySpecialType(userId, type);
    }

    @GetMapping(path = "/clothes/type/{userId}/{type}")
    public List<Clothing> getClothingByType(@PathVariable("userId") long userId, @PathVariable("type") long type){
        return clothingRepository.findByType(userId, type);
    }

    @GetMapping(path = "/clothes/user/{userId}")
    public List<Clothing> getClothingByUser(@PathVariable long userId){
      return clothingRepository.findByUserId(userId);
    }

    @GetMapping(path = "/clothing/stats/{id}")
    public Stat getClothingStat(@PathVariable long id){
      return statRepository.findByClothesId(id);
    }

    @PutMapping(path = "/clothing/stats/{id}")
    public void updateStat(@RequestBody Stat stat, @PathVariable long id){
      statRepository.save(stat);
    }

    @PostMapping(path = "/clothes")
    public Clothing saveClothing(@RequestBody Clothing clothing) {
        clothing.setStat(new Stat());
        statRepository.save(clothing.getStat());
        return clothingRepository.save(clothing);
    }

    @DeleteMapping(path = "/clothes/{itemId}")
    public void deleteClothing(@PathVariable long itemId) {
        clothingRepository.deleteById(itemId);
    }

    @PutMapping (path = "/clothes/")
    public void updateClothing(@RequestBody Clothing clothing){
        clothingRepository.save(clothing);
    }

    @PutMapping("/swapFavoriteOnClothing/{clothing_Id}")
    public ResponseEntity<Clothing> swapFavorite(@PathVariable long clothing_Id) {
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
            return ResponseEntity.notFound().build();
        }
    }


}

