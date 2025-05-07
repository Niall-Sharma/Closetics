package closetics.Users;

import closetics.Outfits.Outfit;
import jakarta.persistence.*;
import org.mindrot.jbcrypt.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import closetics.Users.UserProfile.UserProfile;
import java.util.List;

@Entity(name = "users_table")
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
    private String sQA1;
    private long sQID1;
    private String sQA2;
    private long sQID2;
    private String sQA3;
    private long sQID3;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "userProfile_id")
    @JsonIgnore
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @JsonIgnore
    private List<Outfit> outfits;

    @PreRemove
    private void prepareForDelete() {
        if (this.userProfile != null && this.userProfile.getOutfits() != null) {
            this.userProfile.getOutfits().clear();
        }
        
    }

    public User(long userId, String name, String email, String username, String password, String userTier, String sQA1,
                long sQID1, String sQA2, long sQID2, String sQA3, long sQID3) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.userTier = userTier;
        this.sQA1 = sQA1;
        this.sQID1 = sQID1;
        this.sQA2 = sQA2;
        this.sQID2 = sQID2;
        this.sQA3 = sQA3;
        this.sQID3 = sQID3;
        userProfile = new UserProfile(false, username);
    }

    public User() {

    }


    public boolean compareHashedPassword(String checkPass){
        return BCrypt.checkpw(checkPass, password);
    }

    public boolean compareHashedSQ(String checkSQ, long SQID){
        if(BCrypt.checkpw(checkSQ, sQA1) && SQID == sQID1){
            return true;
        } else if (BCrypt.checkpw(checkSQ, sQA2) && SQID == sQID2) {
            return true;
        } else if (BCrypt.checkpw(checkSQ, sQA3) && SQID == sQID3) {
            return true;
        } else {
            return false;
        }
    }

    public static String encryptString(String p){
        return BCrypt.hashpw(p,BCrypt.gensalt());
    }

    /*
Regex explanation:
    [0-9A-Za-z] contains only letters and numbers
    {3,16} between 3 and 16 characters long
 */
    public static Boolean validateUsername(String username){
        String pattern = "[0-9A-Za-z]{3,16}";
        return username.matches(pattern);
    }

    /*
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
    Regex explanation:
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

    public String getsQA1() {return sQA1;}
    public void setsQA1(String sQA1) {this.sQA1 = sQA1;}

    public String getsQA2() {return sQA2;}
    public void setsQA2(String sQA2) {this.sQA2 = sQA2;}

    public String getsQA3() {return sQA3;}
    public void setsQA3(String sQA3) {this.sQA3 = sQA3;}

    public long getsQID1() {return sQID1;}
    public void setsQID1(long sQID1) {this.sQID1 = sQID1;}

    public long getsQID2() {return sQID2;}
    public void setsQID2(long sQID2) {this.sQID2 = sQID2;}

    public long getsQID3() {return sQID3;}
    public void setsQID3(long sQID3) {this.sQID3 = sQID3;}

    public UserProfile GetUserProfile(){
    return userProfile;
  }
    public void SetUserProfile(UserProfile userProfile){this.userProfile = userProfile;}

    public List<Outfit> getOutfits() {
        return outfits;
    }

    public void setOutfits(List<Outfit> outfits) {
        this.outfits = outfits;
    }
}
