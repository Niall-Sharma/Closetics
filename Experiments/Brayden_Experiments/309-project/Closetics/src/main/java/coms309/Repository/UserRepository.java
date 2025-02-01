package coms309.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import coms309.EntityCreation.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username); // Username query method
}
