package closetics.Statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

 
@Repository
public interface ClothingStatRepository extends JpaRepository<ClothingStats, Long> {

}
