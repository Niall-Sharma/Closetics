package closetics.Clothes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
}
