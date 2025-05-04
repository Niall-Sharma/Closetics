package closetics.Clothes.ClothingImages;

import closetics.Clothes.Clothing;
import closetics.Outfits.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query(value = "SELECT * FROM image_table WHERE user_id = :userId ",nativeQuery = true)
    Optional<Image> findByUserId(@Param("userId") long userId);
}
