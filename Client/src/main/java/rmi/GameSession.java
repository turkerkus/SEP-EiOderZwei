package rmi;

import sharedClasses.ClientUIUpdateListener;
import sharedClasses.ServerPlayer;
import sharedClasses.ServerTable;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameSession {
    private final UUID gameId;
    private final List<ServerPlayer> players = new ArrayList<>();
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
        this.gameId = gameId;
        this.maxNumOfPlayers = numOfHumanPlayersRequired;
        this.hostPlayerName = hostPlayerName;
        // TODO: decide on lobbyRoom Timer
        // this.remainingTimeMillis = (2 * 60 * 1000);
        setGameName(gameName);
        // Add the host player
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

    public List<ServerPlayer> getPlayers() {
        for (ServerPlayer player : players){
            System.out.println(player.toString());
        }
        return players;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void addPlayer(UUID clientID,ClientUIUpdateListener listener,String playerName) {
        // Check if the game is already started or the maximum number of players is
        // reached
        if (!gameStarted && players.size() < getMaxNumOfPlayers()) {
            players.add(new ServerPlayer(clientID, playerName, false, 0));
            clientListeners.put(clientID, listener);
            this.numberOfHumanPlayersPresent++;
        }

    }

    public void addBots(Integer bot) {
        for (int i = 1; i < bot + 1; i++) {
            String botName = generateFunnyBotName() + "_@Bot" + i;
            // Check if the game is already started or the maximum number of players is
            // reached
            ServerPlayer player = new ServerPlayer(UUID.randomUUID(), botName, false, 0);
            player.setBot(true);
            if (!isGameSessionReady && players.size() < getMaxNumOfPlayers()) {

                players.add(player);
            }
        }

    }

    public int getMaxNumOfPlayers() {
        // Define the maximum number of players for your game
        return this.maxNumOfPlayers;
    }

    public void startGameTable() {
        if (getNumberOfHumanPlayersPresent() < maxNumOfPlayers) {
            int botsToAdd = getMaxNumOfPlayers() - getNumberOfHumanPlayersPresent();
            addBots(botsToAdd);
        }
        if (BroadcastIsNotSent(BroadcastType.SWITCH_TO_TABLE)) {
            for (ServerPlayer player : players) {
                //the player is in the game
                player.einsteigen();
                try {
                    // check if the player is not a boot or not a hostPlayer
                    if (!player.isBot() || !checkStartTable(player.getServerPlayerName())) {
                        ClientUIUpdateListener listener = clientListeners.get(player.getServerPlayerId());
                        if (listener != null) {
                            listener.setNumOfPlayers(getMaxNumOfPlayers());
                            listener.updateUI("switchToTable");
                        }

                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            setBroadcastSent(BroadcastType.SWITCH_TO_TABLE, false);

        }
        isGameSessionReady = true;
    }

    public Integer getNumberOfHumanPlayersPresent() {
        return numberOfHumanPlayersPresent;
    }

    public boolean isFull() {
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

    public void startGame() {

        // Check if there are enough players to start the game
        if (!gameStarted && isGameSessionReady) {
            // Start the game
            gameStarted = true;
            setBroadcastSent(BroadcastType.START_GAME, true);

            // Implement your game logic here
            // Randomly choose the first player
            Random random = new Random();

            int firstPlayerIndex = random.nextInt(maxNumOfPlayers);

            // Give the firstPlayer the 'hahn' card
            setBroadcastSent(BroadcastType.Hahn_karte_Geben, true);
            hahnKarteGeben(firstPlayerIndex);


            // Create a serverTable and put the players on the serverTable
            this.serverTable = new ServerTable(players);

            // Set the firstPlayer as the active player in the serverTable
            serverTable.setActive(firstPlayerIndex);

            // Initialize the deck and shuffle it
            serverTable.intiNachZiehDeck();

            startPlayerTurn();

        }
    }
    public void hahnKarteGeben(Integer playerIdx){
        ServerPlayer serverPlayer = players.get(playerIdx);
        serverPlayer.setHahnKarte(true);
        if(BroadcastIsNotSent(BroadcastType.Hahn_karte_Geben)) {
            for (ServerPlayer player : players) {
                ClientUIUpdateListener listener = clientListeners.get(player.getServerPlayerId());
                if(listener != null){
                    // update the ui of the player
                    try {
                        listener.hahnKarteGeben(playerIdx);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            setBroadcastSent(BroadcastType.Hahn_karte_Geben,false);
        }
    }

    /**
     * Starts the turn for the current player, sets up a timer, and updates the UI accordingly.
     * Checks for end-game conditions and handles game over if necessary.
     */
    private void startPlayerTurn() {
        // Check for end-game condition
        if (gameLogic.findWinningPlayer(this.players, this.serverTable) != null) {
            ServerPlayer GameWinner = gameLogic.findWinningPlayer(this.players, this.serverTable);

            //TODO implement what will happen if the player wins
            System.out.print(GameWinner.getServerPlayerName() + "hat gewonnen");
            // Handle game over (declare winner, etc.)
            // TODO: SWITCH TO SCORE BOARD OR SHOW A DIALOG BOX
            //
            // This is just a placeholder
            return;
        }

        try {


            // set the current turn to true
            ServerPlayer currentPlayer = players.get(serverTable.getActive());
            Integer currentPlayerIdx = serverTable.getActive();
            if(!currentPlayer.isBot()){
                ClientUIUpdateListener currentPlayerListener = clientListeners.get(currentPlayer.getServerPlayerId());
                currentPlayerListener.setPlayerTurn(true);
                System.out.println(currentPlayer.toString() + " this is the current player");
            }

            // Broadcast to all players to startGame
            if(BroadcastIsNotSent(BroadcastType.START_GAME)){
                for(ServerPlayer player : players){
                    System.out.println("hat HahnKarte: " + player.hatHahnKarte());
                    // get the player listener
                    ClientUIUpdateListener listener = clientListeners.get(player.getServerPlayerId());
                    if(listener != null){
                        // update the current player index
                        listener.setCurrentPlayerIndex(currentPlayerIdx);
                        // update the ui of the player
                        listener.updateUI("startPlayerTurn");
                    }

                }

                setBroadcastSent(BroadcastType.START_GAME, false);
            }

            //Start the timer
            setBroadcastSent(BroadcastType.UPDATE_TIMER_LABEL, true);
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

                if (BroadcastIsNotSent(BroadcastType.UPDATE_TIMER_LABEL)) {
                    for (ServerPlayer player : players) {
                        try {
                            if (!player.isBot()) {
                                // Get the player listener
                                ClientUIUpdateListener listener = clientListeners.get(player.getServerPlayerId());
                                // Update the timerLeft for the player
                                listener.setTimeLeft(timeLeft);
                                // Send update to the client to update the UI
                                // (This part will depend on how your client-server communication is set up)
                            }
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
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
        System.out.println("acive = " + serverTable.getActive());
        setBroadcastSent(BroadcastType.START_GAME, true);
        startPlayerTurn(); // Start the next player's turn
    }






}
