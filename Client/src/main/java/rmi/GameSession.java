package rmi;

import sharedClasses.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameSession {
    private final UUID gameId;
    private final List<ServerPlayer> players = new ArrayList<>();
    private boolean gameStarted = false;

    private String gameName;

    private boolean isGameReady = false;
    private Integer numberOfHumanPlayersPresent = 0;

    private final Integer maxNumOfPlayers;

    private final String hostPlayerName;

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

    // TODO: decide on lobbyRoom Timer
    /*
     * public long getRemainingTimeMillis() {
     * synchronized (lock) {
     * return remainingTimeMillis;
     * }
     * }
     * 
     * //TODO: decide on lobbyRoom Timer
     * //private volatile long remainingTimeMillis = -1;
     * private final Object lock = new Object();
     * private final ScheduledExecutorService scheduler =
     * Executors.newScheduledThreadPool(1);
     * 
     * 
     */

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getHostPlayerName() {
        return hostPlayerName;
    }

    // Remaining time

    public GameSession(UUID clientID, String gameName, UUID gameId, String hostPlayerName, Integer numOfHumanPlayersRequired) {
        this.gameId = gameId;
        this.maxNumOfPlayers = numOfHumanPlayersRequired;
        this.hostPlayerName = hostPlayerName;
        // TODO: decide on lobbyRoom Timer
        // this.remainingTimeMillis = (2 * 60 * 1000);
        setGameName(gameName);
        // Add the host player
        addPlayer(clientID,hostPlayerName);

        /*
         * startCountdown();
         * if (numOfHumanPlayersRequired == 1){
         * remainingTimeMillis = 10000;
         * addBots(5);
         * }
         * 
         */
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
        return players;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void addPlayer(UUID clientID,String playerName) {
        // Check if the game is already started or the maximum number of players is
        // reached
        if (!gameStarted && players.size() < getMaxNumOfPlayers()) {
            players.add(new ServerPlayer(clientID, playerName, false, 0));
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
            if (!isGameReady && players.size() < getMaxNumOfPlayers()) {

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
        isGameReady = true;
    }

    public void startGame() {
        // Check if there are enough players to start the game
        if (!gameStarted && players.size() >= getMinPlayersToStart()) {
            // Start the game
            gameStarted = true;
            // Implement your game logic here
            // TODO SWITCH FROM LOBBY ROOM TO TABLE
        }
    }

    private int getMinPlayersToStart() {
        // Define the minimum number of players required to start the game
        return 2; // For example, at least 2 players
    }

    public Integer getNumberOfHumanPlayersPresent() {
        return numberOfHumanPlayersPresent;
    }

    public Integer getNumOfHumanPlayersRequired() {
        return maxNumOfPlayers;
    }

    public void setNumberOfHumanPlayersPresent(Integer numberOfHumanPlayersPresent) {
        this.numberOfHumanPlayersPresent = numberOfHumanPlayersPresent;
    }

    public boolean isFull() {
        return players.size() == getMaxNumOfPlayers();
    }

    public boolean checkStartTable(String playerName) {
        if (playerName.equals(hostPlayerName)) {
            return true;
        }
        return false;
    }

    /*
     * //TODO NEED TO UPDATE LABEL AND PLAYERS IN LOBBY ROOM
     * public void startCountdown() {
     * Runnable countdownTask = () -> {
     * if (remainingTimeMillis <= 0) {
     * scheduler.shutdown(); // Stop the countdown
     * onCountdownComplete();
     * } else {
     * remainingTimeMillis -= 1000; // Decrement remaining time
     * }
     * };
     * 
     * // Schedule the countdown task to run every second
     * scheduler.scheduleAtFixedRate(countdownTask, 0, 1, TimeUnit.SECONDS);
     * }
     * private void onCountdownComplete() {
     * // Logic to execute when the countdown is over
     * // For example, adding bots to the game or starting the game
     * // If the desired number of players is not reached, add bots
     * if (getNumberOfHumanPlayersPresent() < numOfHumanPlayersRequired) {
     * int botsToAdd = getMaxNumOfPlayers()- getNumberOfHumanPlayersPresent();
     * addBots(botsToAdd);
     * }
     * isGameReady = true;
     * 
     * }
     *
     * 
     */
    public boolean isGameReady() {
        return isGameReady;
    }

    // Method to check if the waiting period is over


}
