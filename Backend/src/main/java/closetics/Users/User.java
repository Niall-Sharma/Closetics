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
        this.passwordHash = encryptPassword(passwordHash);
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

    public static String encryptPassword(String p){
        return BCrypt.hashpw(p,BCrypt.gensalt());
    }
    /*
Regex explanataion:
    [0-9A-Za-z] contains only letters and numbers
    {3,16} between 3 and 16 characters long
 */
    public static Boolean validateUsername(String username){
        String pattern = "[0-9A-Za-z]{3,16}";
        return username.matches(pattern);
    }

    /*
    Regex explanataion:
        (?=.*[0-9]) a digit must occur at least once
        (?=.*[a-z]) a lower case letter must occur at least once
        (?=.*[A-Z]) an upper case letter must occur at least once
        (?=.*[@#$%^&+=]) a special character must occur at least once
        (?=\\S+$) no whitespace allowed in the entire string
        .{8,} at least 8 characters
     */
    public static boolean validatePassword(String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        return password.matches(pattern);
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
