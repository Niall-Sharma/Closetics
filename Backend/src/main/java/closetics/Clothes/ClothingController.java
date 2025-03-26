package closetics.Clothes;
import java.util.List;
import java.util.Optional;

import closetics.Users.User;
import closetics.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import closetics.Clothes.Statistics.Stat;
import closetics.Clothes.Statistics.StatRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClothingController {
    @Autowired
    ClothingRepository clothingRepository;

    @Autowired
    StatRepository statRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping(path = "/clothing")
    public List<Clothing> getAllClothing() {
        return clothingRepository.findAll();
    }

    @GetMapping(path = "/clothing/{id}")
    public Optional<Clothing> getClothing(@PathVariable long id) {
        return clothingRepository.findById(id);
    }

    @GetMapping(path = "/clothing/special_type/{userId}/{type}")
    public List<Clothing> getClothingBySpecialType(@PathVariable("userId") long userId, @PathVariable("type") long type){
        return clothingRepository.findBySpecialType(userId, type);
    }

    @GetMapping(path = "/clothing/type/{userId}/{type}")
    public List<Clothing> getClothingByType(@PathVariable("userId") long userId, @PathVariable("type") long type){
        return clothingRepository.findByType(userId, type);
    }

    @GetMapping(path = "/clothing/user/{userId}")
    public List<Clothing> getClothingByUser(@PathVariable long userId){
      return clothingRepository.findByUserId(userId);
    }

    @PostMapping(path = "/clothing")
    public ResponseEntity<Clothing> createClothing(@RequestBody clothingMinimal request) {
        long user_id = request.getUserId();
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));

        Clothing clothing = new Clothing();
        clothing.setBrand(request.getBrand());
        clothing.setColor(request.getColor());
        clothing.setDateBought(request.getDateBought());
        clothing.setFavorite(request.getFavorite());
        clothing.setImagePath(request.getImagePath());
        clothing.setItemName(request.getItemName());
        clothing.setMaterial(request.getMaterial());
        clothing.setPrice(request.getPrice());
        clothing.setSize(request.getSize());
        clothing.setSpecialType(request.getSpecialType());
        clothing.setClothingType(request.getClothingType());
        clothing.setUser(user);

        Clothing savedClothing = clothingRepository.save(clothing);
        return ResponseEntity.ok(savedClothing);
    }

    @DeleteMapping(path = "/clothing/{itemId}")
    public void deleteClothing(@PathVariable long itemId) {
        clothingRepository.deleteById(itemId);
    }

    @PutMapping (path = "/updateClothing")
    public ResponseEntity<Clothing> updateClothing(@RequestBody clothingMinimal request){
        try {
            Clothing clothing = clothingRepository.findById(request.getClothingId())
                    .orElseThrow(() -> new RuntimeException("Clothing Item not found"));
            clothing.setBrand(request.getBrand());
            clothing.setColor(request.getColor());
            clothing.setDateBought(request.getDateBought());
            clothing.setFavorite(request.getFavorite());
            clothing.setImagePath(request.getImagePath());
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}

