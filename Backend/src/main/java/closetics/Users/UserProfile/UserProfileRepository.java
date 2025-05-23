package closetics.Users.UserProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Transactional
    @Modifying
    void deleteById(long id);

    @Query(value = "SELECT * FROM user_profiles_table WHERE username=?",nativeQuery = true)
    UserProfile findByUsername(String username);

    @Modifying
    @Query(value = "DELETE FROM users_outfits WHERE UID = :userProfileId", nativeQuery = true)
    void deleteUserOutfitAssociations(@Param("userProfileId") long userProfileId);
    
}
