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
    private String securityQuestion1;
    private String securityQuestion2;
    private String securityQuestion3;

    public User(long id, String name, String emailId, String username, String passwordHash, String userTier, String securityQuestion1, String securityQuestion2, String securityQuestion3) {
        this.id = id;
        this.name = name;
        this.emailId = emailId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.userTier = userTier;
        this.securityQuestion1 = securityQuestion1;
        this.securityQuestion2 = securityQuestion2;
        this.securityQuestion3 = securityQuestion3;
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

    public boolean compareHashedPassword(String checkPass){
        return BCrypt.checkpw(checkPass, passwordHash);
    }

    public boolean compareHashedSQ(String checkSQ){
        if(BCrypt.checkpw(checkSQ, securityQuestion1) || BCrypt.checkpw(checkSQ, securityQuestion2)
                || BCrypt.checkpw(checkSQ, securityQuestion3)){
            return true;
        }
        return false;
    }

    public static String encryptString(String p){
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
        (?=.*[!@#$%^&+=]) a special character must occur at least once
        (?=\\S+$) no whitespace allowed in the entire string
        .{8,} at least 8 characters
     */
    public static boolean validatePassword(String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}";
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

    public String getSecurityQuestion1() {return securityQuestion1;}

    public void setSecurityQuestion1(String securityQuestion1) {this.securityQuestion1 = securityQuestion1;}

    public String getSecurityQuestion2() {return securityQuestion2;}

    public void setSecurityQuestion2(String securityQuestion2) {this.securityQuestion2 = securityQuestion2;}

    public String getSecurityQuestion3() {return securityQuestion3;}

    public void setSecurityQuestion3(String securityQuestion3) {this.securityQuestion3 = securityQuestion3;}
}
