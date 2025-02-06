package coms309.Controller;

import coms309.EntityCreation.ClothingItem;
import coms309.Service.ClothingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClothingItemController {

    @Autowired
    private ClothingItemService clothingItemService;

    @PostMapping("/addClothingItem")
    public ClothingItem postInfo(@RequestBody ClothingItem clothingItem) {
        return clothingItemService.postInfo(clothingItem);
    }

    @GetMapping("/getClothingItem/{itemId}")
    public ClothingItem getInfoByItemId(@PathVariable Integer itemId) {
        return clothingItemService.getInfoByItemId(itemId);
    }

    @PutMapping("/updateClothingItem")
    public ClothingItem updateClothingItemInfo(@RequestBody ClothingItem clothingItem) {
        return clothingItemService.updateClothingItemInfo(clothingItem);
    }

    @DeleteMapping("/deleteClothingItem/{itemId}")
    public void deleteClothingItemInfo(@PathVariable Integer itemId){
        clothingItemService.deleteClothingItemInfo(itemId);
    }

}
