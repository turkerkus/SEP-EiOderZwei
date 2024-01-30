package rmi;

import sharedClasses.ServerCard;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HardBot extends BasicBot {

    BioCardCombinator bioCardCombinator;
    CornCardCombinator cornCardCombinator;
    MixedCornCardCombinator mixedCornCardCombinator;

    public HardBot(UUID gameId, UUID botID, String botName, boolean hahnKarte, GameSessionCallback callback) {
        super(gameId, botID, botName, hahnKarte, callback);
        // Additional initialization for HardBot
    }


    @Override
    protected void performActions() {

        // Assess the current game state
        actionTypes = new ArrayList<>();

        if (getCardHand().size() >= 24) {
            getEggs();
            return;
        }

        // Get players
        getGameState();

        // check for actions
        shouldGetEggs();
        if (actionTypes.isEmpty()) shouldTakeRoosterCard();
        if (actionTypes.isEmpty()) shouldDrawCard();
        System.out.println(actionTypes);

        //perform Action
        if (!actionTypes.isEmpty()) {
            play(actionTypes.get(0));
        }


    }

    @Override
    protected void shouldGetEggs() {

        selectedCards = new ArrayList<>();

        // Handle other cases if needed
        if (bioCornCardsTotalValue >= 5) {
            // get and put the combination in the selected cards
            bioCardCombinator = new BioCardCombinator(bioCornCards);
            selectedBioCornCards = bioCardCombinator.bioCardCombination;
            System.out.println(bioCardCombinator);

            // check if any combinations was found
            if (!selectedBioCornCards.isEmpty()) actionTypes.add(ActionType.GET_EGGS_BIO);
            eggPoints = bioCardCombinator.getEggPoints();

        } else if (cornCardsTotalValue >= 5) {
            // get and put the combination in the selected cards
            cornCardCombinator = new CornCardCombinator(cornCards);
            selectedCornCards = cornCardCombinator.cornCardCombination;
            System.out.println(cornCardCombinator);

            // check if any combinations was found
            if (!selectedCornCards.isEmpty()) actionTypes.add(ActionType.GET_EGGS_CORN);
            eggPoints = cornCardCombinator.getEggPoints();

        } else if (bioCornCardsTotalValue + cornCardsTotalValue > 5) {
            Map<UUID, ServerCard> mergedMap = new ConcurrentHashMap<>(bioCornCards);
            mergedMap.putAll(cornCards);

            // get and put the combination in the selected cards
            mixedCornCardCombinator = new MixedCornCardCombinator(mergedMap);
            selectedCards = mixedCornCardCombinator.mixedCardCombination;
            System.out.println(mixedCornCardCombinator);

            // check if any combinations was found
            if (!selectedCards.isEmpty()) actionTypes.add(ActionType.GET_EGGS_BIO_CORN);
            eggPoints = mixedCornCardCombinator.getEggPoints();

        }
    }


}
