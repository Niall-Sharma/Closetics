package coms309.Repository;

import coms309.EntityCreation.ClothingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;

public interface ClothingItemRepository extends JpaRepository<ClothingItem, Integer> {

    void deleteClothingItemByItemId(Integer itemId);

}
