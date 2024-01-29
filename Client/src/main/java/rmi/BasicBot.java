package rmi;

import javafx.application.Platform;
import sharedClasses.CustomTimer;
import sharedClasses.Hand;
import sharedClasses.ServerCard;
import sharedClasses.ServerPlayer;

import java.io.Serial;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BasicBot extends ServerPlayer {
    @Serial
    private static final long serialVersionUID = 4650222820820902056L;


    private Random random;
    private final UUID gameId;

    private Map<UUID, ServerPlayer> players;
    private final GameSessionCallback callback;

    // game state
    ArrayList<ActionType> actionTypes;
    ServerPlayer roosterCardHolder;
    private Map<UUID, ServerCard> bioCornCards;
    private Integer bioCornCardsTotalValue = 0;
    private ArrayList<ServerCard> selectedBioCornCards;
    private Map<UUID, ServerCard> cornCards;
    private Integer cornCardsTotalValue = 0;
    private ArrayList<ServerCard> selectedCornCards;

    ArrayList<ServerCard> selectedCards;

    private Integer eggPoints = 0;


    public BasicBot(UUID gameId, UUID botID, String botName, boolean hahnKarte, GameSessionCallback callback) {
        super(botID, botName, hahnKarte);
        this.random = new Random();
        this.callback = callback;
        this.gameId = gameId;
        this.selectedCornCards = new ArrayList<>();
        this.selectedBioCornCards = new ArrayList<>();
        players = new ConcurrentHashMap<>();
        setBot(true);
    }

    private CustomTimer timer;

    public void takeTurn() {
        timer = new CustomTimer();
        timer.schedule(() -> {

            try {
                performActions();  // Implement this method as needed
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

        }, 8);
    }


    private void performActions() throws RemoteException {

        // Assess the current game state
        actionTypes = new ArrayList<>();

        if(getCardHand().size() >= 24){
            getEggs();
            return;
        }

        // Get players
        Map<UUID, ServerPlayer> playerMap = callback.getPlayers(gameId);
        if (playerMap == null) {
            return;
        }
        for (Map.Entry<UUID, ServerPlayer> entry : playerMap.entrySet()) {
            if (!entry.getKey().equals(getServerPlayerId())) {
                players.put(entry.getKey(), entry.getValue());
            }
        }

        // get rooster card holder
        roosterCardHolder = callback.getRoosterCardHolder(gameId);

        //empty the selected lists
        selectedCornCards = new ArrayList<>();
        selectedBioCornCards = new ArrayList<>();
        selectedCards = new ArrayList<>();

        // get all your bio corn cards
        bioCornCards = getCardHand().getBioCornCards();

        // calculate your bioCardPoints
        bioCornCardsTotalValue = calculateCardValue(bioCornCards);
        System.out.println(getServerPlayerName() + ": total value of bot bio Cards is " + bioCornCardsTotalValue);

        // get all your corn cards
        cornCards = getCardHand().getCornCards();
        System.out.println(getServerPlayerName() + ": total value of bot corn Cards is  " + cornCardsTotalValue);

        // calculate your corn card points
        cornCardsTotalValue = calculateCardValue(cornCards);

        // check for possible actions
        shouldDrawCard();
        shouldGetEggs();
        shouldTakeRoosterCard();

        // choose a random action
        ActionType randomAction = getRandomAction();

        //perform Action
        play(randomAction);

    }

    private ActionType getRandomAction() {
        // Generate a random index
        random = new Random();
        int randomIndex = random.nextInt(actionTypes.size());

        // Use the random index to get a random Action
        return actionTypes.get(randomIndex);
    }

    public void play(ActionType actionType) {
        switch (actionType) {
            //TODO remove the print line of cards:  for loops
            case DRAW_CARD -> callback.drawCard(getServerPlayerId(), gameId);
            case GET_EGGS_BIO -> {
                System.out.println(getServerPlayerName() + ": is going to change bio cards");
                for (ServerCard card : selectedBioCornCards) System.out.println(card.toString());
                callback.karteUmtauschen(gameId, getServerPlayerId(), eggPoints, selectedBioCornCards);

            }
            case GET_EGGS_CORN -> {
                System.out.println(getServerPlayerName() + ": is going to change corn cards");
                for (ServerCard card : selectedCornCards) System.out.println(card.toString());
                callback.karteUmtauschen(gameId, getServerPlayerId(), eggPoints, selectedCornCards);

            }

            case GET_EGGS_BIO_CORN -> {
                System.out.println(getServerPlayerName() + ": is going to change corn cards");
                for (ServerCard card : selectedCards) System.out.println(card.toString());
                callback.karteUmtauschen(gameId, getServerPlayerId(), eggPoints, selectedCards);

            }
            case TAKE_ROOSTER_CARD -> callback.hahnKlauen(gameId, getServerPlayerId());
        }
    }

    // Basic logic to decide whether to draw a card or not
    private void shouldDrawCard() {
        // Simple decision for easy difficulty bot: always tries to draw a card
        if (hatHahnKarte() && getCardHand().size() <= 22) {
            actionTypes.add(ActionType.DRAW_CARD);
        } else if (getCardHand().size() <= 23) {
            actionTypes.add(ActionType.DRAW_CARD);
        }

    }

    private void shouldTakeRoosterCard() {
        if (!hatHahnKarte() && getPunkte() < roosterCardHolder.getPunkte()) {
            actionTypes.add(ActionType.TAKE_ROOSTER_CARD);
        }
    }

    // Getting Eggs Methods
    private void shouldGetEggs() {
        // Handle other cases if needed
        if (bioCornCardsTotalValue > 5) {
            actionTypes.add(ActionType.GET_EGGS_BIO);
            eggPoints = calculateEggPoints(selectedBioCornCards);

        } else if (cornCardsTotalValue > 5) {
            actionTypes.add(ActionType.GET_EGGS_CORN);
            eggPoints = calculateEggPoints(selectedCornCards);

        } else if (bioCornCardsTotalValue + cornCardsTotalValue > 5) {
            System.out.println(getServerPlayerName() + ": total value of bot hand is " + (bioCornCardsTotalValue + cornCardsTotalValue));
            actionTypes.add(ActionType.GET_EGGS_BIO_CORN);
            selectedCards = new ArrayList<>();
            selectedCards.addAll(bioCornCards.values());
            selectedCards.addAll(cornCards.values());
            eggPoints = calculateEggPoints(selectedCards);

        }


    }

    private void getEggs() {
        // get all your bio corn cards
        bioCornCards = getCardHand().getBioCornCards();

        // calculate your bioCardPoints
        bioCornCardsTotalValue = calculateCardValue(bioCornCards);

        // get all your corn cards
        cornCards = getCardHand().getCornCards();

        // calculate your corn card points
        cornCardsTotalValue = calculateCardValue(cornCards);

        if (bioCornCardsTotalValue > 5) {
            eggPoints = calculateEggPoints(selectedBioCornCards);
            callback.karteUmtauschen(gameId, getServerPlayerId(), eggPoints, selectedBioCornCards);

        } else if (cornCardsTotalValue > 5) {
            actionTypes.add(ActionType.GET_EGGS_CORN);
            eggPoints = calculateEggPoints(selectedCornCards);
            callback.karteUmtauschen(gameId, getServerPlayerId(), eggPoints, selectedCornCards);

        } else if (bioCornCardsTotalValue + cornCardsTotalValue > 5) {
            actionTypes.add(ActionType.GET_EGGS_BIO_CORN);
            selectedCards = new ArrayList<>();
            selectedCards.addAll(bioCornCards.values());
            selectedCards.addAll(cornCards.values());
            eggPoints = calculateEggPoints(selectedCards);
            callback.karteUmtauschen(gameId, getServerPlayerId(), eggPoints, selectedCards);

        }


    }

    private Integer calculateCardValue(Map<UUID, ServerCard> serverCards) {
        int cardValuePoints = 0;
        for (Map.Entry<UUID, ServerCard> entry : serverCards.entrySet()) {
            ServerCard serverCard = entry.getValue();
            cardValuePoints += serverCard.getValue();
            switch (serverCard.getType()) {
                case "BioKoerner" -> selectedBioCornCards.add(serverCard);
                case "Koerner" -> selectedCornCards.add(serverCard);
            }
        }
        return cardValuePoints;
    }

    // calculate eggPoints
    public Integer calculateEggPoints(ArrayList<ServerCard> selectedCards) {
        int kornzahl = 0;
        int bKornzahl = 0;
        int kornzahlwert = 0;
        int bKornzahlwert = 0;
        int eggPoints = 0;

        for (ServerCard c : selectedCards) {
            if (c.getType().equals("Koerner")) {
                kornzahl += 1;
            } else {
                bKornzahl += 1;
            }
        }

        for (ServerCard c : selectedCards) {
            if (kornzahl == 0 && bKornzahl >= 1) {      //nur Biokörner
                bKornzahlwert += c.getValue();
            } else {                              //normale oder gemischte Körner
                kornzahlwert += c.getValue();

            }
        }


        // Calculate the total value for Koerner and BioKoerner cards


        // Each egg needs at least five seeds, organic grains count double
        // Every 5 grains make an egg, and every 5 organic grains make two eggs
        eggPoints += (int) Math.floor(kornzahlwert / 5);
        eggPoints += (int) Math.floor(bKornzahlwert / 5) * 2;


        return eggPoints;
    }


    // Method to handle drawing a card
    public void drawCard(ServerCard card) throws RemoteException {
        // Interact with ServerTable to draw a card
        if(card.getType() == "Fuchs") drawnFoxCard();
        System.out.println(getServerPlayerName() + " has " + getCardCount() + " in hand");
    }

    private void drawnFoxCard() {
        if (players.isEmpty()) {
            if (hatHahnKarte()) {
                // endplayer turn
                CustomTimer timer = new CustomTimer();
                timer.schedule(() -> {

                    endTurn();

                }, 2);

            } else {
                endTurn();
            }


        } else {


            // choose a random player
            ServerPlayer randomPlayer = chooseRandomPlayer();

            // get bio and corn cards of targeted player
            Map<UUID, ServerCard> bio = randomPlayer.getCardHand().getBioCornCards();
            Map<UUID, ServerCard> corn = randomPlayer.getCardHand().getCornCards();

            // get random player hand size
            Integer randomPlayerHandSize = randomPlayer.getCardCount();
            System.out.println("randomPlayer hand size is " + randomPlayerHandSize);

            actionTypes = new ArrayList<>(Arrays.asList(ActionType.STEAL_ONE_CARD));
            if (randomPlayerHandSize > 2) {
                actionTypes.add(ActionType.STEAL_ALL_CARDS);
            }
            // choose a random action
            ActionType randomAction = getRandomAction();

            if (randomPlayerHandSize == 0) {
                players.remove(randomPlayer.getServerPlayerId());
                drawnFoxCard();
            } else {
                // Merge the maps into a new map
                Map<UUID, ServerCard> mergedMap = new ConcurrentHashMap<>(bio);
                mergedMap.putAll(corn);

                switch (randomAction) {
                    case STEAL_ONE_CARD:
                        ArrayList<ServerCard> serverCards = chooseRandomCard(mergedMap);
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


    }

    public void stealingCardComplete(ArrayList<ServerCard> selectedCards) {

        // endplayer turn
        CustomTimer timer = new CustomTimer();
        timer.schedule(() -> {

            endTurn();

        }, 5);

    }

    public void stealAllCards(UUID clientId) {
        System.out.println(getServerPlayerName() + ": is handling the stealing of cards");

        selectedCards = new ArrayList<>();

        // Get all your hand cards
        Hand hand = getCardHand();

        // Merge both bio corn cards and corn cards maps
        Map<UUID, ServerCard> mergedMap = new ConcurrentHashMap<>(hand.getBioCornCards());
        mergedMap.putAll(hand.getCornCards());
        System.out.println("this size of the Merged map is: "+ mergedMap.size());

        // Choose a random card to keep and remove it from the merged map
        ArrayList<ServerCard> chosenCardToKeep = chooseRandomCard(mergedMap);
        if (chosenCardToKeep != null && !chosenCardToKeep.isEmpty()) {
            ServerCard cardToKeep = chosenCardToKeep.get(0);
            mergedMap.remove(cardToKeep.getServeCardID());

            // Add the rest of the cards to the selectedCards list for stealing
            //selectedCards.addAll(mergedMap.values());
            for(ServerCard card : mergedMap.values()){
                System.out.println(card.getServeCardID() + card.toString());
                selectedCards.add(card);
            }
            removeMultipleCards(selectedCards);

            callback.stealingInProgress(gameId, clientId, getServerPlayerId(), selectedCards);
            System.out.println(getServerPlayerName() + " chose to keep : "+ cardToKeep.getServeCardID() + cardToKeep.toString());
        } else {
            System.out.println("No cards to steal.");
        }
    }


    private ArrayList<ServerCard> chooseRandomCard(Map<UUID, ServerCard> cards) {
        ArrayList<ServerCard> cardList = new ArrayList<>();
        for (Map.Entry<UUID, ServerCard> entry : cards.entrySet()) {
            ServerCard card = entry.getValue();
            cardList.add(card);
        }
        random = new Random();
        if (cardList.isEmpty()) {
            return null;
        }
        int randomIndex = random.nextInt(cardList.size());
        ServerCard randomCard = cardList.get(randomIndex);

        // Use the random index to get a random Action
        ArrayList<ServerCard> serverCards = new ArrayList<>();
        serverCards.add(randomCard);
        return serverCards;

    }

    private ServerPlayer chooseRandomPlayer() {
        ArrayList<ServerPlayer> playerList = new ArrayList<>();
        for (Map.Entry<UUID, ServerPlayer> entry : players.entrySet()) {
            ServerPlayer player = entry.getValue();
            // check if the player is this bot
            if (!Objects.equals(player.getServerPlayerId(), getServerPlayerId())) {
                playerList.add(player);
            }
        }
        random = new Random();
        int randomIndex = random.nextInt(playerList.size());

        /* Use the random index to get a random Action */
        return playerList.get(randomIndex);
    }


    // Method to end the bot's turn
    private void endTurn() {
        // Interact with ServerTable to end the turn
        callback.endPlayerTurn(gameId);
    }

    public void removeMultipleCards(ArrayList<ServerCard> discardedCards) {
        // Executor to schedule card removal with delays
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        // Schedule the card removal with a delay
        int delay = 0; // Initial delay
        for (ServerCard card : discardedCards) { // No need to create a new ArrayList here
            executorService.schedule(() -> Platform.runLater(() -> remove(card.getServeCardID(), card.getType())), delay, TimeUnit.SECONDS);
            delay += 1; // Increment delay for the next card
        }

    }


}

