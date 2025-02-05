package org.springframework.samples.petclinic.pets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PetController {

    @Autowired
    PetRepository petRepository;



    private final Logger logger = LoggerFactory.getLogger(PetController.class);

    @RequestMapping(method = RequestMethod.POST, path = "/pets/create")
    public String createDummyData() {
        Pet p = new Pet(1, "Bailey","Terrier", "Brindle", "3", new owner());
        petRepository.save(p);
        return "Successfully created dummy data";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/pets/add")
    public String addPet(Pet pet) {
        petRepository.save(pet);
        return "Successfully saved pet: " + pet.getPetName();
    }

    @RequestMapping(method =  RequestMethod.GET, path = "/pets/{petID}")
    public Optional<Pet> getPet(@PathVariable("petID") int id) {
        logger.info("Entered into Controller Layer");
        Optional<Pet> results = petRepository.findById(id);
        return results;
    }

//    @GetMapping(path = "/pets/{petID}")
//    public owner getOwner(@PathVariable("petID") int id) {
//        logger.info("Entered into Controller Layer");
//        Optional<Pet> results = petRepository.findById(id);
//        return results.get().getOwner();
//    }


    @GetMapping(path = "/pets")
    public List<Pet> getPets() {
        return petRepository.findAll();
    }



}
