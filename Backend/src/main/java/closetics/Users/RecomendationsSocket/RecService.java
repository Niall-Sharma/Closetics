package closetics.Users.RecomendationsSocket;

import closetics.Outfits.Outfit;
import closetics.Outfits.OutfitRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecService {

    private final OutfitRepository outfitRepository;

    public RecService(OutfitRepository outfitRepository) {
        this.outfitRepository = outfitRepository;
    }

    @Transactional
    public void initializeOutfits(List<Outfit> outfits) {
        // Force Hibernate to load the lazy collection
        for (Outfit outfit : outfits) {
            outfit.getOutfitItems().size();  // triggers loading
        }
    }
    @Transactional
    public List<Outfit> getRecommendations(long uid, List<Outfit> followedOutfits) {
        List<Outfit> recs = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            int randomChoice = (int)((Math.random() * 10) + 1);
            if (randomChoice > 5 && !followedOutfits.isEmpty()) {
                int randIndex = (int)(Math.random() * followedOutfits.size());
                Outfit o = followedOutfits.remove(randIndex);
                o.getOutfitItems().size();
                recs.add(o);
            } else {
                long count = outfitRepository.count();
                long randId = Math.max(1, Math.round(Math.random() * count));
                outfitRepository.findById(randId).ifPresent(o -> {
                    if (o.getOutfitItems() == null) {
                        o.setOutfitItems(new ArrayList<>());
                    }
                    o.getOutfitItems().size();
                    recs.add(o);
                });
            }
        }

        return recs;
    }
}