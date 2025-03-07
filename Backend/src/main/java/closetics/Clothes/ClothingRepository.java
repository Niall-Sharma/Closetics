package closetics.Clothes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ClothingRepository extends JpaRepository<Clothing, Long> {

    @Query(value = "SELECT * FROM clothes_table WHERE clothes_id=?",nativeQuery = true)
    Clothing findById(long clothesId);

    @Transactional
    @Query(value = "DELETE FROM clothes_table WHERE clothes_id=?",nativeQuery = true)
    void deleteByClothesId(long clothesId);

    @Query(value = "SELECT * FROM clothes_table WHERE user_id=? AND type=?",nativeQuery = true)
    List<Clothing> findByType(long user_id, long type);
    //CHANGE THIS SO USERS CAN'T SEE EACH OTHER
    @Query(value = "SELECT * FROM clothes_table WHERE special_type=? AND user_id=?",nativeQuery = true)
    List<Clothing> findBySpecialType(long user_id, long special_type);

    @Query(value = "SELECT * FROM clothes_table WHERE user_Id=?", nativeQuery = true)
    List<Clothing> findByUserId(long userId);
}
