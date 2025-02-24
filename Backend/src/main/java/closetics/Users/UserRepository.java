package closetics.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id);

    @Transactional
    void deleteById(long id);

    @Query(value = "SELECT * FROM users_table WHERE username=?",nativeQuery = true)
    User findByUsername(String username);

    @Query(value = "SELECT * FROM users_table WHERE email= :email",nativeQuery = true)
    User findByEmail(String email);
}
