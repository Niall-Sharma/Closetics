package closetics.Clothes;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClothingController {
    @Autowired
    ClothingRepository clothingRepository;

    @GetMapping(path = "/clothes")
    public List<Clothing> getAllClothing() {
        return clothingRepository.findAll();
    }

    @GetMapping(path = "/clothes/{id}}")
    public Clothing getClothing(@PathVariable int id) {
        return clothingRepository.findById(id);
    }

    @GetMapping(path = "/clothes/special_type/{type}")
    public List<Clothing> getClothingBySpecialType(@PathVariable String type){
        return clothingRepository.findBySpecialType(type);
    }

    @GetMapping(path = "/clothing/type/{type}")
    public List<Clothing> getClothingByType(@PathVariable String type){
        return clothingRepository.findByType(type);
    }

    @PostMapping(path = "/clothes")
    public Clothing saveClothing(@RequestBody Clothing clothing) {
        return clothingRepository.save(clothing);
    }

    @DeleteMapping(path = "/clothes/{id}")
    public void deleteClothing(@PathVariable int id) {
        clothingRepository.deleteById(id);
    }

}
