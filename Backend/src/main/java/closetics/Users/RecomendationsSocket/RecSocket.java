
package closetics.Users.RecomendationsSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import closetics.Outfits.Outfit;
import closetics.Outfits.OutfitRepository;
import closetics.Users.UserProfile.UserProfile;
import closetics.Users.UserProfile.UserProfileRepository;

@Controller      // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/recomendation/{uid}")  // this is Websocket url
public class RecSocket {

  // cannot autowire static directly (instead we do it by the below
  // method
	private static UserProfileRepository UserProfileRepository; 
  private static OutfitRepository OutfitRepository;

	/*
   * Grabs the MessageRepository singleton from the Spring Application
   * Context.  This works because of the @Controller annotation on this
   * class and because the variable is declared as static.
   * There are other ways to set this. However, this approach is
   * easiest.
	 */
	@Autowired
	public void setUserProfileRepository(UserProfileRepository repo) {
		UserProfileRepository = repo;  // we are setting the static variable
	}

  @Autowired
  public void setOutfitRepository(OutfitRepository repo){
    OutfitRepository = repo;
  }

	// Store all socket session and their corresponding username.
	private static Map<Session, Long> sessionUsernameMap = new Hashtable<>();
	private static Map<Long, Session> uidSessionMap = new Hashtable<>();
  private static Map<Long, List<Outfit>> followingOutfitMap = new Hashtable<>();

	private final Logger logger = LoggerFactory.getLogger(RecSocket.class);

	@OnOpen
	public void onOpen(Session session, @PathParam("UID") long UID) 
      throws IOException {

		logger.info("Entered into Open");

    // store connecting user information
		sessionUsernameMap.put(session, UID);
		uidSessionMap.put(UID, session);
    UserProfile uProfile = UserProfileRepository.findById(UID);
    List<UserProfile> following = uProfile.GetFollowing();
    List<Outfit> followingOutfits = new ArrayList<>();
    for(int i = 0; i < following.size();i++){
      followingOutfits.addAll(following.get(i).GetOutfits());
    }
    followingOutfitMap.put(UID, followingOutfits);

		
	}

	@OnMessage
	public void onMessage(Session session) throws IOException {

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
		logger.info("Entered into Error");
		throwable.printStackTrace();
	}


	private void sendRec(long UID) {
    int recSize = 30;
    List<Outfit> RecList = new ArrayList<>();
		try {
      for(int i = 0; i < recSize; i++){
      int randomChoice = (int)((Math.random()*10)+1);
      if(randomChoice > 5){
        int randomOutfitInteger = (int)((Math.random()*followingOutfitMap.get(UID).size()));
        RecList.add(followingOutfitMap.get(UID).remove(randomOutfitInteger)); 
      }else{

        Long randomOutfit = Math.round(Math.random()*OutfitRepository.count());
        RecList.add(OutfitRepository.findById(randomOutfit).get());
      }
      }
        uidSessionMap.get(UID).getBasicRemote().sendObject(RecList);
		} 
    catch (IOException e) {
			logger.info("Exception: " + e.getMessage().toString());
			e.printStackTrace();
		}
    catch(EncodeException encodeException){
      logger.info("Exception: " + encodeException.getMessage().toString());
      encodeException.printStackTrace();
    }
	}


} 
