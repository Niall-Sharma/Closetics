package closetics.Clothes.ClothingTypes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface TypeRepository extends JpaRepository<ClothingType, Long> {
  @Query(value = "SELECT * FROM types WHERE id = ?", nativeQuery = true)
  ClothingType findByTypeId(long typeId);

}
