package closetics.Outfits;


import closetics.Clothes.Clothing;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface OutfitRepository extends JpaRepository<Outfit, Long> {
    @Query(value = "SELECT * FROM outfit_table WHERE user_id = :userId ",nativeQuery = true)
    List<Outfit> findAllByUserId(@Param("userId") long userId);

    @Query(value = "SELECT COUNT(DISTINCT outfit_id) FROM outfit_items WHERE clothing_id = :clothingId", nativeQuery = true)
    long countDistinctOutfitsContainingClothing(@Param("clothingId") Long clothingId);

    @Query(value = """
    SELECT o.outfit_id AS outfitId, SUM(CAST(c.price AS DOUBLE)) AS totalPrice
    FROM outfit_table o
    JOIN outfit_items oi ON o.outfit_id = oi.outfit_id
    JOIN clothing_table c ON c.clothes_id = oi.clothing_id
    GROUP BY o.outfit_id
    ORDER BY totalPrice DESC
    LIMIT 10
    """, nativeQuery = true)
    List<Map<String, Object>> findTop10MostExpensiveOutfits();

    Optional<Outfit> findById(Long id);
}
