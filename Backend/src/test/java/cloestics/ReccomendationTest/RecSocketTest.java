package cloestics.ReccomendationTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import closetics.Outfits.OutfitRepository;
import closetics.Users.RecomendationsSocket.RecService;
import closetics.Users.RecomendationsSocket.RecSocket;
import closetics.Users.UserProfile.UserProfile;
import closetics.Users.UserProfile.UserProfileController;
import closetics.Users.UserProfile.UserProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import closetics.Outfits.Outfit;
import closetics.Users.User;
import closetics.Users.UserRepository;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = RecSocket.class)
@ContextConfiguration(classes = closetics.MainApplication.class)
public class RecSocketTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserProfileRepository uRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private OutfitRepository outfitRepository;

    @MockBean
    private RecService recService;

    private UserProfile profile1;
    private UserProfile profile2;
    private User user1;
    private User user2;

    @BeforeEach
    void setup() {
        // Create two user profiles and users
        profile1 = new UserProfile();
        profile1.setId(1L);
        profile1.setIsPublic(true);

        profile2 = new UserProfile();
        profile2.setId(2L);
        profile2.setIsPublic(false);

        user1 = new User();
        user1.setUserId(1);
        user1.SetUserProfile(profile1);

        user2 = new User();
        user2.setUserId(2);
        user2.SetUserProfile(profile2);

        profile1.addFollowing(profile2);

        
    }

}
