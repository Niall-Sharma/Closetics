package org.springframework.samples.petclinic.pets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class PetController {

    @Autowired
    PetRepository petRepository;

    @Autowired
    OwnerRepository ownerRepository;

    private final Logger logger = LoggerFactory.getLogger(PetController.class);

    @RequestMapping(method = RequestMethod.POST, path = "/pets/create")
    public String savePet(){

        return "Pet Successfully Added to Registry";
    }

    @RequestMapping(method =  RequestMethod.GET, path = "/pets/{petID}")
    public Optional<Pet> getPet(@PathVariable("petID") int id) {
        logger.info("Entered into Controller Layer");
        Optional<Pet> results = petRepository.findById(id);
        return results;
    }

    @RequestMapping(method =  RequestMethod.GET, path = "/pets/{petID}")
    public String getPetOwner(@PathVariable("petID") int id) {
        logger.info("Entered into Controller Layer");
        Optional<Pet> results = petRepository.findById(id);
        return results.get().getOwnerName();
    }


    @RequestMapping(method = RequestMethod.GET, path = "/pets")
    public List<Pet> getPets() {
        return petRepository.findAll();
    }



}
