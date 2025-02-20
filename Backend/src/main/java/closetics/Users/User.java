package closetics.Users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.mindrot.jbcrypt.*;


@Entity(name = "usersTable")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String emailId;
    private String username;
    private String passwordHash;


    public User(String name, String emailId, String username, String password) {
        this.name = name;
        this.emailId = emailId;
        this.username = username;
        this.passwordHash = encryptPassowrd(password);
    }

    public User() {
    }

    public int getId(){
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

    public String getPasswordHash() {
        return passwordHash;
    }

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

    public void setUsername(String username) {
        this.username = username;

    }
}
