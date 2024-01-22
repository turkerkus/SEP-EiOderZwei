package rmi;

import sharedClasses.ClientUIUpdateListener;
import sharedClasses.ServerCard;
import sharedClasses.ServerPlayer;
import sharedClasses.ServerTable;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class GameSession {
    private final UUID gameId;

    private boolean gameStarted = false;

    private String gameName;

    private boolean isGameSessionReady = false;
    private Integer numberOfHumanPlayersPresent = 0;

    private final Integer maxNumOfPlayers;

    private final String hostPlayerName;

    private ServerTable serverTable;
    private final GameLogic gameLogic = new GameLogic();

    private Integer numOfBotPlayers  = 0;
    private GameSessionCallback callback;

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

    private Map<UUID, ClientUIUpdateListener> clientListeners = new ConcurrentHashMap<>();
    private Map<BroadcastType, Boolean> broadcastStatus = new ConcurrentHashMap<>();
    private Timer timer;
    private volatile  Integer timeLeft; // Time left in seconds


    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    // Remaining time

    public GameSession(GameSessionCallback callback, UUID clientID, ClientUIUpdateListener listener, String gameName, UUID gameId, String hostPlayerName, Integer numOfHumanPlayersRequired) {
        this.callback = callback;
        this.serverTable = new ServerTable();
        this.gameId = gameId;
        this.maxNumOfPlayers = numOfHumanPlayersRequired;
        this.hostPlayerName = hostPlayerName;
        setGameName(gameName);

        addPlayer(clientID,listener,hostPlayerName);
        setBroadcastSent(BroadcastType.SWITCH_TO_TABLE, true);

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
        players.forEach((key, player) -> System.out.println(player.toString()));

        return players;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void addPlayer(UUID clientID,ClientUIUpdateListener listener,String playerName) {
        // Check if the game is already started or the maximum number of players is
        // reached
        Map<UUID, ServerPlayer> players = serverTable.getPlayers();
        if (!gameStarted && players.size() < getMaxNumOfPlayers()) {
            serverTable.addplayer(clientID,new ServerPlayer(clientID, playerName, false ));
            clientListeners.put(clientID, listener);
            this.numberOfHumanPlayersPresent++;
        }

    }

    public void addBots(Integer bot) {
        Map<UUID, ServerPlayer> players = serverTable.getPlayers();
        numOfBotPlayers++;
        for (int i = 1; i < bot + 1; i++) {
            String botName = generateFunnyBotName() + "_@Bot" + numOfBotPlayers;
            UUID botId = UUID.randomUUID();

            // Check if the game is already started or the maximum number of players is
            // reached
            ServerPlayer player = new ServerPlayer(botId, botName, false);
            player.setBot(true);
            if (!isGameSessionReady && players.size() < getMaxNumOfPlayers()) {

                serverTable.addplayer(botId,player);
            }
        }

    }

    public int getMaxNumOfPlayers() {
        // Define the maximum number of players for your game
        return this.maxNumOfPlayers;
    }

    public void startGameTable() throws RemoteException {
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
    }

    /**
     * Starts the turn for the current player, sets up a timer, and updates the UI accordingly.
     * Checks for end-game conditions and handles game over if necessary.
     */
    private void startPlayerTurn() {
        Map<UUID, ServerPlayer> players = serverTable.getPlayers();
        // Check for end-game condition
        if (gameLogic.findWinningPlayer(players, this.serverTable) != null) {
            ServerPlayer GameWinner = gameLogic.findWinningPlayer(players, this.serverTable);

            //TODO implement what will happen if the player wins
            System.out.print(GameWinner.getServerPlayerName() + "hat gewonnen");
            // Handle game over (declare winner, etc.)
            // TODO: SWITCH TO SCORE BOARD OR SHOW A DIALOG BOX
            //
            // This is just a placeholder
            return;
        }
        // broadCast StartGame
        try {
            setBroadcastSent(BroadcastType.UPDATE_TIMER_LABEL,true);
            broadcastSafeCommunication(BroadcastType.START_GAME);
            startTurnTimer(45);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Starts the turn timer with the specified duration in seconds.
     * @param durationInSeconds The duration of the turn timer in seconds.
     */
    public void startTurnTimer(int durationInSeconds) {
        timeLeft = durationInSeconds;

        // Stop any existing timer
        if (timer != null) {
            timer.cancel();
        }

        // Create a new Timer
        timer = new Timer();

        // Schedule a TimerTask
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Decrement timeLeft
                timeLeft--;
                try {
                    broadcastSafeCommunication(BroadcastType.UPDATE_TIMER_LABEL);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

                if (timeLeft <= 0) {
                    timer.cancel();
                    // Call method to handle end of timer
                    // This should also communicate with the client as needed
                    endPlayerTurn();
                }
            }
        }, 0, 1000); // Schedule the task to run every second
    }
    /**
     * Ends the turn for the specified player and performs end-of-turn actions.
     * Moves to the next player's turn.
     *
     */
    private void endPlayerTurn() {
        // Perform end-of-turn actions for the player

        // Move to the next player's turn
        serverTable.nextSpieler();
        System.out.println("active = " + serverTable.getActive());
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
        System.out.println("Player " + player.getServerPlayerName() + " has left the game and has been replaced by a bot.");
        numOfBotPlayers ++;
        if(clientListeners.isEmpty()){  // meaning there is no human player left, so you end the game
            timer.cancel();
            endGameSession();
        } else if(!player.isBot()){
            String botName = generateFunnyBotName() + "_@Bot" + numOfBotPlayers;
            // remove the player listener
            clientListeners.remove(disconnectedPlayerId);

            //swap the player with bot

            serverTable.swapPlayerWithBot(disconnectedPlayerId,botName);

            //Broadcast to the other players that the player has left the gameSession
            Map<UUID, ServerPlayer> players = serverTable.getPlayers();
            for (Map.Entry<UUID, ServerPlayer> entry : players.entrySet()) {
                ServerPlayer serverPlayer = entry.getValue();

                // Now use safeClientCommunication for each player
                safeClientCommunication(serverPlayer, listener -> {
                    try {
                        listener.playerLeftGameSession(disconnectedPlayerId,botName);
                    }catch (RemoteException e){
                        throw  new RuntimeException(e);
                    }
                });
            }
        }
        if(clientListeners.isEmpty()){  // meaning there is no human player left, so you end the game
            timer.cancel();
            endGameSession();
        }


    }

    /**
     * this method end the game session by removing it form the Game Map
     */
    public void endGameSession(){
        System.out.println("Game session with the id : "+ this.gameId+" has been ended");
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

        drawAndCheckCard(player);

        if (player.hatHahnKarte()) {
            drawAndCheckCard(player);
        }

        endPlayerTurn();
    }

    private void drawAndCheckCard(ServerPlayer player) throws RemoteException {
        serverTable.karteZiehen(player.getServerPlayerId());
        checkDrawnCard(player.getServerPlayerId());
        System.out.println(player.toString() + " has the ?  " + player.hatHahnKarte());
    }


    public void checkDrawnCard(UUID clientId) throws RemoteException {
        ServerCard card = serverTable.getDrawnCard();
        if (Objects.equals(serverTable.getDrawnCard().getType(), "Kuckuck")) {
            System.out.println("Kuckuck drawn. Sending info to player.");
            serverTable.karteAblegen(clientId, card);
            serverTable.getPlayer(clientId).raisePunkte();
            setBroadcastSent(BroadcastType.DRAWN_KUCKUCK_CARD, true);
            broadcastSafeCommunication(BroadcastType.DRAWN_KUCKUCK_CARD);
        }
        else if (Objects.equals(serverTable.getDrawnCard().getType(), "Fuchs")) {
            System.out.println("Fox drawn. Sending info to player.");
            serverTable.karteAblegen(clientId, card);
            setBroadcastSent(BroadcastType.DRAWN_FOX_CARD, true);
            broadcastSafeCommunication(BroadcastType.DRAWN_FOX_CARD);
        }
        else {
            System.out.println("Corn drawn, client can save that.");

        }
        setBroadcastSent(BroadcastType.HAS_DRAWN_A_CARD, true);
        broadcastSafeCommunication(BroadcastType.HAS_DRAWN_A_CARD);
    }

    public void discardCard(ServerCard card, UUID clientId) throws RemoteException {
        serverTable.karteAblegen(clientId, card);
        setBroadcastSent(BroadcastType.CARD_DISCARDED, true);
        broadcastSafeCommunication(BroadcastType.CARD_DISCARDED);
    }

    /**
     * Lets a player steal a rooster card as a round action.
     *
     * @param clientId The UUID of the current Client drawing the card.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void hahnKlauen(UUID clientId) throws RemoteException{
        setBroadcastSent(BroadcastType.CHANGE_ROOSTER_PLAYER, true);
        serverTable.setSpielerMitHahnKarte(clientId);
        setBroadcastSent(BroadcastType.CHANGE_ROOSTER_PLAYER, true);
        broadcastSafeCommunication(BroadcastType.CHANGE_ROOSTER_PLAYER);
        endPlayerTurn();
    }

    /**
     * Lets a player change card .
     *
     * @param clientId The UUID of the current Client drawing the card.
     * @throws RemoteException if a remote communication error occurs.
     */
    void karteUmtauschen(UUID clientId) throws RemoteException{

    }


    public ServerPlayer getRoosterPlayer(){
        UUID roosterPlayerId = serverTable.getSpielerMitHahnKarte();
        return serverTable.getPlayer(roosterPlayerId);
    }


    /**
     * Broadcasts a safe communication to all players with a specified UI update message.
     *
     * @param broadcastType   The type of broadcast action to perform.
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
                                    System.out.println("hahn");
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
                                case HAS_DRAWN_A_CARD:
                                    listener.hasDrawnACard(activePlayerId, serverTable.getDrawnCard());
                                    break;
                                case CHANGE_ROOSTER_PLAYER:
                                    listener.changeRoosterPlayer(serverTable.getAlteSpielerMitHahnKarte(),serverTable.getSpielerMitHahnKarte());
                                    break;
                                case DRAWN_KUCKUCK_CARD:
                                    listener.drawnKuckuckCard(serverTable.getActiveSpielerID());
                                    break;
                                case DRAWN_FOX_CARD:
                                    listener.drawnFoxCard(serverTable.getActiveSpielerID());
                                    break;
                                case CARD_DISCARDED:
                                    listener.cardDiscarded(serverTable.getActiveSpielerID(), serverTable.getDiscarded());
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




}
