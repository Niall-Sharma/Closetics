package closetics.Clothes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ClothingRepository extends JpaRepository<Clothing, Long> {

    @Query(value = "SELECT * FROM users_table WHERE clothesId=?",nativeQuery = true)
    Clothing findById(int clothesId);

    @Query(value = "DELETE FROM users_table WHERE clothesId=?",nativeQuery = true)
    void deleteByClothesId(int clothesId);

    @Query(value = "SELECT * FROM users_table WHERE type=?",nativeQuery = true)
    List<Clothing> findByType(String type);

    @Query(value = "SELECT * FROM users_table WHERE special_type=?",nativeQuery = true)
    List<Clothing> findBySpecialType(String specialType);
}
