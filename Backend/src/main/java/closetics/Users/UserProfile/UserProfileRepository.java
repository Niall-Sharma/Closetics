package closetics.Users.UserProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findById(long id);

    @Transactional
    void deleteById(long id);

    @Query(value = "SELECT * FROM user_profiles_table WHERE username=?",nativeQuery = true)
    UserProfile findByUsername(String username);
    
    @Query(value = "SELECT * FROM user_profiles_table WHERE UID=?")
    UserProfile findByUID(long UID);
}
