package cloestics.ReccomendationTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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

import java.io.IOException;
import java.util.*;

import jakarta.websocket.Session;
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

    private RecSocket recSocket;

    @BeforeEach
    void setup() {


        recSocket.setUserRepository(userRepository);
        recSocket.setOutfitRepository(outfitRepository);
        recSocket.setUserProfileRepository(uRepository);

        recService = new RecService(outfitRepository);

        recSocket.setRecommendationService(recService);

    }

    @Test
    void testOnOpen() throws IOException{
        Session session = mock(Session.class);
        long userId = 1L;

        UserProfile mockUserProfile = new UserProfile();
        List<UserProfile> following = new ArrayList<>();
        UserProfile followed = new UserProfile();
        Outfit outfit = new Outfit();
        followed.addOutfit(outfit);
        mockUserProfile.addFollowing(followed);
        followed.addFollower(mockUserProfile);

        when(uRepository.findById(userId)).thenReturn(Optional.of(mockUserProfile));
        when(mockUserProfile.getFollowing()).thenReturn(mockUserProfile.getFollowing());

        recSocket.onOpen(session, userId);

        // No assertions, just ensuring no exceptions
    }
}
