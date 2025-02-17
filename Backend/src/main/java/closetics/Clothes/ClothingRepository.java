package closetics.Clothes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ClothingRepository extends JpaRepository<Clothing, Long> {
    Clothing findById(int id);

    @Transactional
    void deleteById(int id);

    @Query(value = "SELECT * FROM users_table WHERE type=?",nativeQuery = true)
    List<Clothing> findByType(String username);

    @Query(value = "SELECT * FROM users_table WHERE special_type=?",nativeQuery = true)
    List<Clothing> findBySpecialType(String username);
}
