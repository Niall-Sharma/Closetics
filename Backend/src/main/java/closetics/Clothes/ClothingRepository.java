package closetics.Clothes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ClothingRepository extends JpaRepository<Clothing, Long> {

    @Query(value = "SELECT * FROM clothes_table WHERE clothesId=?",nativeQuery = true)
    Clothing findById(int clothesId);

    @Query(value = "DELETE FROM clothes_table WHERE clothesId=?",nativeQuery = true)
    void deleteByClothesId(int clothesId);

    @Query(value = "SELECT * FROM clothes_table WHERE type=?",nativeQuery = true)
    List<Clothing> findByType(String type);

    @Query(value = "SELECT * FROM clothes_table WHERE special_type=?",nativeQuery = true)
    List<Clothing> findBySpecialType(String specialType);

    @Query(value = "SELECT * FROM clothes_table WHERE user_Id=?", nativeQuery = true)
    List<Clothing> findByUserId(long userId);

}
