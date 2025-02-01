package coms309.DatabaseTables;
import org.springframework.data.jpa.repository.JpaRepository;
import coms309.Entities.User;
import java.util.UUID;

public interface UserTable extends JpaRepository<User, UUID> {
}
