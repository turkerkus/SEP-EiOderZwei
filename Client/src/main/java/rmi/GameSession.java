package rmi;

import sharedClasses.ClientUIUpdateListener;
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

    public GameSession(UUID clientID, ClientUIUpdateListener listener, String gameName, UUID gameId, String hostPlayerName, Integer numOfHumanPlayersRequired) {
        this.serverTable = new ServerTable();
        this.gameId = gameId;
        this.maxNumOfPlayers = numOfHumanPlayersRequired;
        this.hostPlayerName = hostPlayerName;
        setGameName(gameName);

        addPlayer(clientID,listener,hostPlayerName);
        setBroadcastSent(BroadcastType.SWITCH_TO_TABLE, true);

    }
    public void setBroadcastSent(BroadcastType type, boolean sent) {
        broadcastStatus.put(type, sent);
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
        players.forEach((key, player) -> {
            System.out.println(player.toString());
        });

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
            serverTable.addplayer(clientID,new ServerPlayer(clientID, playerName, false, 0));
            clientListeners.put(clientID, listener);
            this.numberOfHumanPlayersPresent++;
        }

    }

    public void addBots(Integer bot) {
        Map<UUID, ServerPlayer> players = serverTable.getPlayers();
        for (int i = 1; i < bot + 1; i++) {
            String botName = generateFunnyBotName() + "_@Bot" + i;
            UUID botId = UUID.randomUUID();

            // Check if the game is already started or the maximum number of players is
            // reached
            ServerPlayer player = new ServerPlayer(botId, botName, false, 0);
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
            broadcastSafeCommunication(BroadcastType.START_GAME);
            startTurnTimer(10);
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
        setBroadcastSent(BroadcastType.START_GAME, true);
        startPlayerTurn(); // Start the next player's turn
    }


    /**
     * Safely performs RMI communication with a client if the player is not a bot.
     *
     * @param player The server player associated with the client.
     * @param action The action to perform on the client's UI update listener.
     */
    private void safeClientCommunication(ServerPlayer player, Consumer<ClientUIUpdateListener> action) throws RemoteException {
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
     * @param player The disconnected player.
     * @throws RemoteException If a remote communication error occurs.
     */
    private void handleDisconnectedClient(ServerPlayer player) throws RemoteException {
        System.out.println("Player " + player.getServerPlayerName() + " has left the game and has been replaced by a bot.");
    }

    /**
     * Broadcasts a safe communication to all players with a specified UI update message.
     *
     * @param broadcastType   The type of broadcast action to perform.
     * @throws RemoteException If a remote communication error occurs.
     */
    private void broadcastSafeCommunication(BroadcastType broadcastType) throws RemoteException {
        Map<UUID, ServerPlayer> players = serverTable.getPlayers();
        for (Map.Entry<UUID, ServerPlayer> entry : players.entrySet()) {
            ServerPlayer player = entry.getValue();

            // Now use safeClientCommunication for each player
            safeClientCommunication(player, listener -> {
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
                            UUID activePlayerId = serverTable.getActiveSpielerID();
                            listener.setCurrentPlayerID(activePlayerId);
                            listener.updateUI("startPlayerTurn");
                            break;
                        case UPDATE_TIMER_LABEL:
                            listener.setTimeLeft(timeLeft);
                            break;

                        // Add cases for other BroadcastTypes if needed
                    }


                } catch (RemoteException e) {
                    try {
                        handleDisconnectedClient(player);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }
        // Update the broadcast status after all players have been handled
        setBroadcastSent(broadcastType, false);
    }




}
