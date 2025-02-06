package coms309.pets;

import coms309.people.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
public class PetController {

    HashMap<String, Pet> petsList = new  HashMap<>();


    @RequestMapping(method = RequestMethod.POST, path = "/pets/create")
    public String createDummyData() {
        Person owner = new Person("John", "Doe", "Lorem Ipsum", "555-555-5555");
        Pet p = new Pet(1, "Bailey","Terrier", "Brindle", "3", owner);
        petsList.put(p.getPetName(), p);
        return "Successfully created dummy data";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/pets/add")
    public String addPet(@RequestBody Pet pet) {
        petsList.put(pet.getPetName(), pet);
        return "Successfully saved pet: " + pet.getPetName();
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/pets/{petID}")
    public String updatePet(@PathVariable("petID") String id, @RequestBody Pet pet){
        petsList.put(id, pet);
        return "Successfully updated " + petsList.get(id).getPetName();
    }

    @RequestMapping(method =  RequestMethod.GET, path = "/pets/{petID}")
    public Pet getPet(@PathVariable("petID") String id) {
        return petsList.get(id);
    }

    @GetMapping(path = "/owners/{petID}")
    public Person getOwner(@PathVariable("petID") String id) {
        return petsList.get(id).getOwner();
    }


    @DeleteMapping("/pets/{firstName}")
    public HashMap<String, Pet> deletePet(@PathVariable String id) {
        petsList.remove(id);
        return petsList;
    }

    @GetMapping("/pets/")
    public HashMap<String, Pet> getPetsList(){
        return petsList;
    }


}
