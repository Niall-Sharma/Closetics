package closetics.Users.UserProfile;

import java.util.List;
import java.util.stream.Collectors;

public class UserProfileDTO {
    private long id;
    private String username;
    private boolean isPublic;
    private List<UserProfileDTO> following;
    private List<UserProfileDTO> followers;

    public UserProfileDTO(UserProfile profile, int depth) {
        this.id = profile.getId();
        this.username = profile.getUsername();
        this.isPublic = profile.getIsPublic();

        if (depth > 0) {
            this.following = profile.getFollowing()
                    .stream()
                    .map(p -> new UserProfileDTO(p, depth - 1))
                    .collect(Collectors.toList());
        }
        if (depth > 0) {
            this.followers = profile.getFollowers()
                    .stream()
                    .map(p -> new UserProfileDTO(p, depth - 1))
                    .collect(Collectors.toList());
        }
    }

    // Getters
    public long getId() { return id; }
    public String getUsername() { return username; }
    public boolean getIsPublic() { return isPublic; }
    public List<UserProfileDTO> getFollowing() { return following; }
    public  List<UserProfileDTO> getFollowers(){
        return followers;
    }
}