package coms309.clothing;

import coms309.people.Person;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class ClothingController {

    HashMap<String, Clothing> clothingList = new  HashMap<>();

    @GetMapping("/clothing")
    public  HashMap<String,Clothing> getAllPersons() {
        return clothingList;
    }

    @PostMapping("/clothing")
    public String createClothing(@RequestBody Clothing clothing) {
        clothingList.put(clothing.getName(), clothing);
        return "New clothing article "+ clothing.getName() + " has been saved by server";
    }

    @GetMapping("/people/{name}")
    public Clothing getClothing(@PathVariable String name) {
        return clothingList.get(name);
    }

    @PutMapping("/people/{name}")
    public Clothing updateClothing(@PathVariable String name, @RequestBody Clothing c) {
        clothingList.replace(name, c);
        return clothingList.get(name);
    }

    @DeleteMapping("/people/{name}")
    public HashMap<String, Clothing> deletePerson(@PathVariable String name) {
        clothingList.remove(name);
        return clothingList;
    }
}
