package closetics.Clothes.ClothingTypes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface SpecialTypeRepository extends JpaRepository<SpecialType, Long> {
    @Query(value = "SELECT * FROM special_types WHERE id = ?", nativeQuery = true)
    SpecialType findBSpecialTypeId(long specialTypeId);
}
