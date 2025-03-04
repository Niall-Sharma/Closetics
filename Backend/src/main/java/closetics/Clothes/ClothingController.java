package closetics.Clothes;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClothingController {
    @Autowired
    ClothingRepository clothingRepository;

    @GetMapping(path = "/clothes")
    public List<Clothing> getAllClothing() {
        return clothingRepository.findAll();
    }

    @GetMapping(path = "/clothes/{id}")
    public Clothing getClothing(@PathVariable long id) {
        return clothingRepository.findById(id);
    }

    @GetMapping(path = "/clothes/special_type/{type}")
    public List<Clothing> getClothingBySpecialType(@PathVariable long type){
        return clothingRepository.findBySpecialType(type);
    }

    @GetMapping(path = "/clothing/type/{type}")
    public List<Clothing> getClothingByType(@PathVariable long type){
        return clothingRepository.findByType(type);
    }

    @GetMapping(path = "clothing/user/{userId}")
    public List<Clothing> getClothingByUser(@PathVariable long userId){
      return clothingRepository.findByUserId(userId);
    }

    @PostMapping(path = "/clothes")
    public Clothing saveClothing(@RequestBody Clothing clothing) {
        return clothingRepository.save(clothing);
    }

    @DeleteMapping(path = "/clothes/{itemId}")
    public void deleteClothing(@PathVariable long itemId) {
        clothingRepository.deleteByClothesId(itemId);
    }

    @PutMapping (path = "/clothes/")
    public void updateClothing(@RequestBody Clothing clothing){
        clothingRepository.save(clothing);
    }
}

