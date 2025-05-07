package closetics.Outfits;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
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

    @Query(value = """
    SELECT o.outfit_id AS outfitId, SUM(CAST(c.price AS DOUBLE)) AS totalPrice
    FROM outfit_table o
    JOIN outfit_items oi ON o.outfit_id = oi.outfit_id
    JOIN clothing_table c ON c.clothes_id = oi.clothing_id
    WHERE o.user_id = :userId
    GROUP BY o.outfit_id
    ORDER BY totalPrice DESC
    LIMIT 1
    """, nativeQuery = true)
    Map<String, Object> findMostExpensiveOutfitByUserId(@Param("userId") long userId);

    Optional<Outfit> findTopByUser_userIdOrderByOutfitStats_timesWornDesc(long userId);

    @Query("SELECT c FROM outfit_table c " +
            "JOIN c.outfitStats cs " +
            "WHERE c.user.userId = :userId " +
            "AND cs.avgLowTemp > -1000 " +
            "ORDER BY cs.avgLowTemp ASC")
    List<Outfit> findTopByUserIdOrderByAvgLowTempAsc(@Param("userId") long userId, Pageable pageable);

    @Query("SELECT c FROM outfit_table c " +
            "JOIN c.outfitStats cs " +
            "WHERE c.user.userId = :userId " +
            "AND cs.avgHighTemp > -1000 " +
            "ORDER BY cs.avgHighTemp DESC")
    List<Outfit> findTopByUserIdOrderByAvgHighTempDesc(@Param("userId") long userId, Pageable pageable);

    long countByUserUserId(long userId);

    @Query(value = "SELECT DISTINCT oi.outfit_id FROM outfit_items oi WHERE oi.clothing_id = :clothingId", nativeQuery = true)
    List<Long> findOutfitIdsByClothingId(@Param("clothingId") Long clothingId);
}
