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
    @Getter
    @Column(nullable = false, updatable = false)
    private UUID user_id;

    @Getter
    @Column(nullable = false)
    private String username;

    @Getter
    @Column(nullable = false)
    private String password;

    @Getter
    @Column(nullable = false)
    private String first_name;

    @Getter
    @Column(nullable = false)
    private String last_name;

    @Getter
    @Temporal(TemporalType.TIMESTAMP) // Ensures correct timestamp storage
    @Column(nullable = false, updatable = false)
    private Date user_created_date = new Date();







}
