package cloestics;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserProfileController.class)
@ContextConfiguration(classes = closetics.MainApplication.class)
public class NiallSystemTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserProfileRepository uRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    }

    @Test
    void testGetUserProfile() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        mockMvc.perform(get("/userprofile/1"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(1)
                );
    }

    @Test
    void testGetAllUserProfiles() throws Exception {
        when(uRepository.findAll()).thenReturn(Arrays.asList(profile1, profile2));

        mockMvc.perform(get("/userprofile"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].id").value(1),
                        jsonPath("$[1].id").value(2)
                );
    }

    @Test
    void testCreateUserProfile() throws Exception {
        when(uRepository.save(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/userprofile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profile1)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(1),
                        jsonPath("$.following").isEmpty()
                );
    }

    @Test
    void testAddFollowing() throws Exception {
        // Setup find
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(uRepository.save(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/addFollowing/1/2"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(1),
                        jsonPath("$.following[0].id").value(2)
                );
    }

    @Test
    void testRemoveFollowing() throws Exception {
        // Pre-populate following
        profile1.addFollowing(profile2);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(uRepository.save(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/removeFollowing/1/2"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.following").isEmpty()
                );
    }

    @Test
    void testSwapPublicSetting() throws Exception {
        when(uRepository.findById(1L)).thenReturn(Optional.of(profile1));
        when(uRepository.save(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/userprofile/swappublicsetting/1"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.isPublic").value(false)
                );

    }

    @Test
    void testIsFollowing() throws Exception {
        profile1.getFollowing().add(profile2);
        when(uRepository.findById(1L)).thenReturn(Optional.of(profile1));

        mockMvc.perform(get("/userprofile/isFollowing/1/2"))
                .andExpectAll(
                        status().isOk(),
                        content().string("true")
                );
    }

    @Test
    void testGetOutfits() throws Exception {
        Outfit outfit = new Outfit();
        profile1.getOutfits().add(outfit);
        when(uRepository.findById(1L)).thenReturn(Optional.of(profile1));

        mockMvc.perform(get("/userprofile/outfits/1"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].outfitId").value(0)
                );

    }
}
