package closetics.Clothes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClothingRepository extends JpaRepository<Clothing, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM clothing_table WHERE clothes_id = :clothesId",nativeQuery = true)
    void deleteByClothesId(@Param("clothesId") long clothesId);

    @Query(value = "SELECT * FROM clothing_table WHERE user_id = :userId AND type = :type",nativeQuery = true)
    List<Clothing> findByType(@Param("userId") long user_id, @Param("type") long type);
    //CHANGE THIS SO USERS CAN'T SEE EACH OTHER
    @Query(value = "SELECT * FROM clothing_table WHERE special_type = :specialType AND user_id = :userId",nativeQuery = true)
    List<Clothing> findBySpecialType(@Param("userId") long user_id, @Param("specialType") long special_type);

    @Query(value = "SELECT * FROM clothing_table WHERE user_id = :userId ", nativeQuery = true)
    List<Clothing> findByUserId(@Param("userId") long userId);

    @Query("SELECT c FROM clothing_table c WHERE c.price IS NOT NULL ORDER BY CAST(c.price AS double) DESC, c.clothesId ASC")
    List<Clothing> findTop10MostValuable(Pageable pageable);

    @Query("SELECT c.user.id, COUNT(c) as totalItems " +
            "FROM clothing_table c " +
            "GROUP BY c.user.id " +
            "ORDER BY totalItems DESC")
    List<Object[]> findTop10UsersByClothingCount(Pageable pageable);

    Optional<Clothing> findTopByUser_userIdOrderByPriceAsc(long userId);

    Optional<Clothing> findTopByUser_userIdOrderByClothingStats_timesWornDesc(long userId);

    @Query("SELECT c FROM clothing_table c " +
            "JOIN c.clothingStats cs " +
            "WHERE c.user.userId = :userId " +
            "AND cs.avgLowTemp > -1000 " +
            "ORDER BY cs.avgLowTemp ASC")
    Optional<Clothing> findTopByUserIdOrderByAvgLowTempAsc(@Param("userId") Long userId);

    @Query("SELECT c FROM clothing_table c " +
            "JOIN c.clothingStats cs " +
            "WHERE c.user.userId = :userId " +
            "AND cs.avgHighTemp > -1000 " +
            "ORDER BY cs.avgHighTemp DESC")
    Optional<Clothing> findTopByUserIdOrderByAvgHighTempDesc(@Param("userId") Long userId);



}
