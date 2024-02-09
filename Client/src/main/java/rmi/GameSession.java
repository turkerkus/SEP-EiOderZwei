package rmi;

import sharedClasses.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class GameSession implements Serializable {
    private final UUID gameId;
    private final Integer maxNumOfPlayers;
    private final String hostPlayerName;
    private final GameLogic gameLogic = new GameLogic();
    private final String[] FUNNY_BOT_NAMES = {
            "Bloop",
            "Zippy",
            "Funky",
            "Squid",
            "Gizmo",
            "Scoot",
            "Bongo",
            "Pinky",
            "Waldo",
            "Zorro"
            // Add more funny names as needed
    };
    String botLevel = "Easy";
    ArrayList<ServerCard> stollenCards;
    UUID kuckuckPlayer;
    private boolean gameStarted = false;
    private String gameName;
    private String currentChatMessage;
    private boolean isGameSessionReady = false;
    private Integer numberOfHumanPlayersPresent = 0;
    private ServerTable serverTable;
    private Integer numOfBotPlayers = 0;
    private GameSessionCallback callback;
    private Map<UUID, ClientUIUpdateListener> clientListeners = new ConcurrentHashMap<>();
    private Map<BroadcastType, Boolean> broadcastStatus = new ConcurrentHashMap<>();
    private CustomTimer timer;
    private volatile Integer timeLeft; // Time left in seconds

    // Remaining time
    private UUID chatSenderId;

    private UUID eggLayer;
    private UUID thiefID;
    private ServerCard foxCard;

    public boolean isActive() {
        return active;
    }

    private boolean active;
    private String joinPlayerName;
    private ClientUIUpdateListener hostPlayerListener;

    public GameSession(GameSessionCallback callback, UUID clientID, ClientUIUpdateListener listener, String gameName, UUID gameId, String hostPlayerName, Integer numOfHumanPlayersRequired, String botLevel) {
        this.callback = callback;
        this.serverTable = new ServerTable();
        this.gameId = gameId;
        this.maxNumOfPlayers = numOfHumanPlayersRequired;
        this.hostPlayerName = hostPlayerName;
        this.joinPlayerName = hostPlayerName;
        this.hostPlayerListener = listener;
        setGameName(gameName);
        this.botLevel = botLevel;
        System.out.println(botLevel + " is chosen and gameName is "  + gameName);


        addPlayer(clientID, listener, hostPlayerName);
        setBroadcastSent(BroadcastType.SWITCH_TO_TABLE, true);

    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setCurrentChatMessage(String message) {
        this.currentChatMessage = message;
    }

    public void sendChatMessage(UUID senderId, String message) throws RemoteException {
        this.chatSenderId = senderId;
        setCurrentChatMessage(message);
        setBroadcastSent(BroadcastType.CHAT, true);
        broadcastSafeCommunication(BroadcastType.CHAT);
        setCurrentChatMessage(null);
    }

    public void setBroadcastSent(BroadcastType type, boolean notSent) {
        broadcastStatus.put(type, notSent);
    }

    public boolean BroadcastIsNotSent(BroadcastType type) {
        return broadcastStatus.getOrDefault(type, false);
    }

    public String generateFunnyBotName() {
        Random random = new Random();
        int index = random.nextInt(FUNNY_BOT_NAMES.length);
        return FUNNY_BOT_NAMES[index];
    }

    public UUID getGameId() {
        return gameId;
    }

    public Map<UUID, ServerPlayer> getServerPlayers() {
        Map<UUID, ServerPlayer> players = serverTable.getPlayers();
        return players;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void addPlayer(UUID clientID, ClientUIUpdateListener listener, String playerName) {
        // Check if the game is already started or the maximum number of players is
        // reached
        Map<UUID, ServerPlayer> players = serverTable.getPlayers();
        if (!gameStarted && players.size() < getMaxNumOfPlayers()) {
            serverTable.addplayer(clientID, new ServerPlayer(clientID, playerName, false));
            clientListeners.put(clientID, listener);
            joinPlayerName = playerName;
            this.numberOfHumanPlayersPresent++;
            if(playerName != hostPlayerName) updateLobbyRoomPlayerList(listener);
            setBroadcastSent(BroadcastType.PLAYER_JOINED_GAME, true);
            try {
                broadcastSafeCommunication(BroadcastType.PLAYER_JOINED_GAME);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }


        }

    }
    public void updateLobbyRoomPlayerList(ClientUIUpdateListener listener){
        int playerCount = 1;
        for (UUID playerId : serverTable.getPlayerIdList()){
            ServerPlayer player = serverTable.getPlayer(playerId);
            String playerName = player.getServerPlayerName();
            if(listener != null && playerName != joinPlayerName) {
                try {
                    listener.addPlayerToLobby(playerName,playerCount);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            playerCount++;

        }
    }

    public void addBots(Integer numOfBots) {
        Map<UUID, ServerPlayer> players = serverTable.getPlayers();
        for (int i = 0; i < numOfBots; i++) {
            String botName = generateFunnyBotName() + "_@Bot" + (i + 1);
            UUID botId = UUID.randomUUID();

            // Check if the game is already started or the maximum number of players is reached
            if (!isGameSessionReady && players.size() < getMaxNumOfPlayers()) {
                // Create a new BasicBot
                switch (botLevel) {
                    case "Easy":
                        BasicBot bot = new BasicBot(gameId, botId, botName, false, callback);

                        // Add the bot to the server table
                        serverTable.addplayer(botId, bot);
                        break;
                    case "Hard":
                        HardBot hardBot = new HardBot(gameId, botId, botName, false, callback);
                        // Add the bot to the server table
                        serverTable.addplayer(botId, hardBot);
                        break;
                }


            }
        }
    }

    public int getMaxNumOfPlayers() {
        // Define the maximum number of players for your game
        return this.maxNumOfPlayers;
    }

    public void startGameTable() throws RemoteException {
        this.active = true;
        if (getNumberOfHumanPlayersPresent() < maxNumOfPlayers) {
            int botsToAdd = getMaxNumOfPlayers() - getNumberOfHumanPlayersPresent();
            addBots(botsToAdd);
        }
        if (BroadcastIsNotSent(BroadcastType.SWITCH_TO_TABLE)) {
            broadcastSafeCommunication(BroadcastType.SWITCH_TO_TABLE);
        }
        isGameSessionReady = true;
    }

    public Integer getNumberOfHumanPlayersPresent() {
        return numberOfHumanPlayersPresent;
    }

    public boolean isFull() {
        Map<UUID, ServerPlayer> players = serverTable.getPlayers();
        System.out.println(players.size());
        return players.size() == getMaxNumOfPlayers();
    }

    public boolean checkStartTable(String playerName) {
        return playerName.equals(hostPlayerName);
    }

    public boolean isGameSessionReady() {
        return isGameSessionReady;
    }

    /**
     * Sets up the start button action to begin the game if it hasn't started already.
     * Randomly selects the first player, assigns the 'hahn' card to them, initializes the serverTable,
     * sets the first player as the active player, initializes and shuffles the deck, and starts the first player's turn.
     */

    public void startGame() throws RemoteException {

        // Check if there are enough players to start the game
        if (!gameStarted && isGameSessionReady) {
            // Start the game
            gameStarted = true;
            setBroadcastSent(BroadcastType.START_GAME, true);

            // Implement your game logic here
            // Randomly choose the first player
            Random random = new Random();

            int firstPlayerIndex = random.nextInt(maxNumOfPlayers);
            UUID playerId = serverTable.getPlayerId(firstPlayerIndex);

            // Give the firstPlayer the 'hahn' card
            setBroadcastSent(BroadcastType.Hahn_karte_Geben, true);
            serverTable.setSpielerMitHahnKarte(playerId);
            hahnKarteGeben();


            // Set the firstPlayer as the active player in the serverTable
            serverTable.setActive(firstPlayerIndex);

            // Initialize the deck and shuffle it
            serverTable.intiNachZiehDeck();

            startPlayerTurn();

        }
    }

    public void hahnKarteGeben() throws RemoteException {
        broadcastSafeCommunication(BroadcastType.Hahn_karte_Geben);
        UUID playerID = serverTable.getSpielerMitHahnKarte();
        if (serverTable.getPlayer(playerID).isBot()) {
            handleBotAction(serverTable.getSpielerMitHahnKarte(), bot -> bot.setHahnKarte(true));
        }
    }

    /**
     * Starts the turn for the current player, sets up a timer, and updates the UI accordingly.
     * Checks for end-game conditions and handles game over if necessary.
     */
    private void startPlayerTurn() {
        System.out.println("Player turn started");
        Map<UUID, ServerPlayer> players = serverTable.getPlayers();
        // Check for end-game condition
        ServerPlayer GameWinner = gameLogic.findWinningPlayer(players, this.serverTable);
        if (GameWinner != null) {


            //TODO implement what will happen if the player wins
            System.out.print(GameWinner.getServerPlayerName() + "hat gewonnen");
            timer.cancel();

            setBroadcastSent(BroadcastType.SWITCH_TO_RESULTS, true);
            try {
                broadcastSafeCommunication(BroadcastType.SWITCH_TO_RESULTS);
                endGameSession(null);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

            return;
        }


        // broadCast StartGame
        try {
            setBroadcastSent(BroadcastType.UPDATE_TIMER_LABEL, true);
            broadcastSafeCommunication(BroadcastType.START_GAME);
            startTurnTimer(45);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        if (serverTable.getActiveSpieler().isBot()) {
            handleBotAction(serverTable.getActiveSpielerID(), bot -> {
                bot.takeTurn();
            });
        }
    }

    /**
     * Starts the turn timer with the specified duration in seconds.
     *
     * @param durationInSeconds The duration of the turn timer in seconds.
     */

    public void startTurnTimer(int durationInSeconds) {

        timeLeft = durationInSeconds;

        if (timer != null) {
            timer.cancel();
        }

        timer = new CustomTimer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                timeLeft--;


                try {
                    broadcastSafeCommunication(BroadcastType.UPDATE_TIMER_LABEL);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                if (timeLeft <= 0) {
                    timer.cancel();
                    endPlayerTurn();  // Implement this method as needed
                    //TODO REMOVE THIS PRINT LINE
                    System.out.println("end player turn from gameSession timer");
                }

                // Optionally, broadcast the updated timeLeft to clients

            }
        }, 0, 1000);
    }

    /**
     * Ends the turn for the specified player and performs end-of-turn actions.
     * Moves to the next player's turn.
     */
    public void endPlayerTurn() {
        System.out.println("player turn ended");
        // Perform end-of-turn actions for the player
        serverTable.getActiveSpieler().setHasTakenTurn(false);
        // Move to the next player's turn
        serverTable.nextSpieler();
        //setBroadcastSent(BroadcastType.START_GAME, true);
        startPlayerTurn(); // Start the next player's turn
    }

    /**
     * Safely performs RMI communication with a client if the player is not a bot.
     *
     * @param player The server player associated with the client.
     * @param action The action to perform on the client's UI update listener.
     */
    private void safeClientCommunication(ServerPlayer player, Consumer<ClientUIUpdateListener> action) {
        if (!player.isBot()) {  // Check if the player is not a bot
            // Retrieve the client's UI update listener from the map
            ClientUIUpdateListener listener = clientListeners.get(player.getServerPlayerId());
            if (listener != null) {
                action.accept(listener);  // Perform the RMI communication on the listener
            }
        }
    }

    /**
     * Handles a disconnected client by printing a message.
     *
     * @param disconnectedPlayerId The disconnected player id.
     * @throws RemoteException If a remote communication error occurs.
     */
    public void handleDisconnectedClient(UUID disconnectedPlayerId) throws RemoteException {
        ServerPlayer player = serverTable.getPlayer(disconnectedPlayerId);

        numOfBotPlayers++;
        if (clientListeners.isEmpty()) {  // meaning there is no human player left, so you end the game
            timer.cancel();
            endGameSession(null);
        } else if (player!= null &&!player.isBot()) {
            String botName = generateFunnyBotName() + "_@Bot" + numOfBotPlayers;
            // remove the player listener
            clientListeners.remove(disconnectedPlayerId);

            //swap the player with bot
            if (isGameSessionReady ){
                serverTable.swapPlayerWithBot(disconnectedPlayerId, gameId, botName, callback, botLevel);
                System.out.println("Player " + player.getServerPlayerName() + " has left the game and has been replaced by a bot.");
            }
            // then player might be in the lobby Room
            else{
                serverTable.removePLayer(disconnectedPlayerId);
                System.out.println("Player " + player.getServerPlayerName() + " has left the LobbyRoom");
                numberOfHumanPlayersPresent--;

            }

            //Broadcast to the other players that the player has left the gameSession
            Map<UUID, ServerPlayer> players = serverTable.getPlayers();
            for (Map.Entry<UUID, ServerPlayer> entry : players.entrySet()) {
                ServerPlayer serverPlayer = entry.getValue();

                // Now use safeClientCommunication for each player
                safeClientCommunication(serverPlayer, listener -> {
                    try {
                        if(isGameSessionReady) listener.playerLeftGameSession(disconnectedPlayerId, botName);
                        else if(player.getServerPlayerName().equals(hostPlayerName)){
                            listener.playerLeftGameSession(disconnectedPlayerId, "isHostPlayer");
                        } else listener.playerLeftGameSession(disconnectedPlayerId, "isNotHostPlayer");
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            if(player.hasTakenTurn()) endPlayerTurn();
            else if(isGameStarted()){
                handleBotAction(disconnectedPlayerId,bot -> bot.takeTurn());
            }
        }
        if (clientListeners.isEmpty() ) {  // meaning there is no human player left, so you end the game

            if(timer != null && gameStarted){
                timer.cancel();
            }
            endGameSession(hostPlayerListener);
        }


    }

    /**
     * this method end the game session by removing it form the Game Map
     */
    public void endGameSession(ClientUIUpdateListener hostPlayerListener) {
        this.active = false;
        System.out.println("Game session with the id : " + this.gameId + " has been ended");

        try {
            if(hostPlayerListener != null) callback.updateGameSessionList(hostPlayerListener);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        callback.endGameSession(this.gameId);
    }

    /**
     * Lets a player draw a card as a round action.
     *
     * @param clientId The UUID of the current Client drawing the card.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void drawCard(UUID clientId) throws RemoteException {
        ServerPlayer player = serverTable.getPlayer(clientId);
        boolean hasRoosterCard = player.hatHahnKarte();
        boolean hasDrawnFoxCard = false;

        if (player.hasTakenTurn()) {
            endPlayerTurn();
        } else {
            // Draw the first card
            drawAndCheckCard(player);
            // Check if a fox card was drawn
            hasDrawnFoxCard = serverTable.getDrawnCard().getType().equals("Fuchs");

            // If the player has a rooster card, draw a second card
            if (hasRoosterCard) {
                System.out.println(player.getServerPlayerName() + " has the rooster card");
                drawAndCheckCard(player);
                // Check again if a fox card was drawn as the second card
                hasDrawnFoxCard |= serverTable.getDrawnCard().getType().equals("Fuchs");
            }

            // If no fox card was drawn or the player is not a bot, end the player's turn
            if (!hasDrawnFoxCard) {
                endPlayerTurn();
            }
        }
    }

    private void drawAndCheckCard(ServerPlayer player) throws RemoteException {
        System.out.println(player.getServerPlayerName() + " is drawing a card");
        serverTable.karteZiehen(player.getServerPlayerId());
        player.setHasTakenTurn(true);
        checkDrawnCard(player.getServerPlayerId());

    }

    public void checkDrawnCard(UUID clientId) throws RemoteException {
        ServerCard card = serverTable.getDrawnCard();
        ServerPlayer player = serverTable.getPlayer(clientId);

        // if player is not a bot
        if (Objects.equals(serverTable.getDrawnCard().getType(), "Kuckuck")) {
            kuckuckPlayer = clientId;
            System.out.println("Kuckuck drawn. Sending info to player.");
            serverTable.karteAblegen(clientId, card);
            serverTable.getPlayer(clientId).raisePunkte();
            setBroadcastSent(BroadcastType.DRAWN_KUCKUCK_CARD, true);
            broadcastSafeCommunication(BroadcastType.DRAWN_KUCKUCK_CARD);

        } else if (Objects.equals(serverTable.getDrawnCard().getType(), "Fuchs")) {
            thiefID = clientId;
            System.out.println("Fox drawn. Sending info to player.");
            serverTable.karteAblegen(clientId, card);
            setBroadcastSent(BroadcastType.DRAWN_FOX_CARD, true);
            broadcastSafeCommunication(BroadcastType.DRAWN_FOX_CARD);
        } else {
            System.out.println("Corn drawn, client can save that.");

        }
        setBroadcastSent(BroadcastType.HAS_DRAWN_A_CARD, true);
        broadcastSafeCommunication(BroadcastType.HAS_DRAWN_A_CARD);
        // if player is a bot send this too
        if (player.isBot()) {
            handleBotAction(clientId, bot -> {
                try {
                    bot.drawCard(card);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    /**
     * Lets a player steal a rooster card as a round action.
     *
     * @param clientId The UUID of the current Client drawing the card.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void hahnKlauen(UUID clientId) throws RemoteException {
        ServerPlayer player = serverTable.getPlayer(clientId);


        if (player.hasTakenTurn()) {
            endPlayerTurn();
        } else {
            setBroadcastSent(BroadcastType.CHANGE_ROOSTER_PLAYER, true);
            serverTable.setSpielerMitHahnKarte(clientId);
            setBroadcastSent(BroadcastType.CHANGE_ROOSTER_PLAYER, true);
            broadcastSafeCommunication(BroadcastType.CHANGE_ROOSTER_PLAYER);
            ServerPlayer oldRoosterPlayer = serverTable.getPlayer(serverTable.getAlteSpielerMitHahnKarte());
            if (player.isBot() || oldRoosterPlayer.isBot()) {
                if (player.isBot()) {
                    handleBotAction(clientId, bot -> bot.setHahnKarte(true));

                } else {
                    // if bot is the old rooster Player
                    handleBotAction(serverTable.getAlteSpielerMitHahnKarte(), bot -> bot.setHahnKarte(false));
                }

            }
            endPlayerTurn();
        }

    }

    /**
     * Lets a player change card .
     *
     * @param clientId The UUID of the current Client drawing the card.
     * @throws RemoteException if a remote communication error occurs.
     */
    void karteUmtauschen(UUID clientId, Integer eggPoints, ArrayList<ServerCard> selectedCards) throws RemoteException {

        ServerPlayer player = serverTable.getPlayer(clientId);
        if (player.hasTakenTurn()) endPlayerTurn();
        else {
            eggLayer = clientId;
            // discard all the cards on the serverTable
            for (ServerCard serverCard : selectedCards) {
                serverTable.karteAblegen(clientId, serverCard);
            }

            player.increasePointsBy(eggPoints);
            serverTable.setEggPoints(eggPoints);
            serverTable.setDiscardedSelectedCards(selectedCards);
            System.out.println(serverTable.getPlayer(clientId).getServerPlayerName() + "  is changing the following cards for egg Points!");

            //TODO REMOVE THIS
            for (ServerCard element : selectedCards) {
                System.out.println(element.toString());
            }
            System.out.println("egg points: " + serverTable.getEggPoints());

            setBroadcastSent(BroadcastType.CARD_DISCARDED, true);
            broadcastSafeCommunication(BroadcastType.CARD_DISCARDED);
            endPlayerTurn();
        }


    }

    public ServerPlayer getRoosterPlayer() {
        UUID roosterPlayerId = serverTable.getSpielerMitHahnKarte();
        return serverTable.getPlayer(roosterPlayerId);
    }

    public void stealOneCard(UUID targetId, ArrayList<ServerCard> selectedCards, UUID clientId) {
        ServerPlayer player = serverTable.getPlayer(clientId);
        ServerPlayer targetedPlayer = serverTable.getPlayer(targetId);

        System.out.println(player.getServerPlayerName() + " is stealing a card below from " + targetedPlayer.getServerPlayerName() + "!");

        thiefID = clientId;
        serverTable.setStolenCard(selectedCards.get(0));
        serverTable.setTarget(targetId);
        targetedPlayer.remove(serverTable.getStolenCard().getServeCardID(), serverTable.getStolenCard().getType());
        player.addCard(selectedCards.get(0));
        setBroadcastSent(BroadcastType.ONE_CARD_STOLEN, true);
        try {
            broadcastSafeCommunication(BroadcastType.ONE_CARD_STOLEN);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        endPlayerTurn();

    }

    // remove the fox card if the one stealing the card was not able to a card because the
    // opponent has no cards
    public void removeFoxCard(ServerCard foxCard) {
        this.foxCard = foxCard;
        setBroadcastSent(BroadcastType.REMOVE_FOX_CARD, true);
        try {
            broadcastSafeCommunication(BroadcastType.REMOVE_FOX_CARD);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void stealAllCards(UUID targetId, UUID clientId) {
        serverTable.setTarget(targetId);
        thiefID = clientId;
        setBroadcastSent(BroadcastType.ALL_CARDS_STOLEN, true);
        try {
            broadcastSafeCommunication(BroadcastType.ALL_CARDS_STOLEN);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        ServerPlayer targetedPlayer = serverTable.getPlayer(targetId);
        ServerPlayer player = serverTable.getPlayer(clientId);
        // if player is a bot send this too
        if (targetedPlayer.isBot()) {
            System.out.println(player.getServerPlayerName() + " stealing all " + targetedPlayer.getServerPlayerName() + " cards except for one");
            handleBotAction(targetId, bot -> bot.stealAllCards(clientId));
        }


    }

    public void stealingInProgress(UUID playerId, UUID targetId, ArrayList<ServerCard> selectedCards) {
        System.out.println("request to steal all cards accepted");
        serverTable.setTarget(targetId);
        thiefID = playerId;
        stollenCards = selectedCards;

        // remove the cards from the targets hand
        removeMultipleCards(targetId, stollenCards);
        for (ServerCard serverCard : selectedCards) {
            serverTable.getPlayer(thiefID).addCard(serverCard);
        }
        setBroadcastSent(BroadcastType.STEALING_CARD_COMPLETED, true);
        try {
            broadcastSafeCommunication(BroadcastType.STEALING_CARD_COMPLETED);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        ServerPlayer player = serverTable.getPlayer(playerId);
        // if player is a bot send this too
        if (player.isBot()) {
            System.out.println("adding stolen cards to bot");
            handleBotAction(playerId, bot -> bot.stealingCardComplete(selectedCards));
        }

    }


    public void removeMultipleCards(UUID playerID, ArrayList<ServerCard> discardedCards) {
        // Executor to schedule card removal with delays
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        // Schedule the card removal with a delay
        int delay = 0; // Initial delay
        for (ServerCard card : discardedCards) { // No need to create a new ArrayList here
            executorService.schedule(() ->
                    serverTable.getPlayer(playerID).remove(card.getServeCardID(), card.getType()), delay, TimeUnit.SECONDS);
            delay += 1; // Increment delay for the next card
        }

    }


    /**
     * Broadcasts a safe communication to all players with a specified UI update message.
     *
     * @param broadcastType The type of broadcast action to perform.
     * @throws RemoteException If a remote communication error occurs.
     */
    private void broadcastSafeCommunication(BroadcastType broadcastType) throws RemoteException {
        Map<UUID, ServerPlayer> players = serverTable.getPlayers();
        UUID activePlayerId = serverTable.getActiveSpielerID();
        for (Map.Entry<UUID, ServerPlayer> entry : players.entrySet()) {
            UUID playerID = entry.getKey();
            ServerPlayer player = entry.getValue();
            // Now use safeClientCommunication for each player
            safeClientCommunication(player, listener -> {
                // Implementing a retry mechanism before deciding to remove a client due to the client
                // disconnected from the server or leaves game  session
                boolean success = false;
                final int maxRetries = 3;
                for (int attempt = 0; attempt < maxRetries && !success; attempt++) {
                    try {
                        // Perform the actions based on the BroadcastType
                        switch (broadcastType) {
                            case SWITCH_TO_TABLE:
                                // Perform specific action for this broadcast type
                                player.einsteigen();
                                listener.setNumOfPlayers(getMaxNumOfPlayers());
                                listener.updateUI("switchToTable");
                                break;
                            case Hahn_karte_Geben:
                                UUID roosterCardHolder = serverTable.getSpielerMitHahnKarte();
                                listener.hahnKarteGeben(roosterCardHolder);
                                break;
                            case START_GAME:
                                // set the current turn to true
                                listener.setCurrentPlayerID(activePlayerId);
                                listener.updateUI("startPlayerTurn");
                                break;
                            case UPDATE_TIMER_LABEL:
                                listener.setTimeLeft(timeLeft);
                                break;
                            case PLAYER_JOINED_GAME:
                                listener.addPlayerToLobby(joinPlayerName,numberOfHumanPlayersPresent);
                                break;
                            case HAS_DRAWN_A_CARD:
                                listener.hasDrawnACard(activePlayerId, serverTable.getDrawnCard());
                                break;
                            case CHANGE_ROOSTER_PLAYER:
                                listener.changeRoosterPlayer(serverTable.getAlteSpielerMitHahnKarte(), serverTable.getSpielerMitHahnKarte());
                                break;
                            case DRAWN_KUCKUCK_CARD:
                                listener.drawnKuckuckCard(kuckuckPlayer, serverTable.getDrawnCard());
                                break;
                            case DRAWN_FOX_CARD:
                                listener.drawnFoxCard(thiefID, serverTable.getDrawnCard());
                                break;
                            case SWITCH_TO_RESULTS:
                                listener.switchToResultTable(gameLogic.getWinner());
                                break;
                            case CARD_DISCARDED:
                                listener.cardDiscarded(eggLayer, serverTable.getDiscarded(), serverTable.getEggPoints(), serverTable.getDiscardedSelectedCards());
                                break;
                            case ONE_CARD_STOLEN:
                                listener.oneCardStolen(serverTable.getTarget(), serverTable.getStolenCard(), thiefID);
                                break;
                            case REMOVE_FOX_CARD:
                                listener.removeFoxCard(foxCard);
                                break;
                            case ALL_CARDS_STOLEN:
                                listener.allCardsStolen(serverTable.getTarget(), thiefID);
                                break;
                            case STEALING_CARD_COMPLETED:
                                listener.stealingCardCompleted(serverTable.getTarget(), thiefID, stollenCards);
                                break;
                            case CHAT:
                                if (currentChatMessage != null) {
                                    listener.updateChat(currentChatMessage, chatSenderId);
                                }
                                break;
                            // Add cases for other BroadcastTypes if needed
                        }
                        success = true;
                    } catch (RemoteException e) {
                        if (attempt == maxRetries - 1) {
                            // Log retry attempt here
                            try {
                                Thread.sleep(1000);  // Wait for a second before retrying
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();  // Restore the interrupted status
                            }
                        } else {
                            // If all retries fail, handle as a disconnected client
                            try {
                                handleDisconnectedClient(playerID);
                            } catch (RemoteException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                }
            });

        }
        // Update the broadcast status after all players have been handled
        setBroadcastSent(broadcastType, false);
    }

    private void handleBotAction(UUID playerId, BotAction action) {
        ServerPlayer player = serverTable.getPlayer(playerId);
        if (player.isBot()) {

            switch (botLevel) {
                case "Easy":
                    BasicBot bot = (BasicBot) player;
                    action.performAction(bot);
                    break;
                case "Hard":
                    HardBot hardBot = (HardBot) player;
                    action.performAction(hardBot);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown bot difficulty");
            }


        }
    }

    public List<UUID> getPlayerIDList() {
        return serverTable.getPlayerIdList();
    }

    @FunctionalInterface
    interface BotAction {
        void performAction(BasicBot bot);
    }

}
