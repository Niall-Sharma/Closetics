package closetics.Clothes.Statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

 
@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {

    @Query(value = "SELECT * FROM stats_table WHERE clothes_id=?",nativeQuery = true)
    Stat findByClothesId(long clothesId);

    @Query(value = "DELETE FROM stats_table WHERE clothes_Id=?",nativeQuery = true)
    void deleteByClothesId(long clothesId);
}
