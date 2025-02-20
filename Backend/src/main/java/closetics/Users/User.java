package closetics.Users;

import jakarta.persistence.*;
import org.mindrot.jbcrypt.*;


@Entity(name = "usersTable")
@Table(uniqueConstraints = {
@UniqueConstraint(columnNames = "username"),
@UniqueConstraint(columnNames = "emailId")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String emailId;
    private String username;
    private String passwordHash;
    private String userTier;

    public User(String name, String emailId, String username, String passwordHash, String userTier) {
        this.name = name;
        this.emailId = emailId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.userTier = userTier;
    }

    public User() {
    }

    public long getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmailId(){
        return emailId;
    }

    public void setEmailId(String emailId){
        this.emailId = emailId;
    }

    public String getPasswordHash() {return passwordHash;}

    public boolean comparePasswordHash(String checkPass){
        return BCrypt.checkpw(checkPass, passwordHash);
    }

    public static String encryptPassowrd(String p){
        return BCrypt.hashpw(p,BCrypt.gensalt());
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {this.username = username;}

    public String getUserTier() {return userTier;}

    public void setUserTier(String userTier) {this.userTier = userTier;}
}
