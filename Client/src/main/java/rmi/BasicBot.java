package rmi;

import sharedClasses.ServerCard;
import sharedClasses.ServerPlayer;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BasicBot extends ServerPlayer {

    private Random random;
    private UUID gameId;

    private Map<UUID, ServerPlayer> players;
    private GameSessionCallback callback;

    // game state
    ArrayList<ActionType> actionTypes ;
    ServerPlayer roosterCardHolder ;
    private Map<UUID, ServerCard> bioCornCards;
    private Integer bioCornCardsTotalValue = 0;
    private ArrayList<ServerCard> selectedBioCornCards;
    private Map<UUID, ServerCard> cornCards;
    private Integer cornCardsTotalValue = 0;
    private ArrayList<ServerCard> selectedCornCards;

    ArrayList<ServerCard> selectedCards;

    private Integer eggPoints = 0;


    public BasicBot(UUID gameId,UUID botID, String botName, boolean hahnKarte, GameSessionCallback callback) {
        super(botID,botName,hahnKarte);
        this.random = new Random();
        this.callback = callback;
        this.gameId = gameId;
        this.selectedCornCards = new ArrayList<>();
        this.selectedBioCornCards = new ArrayList<>();
    }

    // Method called to prompt the bot to take its turn
    public void takeTurn() throws RemoteException {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // Set a delay (in seconds) before taking a turn
        int delayInSeconds = 5; // Adjust this value as needed

        scheduler.schedule(() -> {
            try {
                // Perform the bot's actions here
                performActions();
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                scheduler.shutdown();
            }
        }, delayInSeconds, TimeUnit.SECONDS);
    }

    private void performActions() throws RemoteException {
        // Assess the current game state
        actionTypes = new ArrayList<>();

        // Get players
        players = callback.getPlayers(gameId);

        // get rooster card holder
        roosterCardHolder = callback.getRoosterCardHolder(gameId);

        // get all your bio corn cards
        bioCornCards = getCardHand().getBioCornCards();

        // calculate your bioCardPoints
        bioCornCardsTotalValue = calculateCardValue(bioCornCards, selectedBioCornCards);

        // get all your corn cards
        cornCards = getCardHand().getCornCards();

        // calculate your corn card points
        cornCardsTotalValue = calculateCardValue(cornCards, selectedBioCornCards);

        // check for possible actions
        shouldDrawCard();
        shouldGetEggs();
        shouldTakeRoosterCard();

        // choose a random action
        ActionType randomAction = getRandomAction();

        //perform Action
        play(randomAction);
    }

    private ActionType getRandomAction(){
        // Generate a random index
        random = new Random();
        int randomIndex = random.nextInt(actionTypes.size());

        // Use the random index to get a random Action
        return actionTypes.get(randomIndex);
    }

    public void play(ActionType actionType){
        switch (actionType){
            case DRAW_CARD -> callback.drawCard(getServerPlayerId(), gameId);
            case GET_EGGS_BIO -> callback.karteUmtauschen(gameId,getServerPlayerId(),eggPoints,selectedBioCornCards);
            case GET_EGGS_CORN -> callback.karteUmtauschen(gameId,getServerPlayerId(),eggPoints,selectedCornCards);
            case GET_EGGS_BIO_CORN -> callback.karteUmtauschen(gameId,getServerPlayerId(),eggPoints,selectedCards);
        }
    }

    // Basic logic to decide whether to draw a card or not
    private void shouldDrawCard() {
        // Simple decision for easy difficulty bot: always tries to draw a card
        if (hatHahnKarte() && getCardHand().size() <= 22){
           actionTypes.add(ActionType.DRAW_CARD);
        } else if (getCardHand().size() <= 23){
            actionTypes.add(ActionType.DRAW_CARD);
        }

    }

    private void shouldTakeRoosterCard(){
        if(!hatHahnKarte() && getPunkte() < roosterCardHolder.getPunkte()){
            actionTypes.add(ActionType.TAKE_ROOSTER_CARD);
        }
    }

    // Getting Eggs Methods
    private  void shouldGetEggs(){
        // Handle other cases if needed
        if ( bioCornCardsTotalValue > 5 ) {
            actionTypes.add(ActionType.GET_EGGS_BIO);
            eggPoints = calculateEggPoints(selectedBioCornCards);
            increasePointsBy(eggPoints);
        } else if (cornCardsTotalValue > 5) {
            actionTypes.add(ActionType.GET_EGGS_CORN);
            eggPoints = calculateEggPoints(selectedCornCards);
            increasePointsBy(eggPoints);
        } else if (bioCornCardsTotalValue + cornCardsTotalValue > 5){
            actionTypes.add(ActionType.GET_EGGS_BIO_CORN);
            selectedCards= new ArrayList<>();
            selectedCards.addAll(bioCornCards.values());
            selectedCards.addAll(cornCards.values());
            eggPoints = calculateEggPoints(selectedCards);
            increasePointsBy(eggPoints);
        }


    }

    private Integer calculateCardValue(Map<UUID, ServerCard> serverCards, ArrayList<ServerCard> selectedCards){
        Iterator<Map.Entry<UUID, ServerCard>> cards = serverCards.entrySet().iterator();
        int cardPoints = 0;
        while (cards.hasNext()){
            Map.Entry<UUID, ServerCard> entry = cards.next();
            ServerCard serverCard =entry.getValue();
            cardPoints += serverCard.getValue();
            selectedCards.add(serverCard);
        }
        return cardPoints;
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
    public void drawCard(ServerCard card ) throws RemoteException {
        // Interact with ServerTable to draw a card
        String cardType = card.getType();
        switch (cardType){
            case "Kuckuck" -> raisePunkte();
            case "Fuchs" -> drawnFoxCard();
            default -> addCard(card);
        }
    }

    private void drawnFoxCard(){
        actionTypes = new ArrayList<>(Arrays.asList(ActionType.STEAL_ALL_CARDS,ActionType.STEAL_ONE_CARD));
        // choose a random action
        ActionType randomAction = getRandomAction();

        // choose a random player
        ServerPlayer randomPlayer = chooseRandomPlayer();

        // get bio and corn cards of targeted player
        Map<UUID, ServerCard> bio = randomPlayer.getCardHand().getBioCornCards();
        Map<UUID, ServerCard> corn = randomPlayer.getCardHand().getCornCards();

        // get random player hand size
        Integer randomPlayerHandSize = randomPlayer.getCardCount();

        // Merge the maps into a new map
        Map<UUID, ServerCard> mergedMap = new ConcurrentHashMap<>(bio);
        mergedMap.putAll(corn);

        switch (randomAction){
            case STEAL_ONE_CARD :
                ArrayList<ServerCard> serverCards = chooseRandomCard(mergedMap);
                callback.stealOneCard(gameId,randomPlayer.getServerPlayerId(),serverCards,getServerPlayerId());
                addCard(serverCards.get(0));
                try {
                    endTurn();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
            case STEAL_ALL_CARDS:
                    if ((24 - getCardCount()) >= randomPlayerHandSize - 1){
                        callback.stealAllCards(gameId,randomPlayer.getServerPlayerId(),getServerPlayerId());
                    } else {
                        getEggs();
                    }
                break;
        }

    }

    public void stealingCardComplete(ArrayList<ServerCard> selectedCards){
        for (ServerCard serverCard : selectedCards){
            addCard(serverCard);
        }

        try {
            endTurn();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<ServerCard> chooseRandomCard(Map<UUID, ServerCard> cards ){
        ArrayList<ServerCard> cardList = new ArrayList<>();
        for (Map.Entry<UUID, ServerCard> entry : cards.entrySet()){
            ServerCard card = entry.getValue();
            cardList.add(card);
        }
        random = new Random();
        int randomIndex = random.nextInt(cardList.size());
        ServerCard randomCard = cardList.get(randomIndex);

        // Use the random index to get a random Action
        ArrayList<ServerCard> serverCards = new ArrayList<>();
        serverCards.add(randomCard);
        return serverCards;

    }

    private ServerPlayer chooseRandomPlayer(){
        ArrayList<ServerPlayer> playerList = new ArrayList<>();
        for (Map.Entry<UUID, ServerPlayer> entry : players.entrySet()){
            ServerPlayer player = entry.getValue();
            // check if the player is this bot
            if(!player.getServerPlayerId().equals(getServerPlayerId())){
                playerList.add(player);
            }
        }
        random = new Random();
        int randomIndex = random.nextInt(playerList.size());

        // Use the random index to get a random Action
        return playerList.get(randomIndex);
    }

    private void getEggs(){

    }

    // Method to end the bot's turn
    private void endTurn() throws RemoteException {
        // Interact with ServerTable to end the turn
        callback.endPlayerTurn(gameId);
    }


}

