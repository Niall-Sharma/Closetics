package closetics.Users.Tokens;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByTokenValue(String tokenValue);

    @Transactional
    void deleteById(long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM tokens WHERE user_id = :userId", nativeQuery = true)
    void deleteByUserId(@Param("userId") long userId);
}
