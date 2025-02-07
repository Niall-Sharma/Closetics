package coms309.EntityCreation;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(nullable = false, updatable = false, name = "user_id")
    private UUID userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Temporal(TemporalType.TIMESTAMP) // Ensures correct timestamp storage
    @Column(nullable = false, updatable = false, name = "user_created_date")
    private Date userCreatedDate = new Date();

}
