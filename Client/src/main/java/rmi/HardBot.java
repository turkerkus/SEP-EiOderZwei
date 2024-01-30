package rmi;

import sharedClasses.CustomTimer;
import sharedClasses.Hand;
import sharedClasses.ServerCard;
import sharedClasses.ServerPlayer;

import java.util.*;
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

    @Override
    protected void drawnFoxCard() {
        getGameState();
        // Check if all players' hands are empty
        boolean allHandsEmpty = players.values().stream()
                .allMatch(player -> player.getCardHand().isEmpty());

        if (players.isEmpty() || allHandsEmpty) {
            if (hatHahnKarte()) {
                // endplayer turn
                CustomTimer timer = new CustomTimer();
                timer.schedule(this::endTurn, 2);

            } else {
                endTurn();
            }


        } else {


            // choose the player with the maximum points else choose the player with max total card value
            ServerPlayer randomPlayer = players.values().stream()
                    .max(Comparator.comparing(ServerPlayer::getPunkte))
                    .orElse(playerWithMaxCardValue());

            // get bio and corn cards of targeted player
            Map<UUID, ServerCard> bio = randomPlayer.getCardHand().getBioCornCards();
            Map<UUID, ServerCard> corn = randomPlayer.getCardHand().getCornCards();

            // get random player hand size
            int randomPlayerHandSize = randomPlayer.getCardCount();
            System.out.println(randomPlayer.getServerPlayerName() + " hand size is " + randomPlayerHandSize);

            actionTypes = new ArrayList<>();
            if (randomPlayerHandSize > 2) actionTypes.add(ActionType.STEAL_ALL_CARDS);
            else actionTypes.add(ActionType.STEAL_ONE_CARD);

            // choose a random action
            ActionType randomAction = actionTypes.get(0);

            switch (randomAction) {
                case STEAL_ONE_CARD:
                    ArrayList<ServerCard> serverCards = new ArrayList<>();

                    // get the card with the max value
                    if (!bio.isEmpty()) {
                        serverCards.add(getHighestValueCard(bio));
                    } else {
                        serverCards.add(getHighestValueCard(corn));
                    }

                    callback.stealOneCard(gameId, randomPlayer.getServerPlayerId(), serverCards, getServerPlayerId());
                    break;
                case STEAL_ALL_CARDS:
                    if ((24 - getCardCount()) >= randomPlayerHandSize - 1) {
                        callback.stealAllCards(gameId, randomPlayer.getServerPlayerId(), getServerPlayerId());
                    } else {
                        getEggs();
                    }
                    break;
            }

        }


    }


    protected ServerPlayer playerWithMaxCardValue() {
        // Variables to hold max egg points and corresponding players
        ServerPlayer maxBioValuePointsPlayer = null;
        int maxBioValuePoints = 0;
        int maxBioEggPoints = 0;
        ServerPlayer maxCornEggPointsPlayer = null;
        int maxCornEggPoints = 0;

        // Iterate over players and calculate egg points
        for (ServerPlayer player : players.values()) {
            // BioCorn cards
            ArrayList<ServerCard> bioC = new ArrayList<>(player.getCardHand().getBioCornCards().values());
            int bioEggPoints = calculateEggPoints(bioC);
            int bioValuePoints = calculateCardValue(player.getCardHand().getBioCornCards());
            if (bioValuePoints > maxBioValuePoints) {
                maxBioValuePoints = bioValuePoints;
                maxBioEggPoints = bioEggPoints;
                maxBioValuePointsPlayer = player;
            }

            // Corn cards
            ArrayList<ServerCard> cornC = new ArrayList<>(player.getCardHand().getCornCards().values());
            int cornEggPoints = calculateEggPoints(cornC);
            if (cornEggPoints > maxCornEggPoints) {
                maxCornEggPoints = cornEggPoints;
                maxCornEggPointsPlayer = player;
            }
        }

        // Compare and find the player with maximum egg points
        if (maxBioEggPoints > maxCornEggPoints) {
            return maxBioValuePointsPlayer;
        } else return (maxBioValuePoints > maxCornEggPoints) ? maxBioValuePointsPlayer : maxCornEggPointsPlayer;

    }
    @Override
    public void stealAllCards(UUID clientId) {
        System.out.println(getServerPlayerName() + ": is handling the stealing of cards");

        selectedCards = new ArrayList<>();

        // Get all your hand cards
        Hand hand = getCardHand();
        Map<UUID, ServerCard> bio = hand.getBioCornCards();
        Map<UUID, ServerCard> corn = hand.getCornCards();


        // Merge both bio corn cards and corn cards maps
        Map<UUID, ServerCard> mergedMap = new ConcurrentHashMap<>(bio);
        mergedMap.putAll(corn);
        System.out.println("this size of the Merged map is: " + mergedMap.size());

        // Choose a card with the max value and remove it from the merged map
        ServerCard maxBio = null;
        ServerCard maxCorn = null;

        if (!bio.isEmpty()) {
            maxBio = getHighestValueCard(bio);
        }

        if (!corn.isEmpty()) {
            maxCorn = getHighestValueCard(corn);
        }

        // Calculate the distance to the next egg point threshold for bio and corn cards
        int bioDistanceToNextThreshold = (maxBio != null) ? 5 - maxBio.getValue() : Integer.MAX_VALUE;
        int cornDistanceToNextThreshold = (maxCorn != null) ? 5 - maxCorn.getValue() : Integer.MAX_VALUE;

        // Determine which card to keep based on the distance to the next threshold
        ServerCard chosenCardToKeep = (bioDistanceToNextThreshold <= cornDistanceToNextThreshold) ? maxBio : maxCorn;

        if (maxCorn.getValue() == 4 && maxBio.getValue() == 1 ) chosenCardToKeep = maxCorn;
        else chosenCardToKeep = (bioDistanceToNextThreshold <= cornDistanceToNextThreshold) ? maxBio : maxCorn;


        if (chosenCardToKeep != null ) {
            mergedMap.remove(chosenCardToKeep.getServeCardID());

            // Add the rest of the cards to the selectedCards list for stealing
            //selectedCards.addAll(mergedMap.values());
            for (ServerCard card : mergedMap.values()) {
                System.out.println(card.getServeCardID() + card.toString());
                selectedCards.add(card);
            }
            removeMultipleCards(selectedCards);

            callback.stealingInProgress(gameId, clientId, getServerPlayerId(), selectedCards);
            System.out.println(getServerPlayerName() + " chose to keep : " + chosenCardToKeep.getServeCardID() + chosenCardToKeep.toString());
        } else {
            System.out.println("No cards to steal.");
        }
    }

    private ServerCard getHighestValueCard(Map<UUID, ServerCard> cards) {
        return  cards.values().stream()
                .max(Comparator.comparingInt(ServerCard::getValue))
                .orElse(null);
    }

    public ServerCard chooseBestCardToKeep(Map<UUID, ServerCard> bioCornCards, Map<UUID, ServerCard> cornCards) {
        // Calculate total values
        int bioTotal = calculateCardValue(bioCornCards);
        int cornTotal = calculateCardValue(cornCards);

        // Calculate potential egg points for each type

        int bioEggPoints = calculateEggPoints(new ArrayList<>(bioCornCards.values()));

        int cornEggPoints = calculateEggPoints(new ArrayList<>(cornCards.values()));

        // Determine which type of card is closer to the egg point threshold
        // or has the potential to earn more points
        boolean keepBio = shouldKeepBio(bioTotal, bioEggPoints, cornTotal, cornEggPoints);

        // Select the best card to keep
        if (keepBio) {
            return getHighestValueCard(bioCornCards);
        } else {
            return getHighestValueCard(cornCards);
        }
    }

    private boolean shouldKeepBio(int bioTotal, int bioEggPoints, int cornTotal, int cornEggPoints) {
        // Calculate the distance to the next egg point threshold for bio and corn cards
        int bioDistanceToNextThreshold = 5 - (bioTotal % 5);
        int cornDistanceToNextThreshold = 5 - (cornTotal % 5);

        // Calculate the potential egg points if one more card of each type is added
        int potentialBioEggPoints = (bioTotal + bioDistanceToNextThreshold)/5 * 2;
        int potentialCornEggPoints = (cornTotal + cornDistanceToNextThreshold)/5;

        // Compare the potential gain in egg points for bio and corn
        boolean bioHasGreaterPotential = (potentialBioEggPoints - bioEggPoints) > (potentialCornEggPoints - cornEggPoints);

        // If bio has a greater potential or the same potential but is closer to the threshold, keep bio
        return bioHasGreaterPotential || (bioEggPoints == cornEggPoints && bioDistanceToNextThreshold < cornDistanceToNextThreshold);
    }



}
