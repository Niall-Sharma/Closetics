package closetics.Users;

import jakarta.persistence.*;
import org.mindrot.jbcrypt.*;


@Entity(name = "usersTable")
@Table(uniqueConstraints = {
@UniqueConstraint(columnNames = "username"),
@UniqueConstraint(columnNames = "email")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    private String name;
    private String email;
    private String username;
    private String password;
    private String userTier;
    private String securityQuestion1;
    private String securityQuestion2;
    private String securityQuestion3;

    public User(long userId, String name, String email, String username, String password, String userTier, String securityQuestion1, String securityQuestion2, String securityQuestion3) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.userTier = userTier;
        this.securityQuestion1 = securityQuestion1;
        this.securityQuestion2 = securityQuestion2;
        this.securityQuestion3 = securityQuestion3;
    }

    public User() {}


    public boolean compareHashedPassword(String checkPass){
        return BCrypt.checkpw(checkPass, password);
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
    Don't even ask I took it from stack overflow
 */
    public static Boolean validateEmail(String email) {
        String pattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\" +
                "x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9]" +
                "(?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9]" +
                "[0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:" +
                "[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        return email.matches(pattern);
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

    public long getUserId(){return userId;}
    public void setUserId(int userId){this.userId = userId;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getUsername() {return username;}
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
