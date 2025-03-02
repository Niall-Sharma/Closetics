package closetics.Outfits;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutfitRepository extends JpaRepository<Outfit, Long> {

    @Query(value = "SELECT * FROM outfit_table WHERE outfit_id=?",nativeQuery = true)
    Outfit findOutfitItemsById(int outfitId);

    @Query(value = "DELETE FROM outfit_table WHERE outfit_id=?",nativeQuery = true)
    void deleteByOutfitId(int outfitId);

    @Query(value = "DELETE FROM outfit_table WHERE outfit_id=?",nativeQuery = true)
    void deleteByOufitId(int outfitId);
}
