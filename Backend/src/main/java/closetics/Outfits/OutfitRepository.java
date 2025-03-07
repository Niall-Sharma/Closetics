package closetics.Outfits;


import closetics.Clothes.Clothing;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutfitRepository extends JpaRepository<Outfit, Long> {
    @Query(value = "SELECT * FROM outfit_table WHERE user_id = :userId ",nativeQuery = true)
    List<Outfit> findAllByUserId(@Param("userId") long userId);

}
