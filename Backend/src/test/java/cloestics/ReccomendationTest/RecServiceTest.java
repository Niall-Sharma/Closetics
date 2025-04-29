package cloestics.ReccomendationTest;

import closetics.Clothes.Clothing;
import closetics.Outfits.Outfit;
import closetics.Outfits.OutfitRepository;
import closetics.Users.RecomendationsSocket.RecService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RecServiceTest {

    @Test
    void testGetRecommendations_returnsRandomOutfit() {
        OutfitRepository mockRepo = mock(OutfitRepository.class);
        RecService recService = new RecService(mockRepo);

        List<Outfit> followed = new ArrayList<>();
        Outfit followedOutfit = new Outfit();

        followedOutfit.setOutfitItems(List.of(new Clothing()));
        followed.add(followedOutfit);

        Outfit randomOutfit = new Outfit();

        when(mockRepo.count()).thenReturn(10L);
        when(mockRepo.findById(anyLong())).thenReturn(Optional.of(randomOutfit));

        List<Outfit> recs = recService.getRecommendations(1L, followed);

        assertFalse(recs.isEmpty());
        verify(mockRepo, atMost(1)).findById(anyLong());
    }
}
