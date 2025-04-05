
package closetics.Users.RecomendationsSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import closetics.Users.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

import closetics.Outfits.Outfit;
import closetics.Outfits.OutfitRepository;
import closetics.Users.UserProfile.UserProfile;
import closetics.Users.UserProfile.UserProfileRepository;

@Controller    
@ServerEndpoint(value = "/recommendation/{uid}")
public class RecSocket {

	private static UserProfileRepository UserProfileRepository; 
  private static OutfitRepository OutfitRepository;
  private static UserRepository UserRepository;
    private static RecService recommendationService;

	@Autowired
	public void setUserProfileRepository(UserProfileRepository repo) {
		UserProfileRepository = repo;
	}

  @Autowired
  public void setOutfitRepository(OutfitRepository repo){
    OutfitRepository = repo;
  }

  @Autowired
  public void setUserRepository(UserRepository repo){
        UserRepository = repo;
    }

    @Autowired
    public void setRecommendationService(RecService service) {
        recommendationService = service;
    }

	// Store all socket session and their corresponding username.
	private static Map<Session, Long> sessionUsernameMap = new Hashtable<>();
	private static Map<Long, Session> uidSessionMap = new Hashtable<>();
  private static Map<Long, List<Outfit>> followingOutfitMap = new Hashtable<>();


	@OnOpen
	public void onOpen(Session session, @PathParam("uid") long UID)
      throws IOException {


    // store connecting user information
        sessionUsernameMap.put(session, UID);
		uidSessionMap.put(UID, session);
    UserProfile uProfile = UserRepository.findById(UID).get().GetUserProfile();
    System.out.println(uProfile.toString());
    List<UserProfile> following = uProfile.GetFollowing();
    List<Outfit> followingOutfits = new ArrayList<>();
    for(int i = 0; i < following.size();i++){
      followingOutfits.addAll(following.get(i).GetOutfits());
    }
    followingOutfitMap.put(UID, followingOutfits);

		
	}

	@OnMessage
	public void onMessage(String message, Session session) throws IOException {

		long UID = sessionUsernameMap.get(session);
    
    sendRec(UID);



	}
    
	@OnClose
	public void onClose(Session session) throws IOException {

		long UID = sessionUsernameMap.get(session);
		sessionUsernameMap.remove(session);
		uidSessionMap.remove(UID);
    followingOutfitMap.remove(UID);

	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		throwable.printStackTrace();
	}

  //Maybe take stats into consideration when deciding on what recomendations to pick
	private void sendRec(long UID) {
    int recSize = 1;
    System.out.println("OUTFITS: "+followingOutfitMap);
        List<Outfit> RecList = new ArrayList<Outfit>();
		try {
      for(int i = 0; i < recSize; i++){
          int randomChoice = (int)((Math.random()*10)+1);
          if(randomChoice > 5 && !followingOutfitMap.get(UID).isEmpty()){
            int randomOutfitInteger = (int)((Math.random()*followingOutfitMap.get(UID).size()));
            RecList.add(followingOutfitMap.get(UID).remove(randomOutfitInteger));
          }else{
            Long randomOutfit = Math.round(Math.random()*OutfitRepository.count());
            RecList.add(OutfitRepository.findById(randomOutfit).get());
          }
      }
        List<Outfit> recList = recommendationService.getRecommendations(UID, followingOutfitMap.get(UID));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(RecList);
        uidSessionMap.get(UID).getBasicRemote().sendText(json);
		} 
    catch (IOException e) {
			e.printStackTrace();
		}
    }


}
