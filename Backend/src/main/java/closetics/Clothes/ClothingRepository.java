package closetics.Clothes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ClothingRepository extends JpaRepository<Clothing, Long> {
    Clothing findById(int id);

    @Transactional
    void deleteById(int id);

    @Query(value = "SELECT * FROM users_table WHERE type=?",nativeQuery = true)
    Clothing findByType(String username);
}
