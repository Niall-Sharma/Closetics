package coms309.Service;

import coms309.EntityCreation.ClothingItem;
import coms309.Repository.ClothingItemRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClothingItemService {

    @Autowired
    private ClothingItemRepository clothingItemRepo;

    public ClothingItem postInfo(ClothingItem clothingItem) {
        return clothingItemRepo.save(clothingItem);
    }

    public ClothingItem getInfoByItemId(Integer itemId) {
        return clothingItemRepo.findById(itemId).orElse(null);
    }

    public ClothingItem updateClothingItemInfo(ClothingItem clothingItem) {
        ClothingItem updateInfo = clothingItemRepo.findById(clothingItem.getItemId()).orElse(null);
        if(updateInfo!=null){
            updateInfo.setItemId(clothingItem.getItemId());
            updateInfo.setBrand(clothingItem.getBrand());
            updateInfo.setColor(clothingItem.getColor());
            updateInfo.setDateBought(clothingItem.getDateBought());
            updateInfo.setPrice(clothingItem.getPrice());
            updateInfo.setItemName(clothingItem.getItemName());
            updateInfo.setClothingCategory(clothingItem.getClothingCategory());
            updateInfo.setClothingType(clothingItem.getClothingType());
            updateInfo.setIsFavorite(clothingItem.getIsFavorite());
            updateInfo.setImagePath1(clothingItem.getImagePath1());
            updateInfo.setImagePath2(clothingItem.getImagePath2());
            updateInfo.setImagePath3(clothingItem.getImagePath3());
            clothingItemRepo.save(updateInfo);
            return updateInfo;
        }
        return null;
    }
    @Transactional
    public void deleteClothingItemInfo(Integer itemId){
        clothingItemRepo.deleteClothingItemByItemId(itemId);
    }

}
