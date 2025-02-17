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
    public List<Clothing> getAllUsers() {
        return clothingRepository.findAll();
    }

    @GetMapping(path = "/clothes/{id}")
    public Clothing getUser(@PathVariable int id) {
        return clothingRepository.findById(id);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation")
    @GetMapping(path = "/clothing/type/{id}")
    public Clothing getUserByUsername(@PathVariable String id){
        return clothingRepository.findByType(id);
    }

    @PostMapping(path = "/clothes")
    public Clothing signUp(@RequestBody Clothing user) {
        return clothingRepository.save(user);
    }

    @DeleteMapping(path = "/clothes/{id}")
    public void deleteUser(@PathVariable int id) {
        clothingRepository.deleteById(id);
    }

}
