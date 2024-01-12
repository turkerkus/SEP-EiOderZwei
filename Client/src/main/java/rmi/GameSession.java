package rmi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class GameSession {
    private final UUID gameId;
    private final List<serverPlayer> players = new ArrayList<>();
    private boolean gameStarted = false;



    private boolean isGameReady = false;
    private Integer numberOfHumanPlayersPresent = 0;


    private final Integer numOfHumanPlayersRequired;

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


    public long getRemainingTimeMillis() {
        return remainingTimeMillis;
    }

    private long remainingTimeMillis = (long) (0.2 * 60 * 1000);              // Remaining time

    public GameSession(UUID gameId, String hostPlayerName, Integer numOfHumanPlayersRequired) {
        this.gameId = gameId;
        this.numOfHumanPlayersRequired = numOfHumanPlayersRequired;
        // Add the host player
        addPlayer(hostPlayerName);
        startCountdown();
        if (numOfHumanPlayersRequired == 1){
            remainingTimeMillis = 10000;
            addBots(5);
        }
    }
    public  String generateFunnyBotName() {
        Random random = new Random();
        int index = random.nextInt(FUNNY_BOT_NAMES.length);
        return FUNNY_BOT_NAMES[index];
    }

    public UUID getGameId() {
        return gameId;
    }

    public List<serverPlayer> getPlayers() {
        return players;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void addPlayer(String playerName) {
        // Check if the game is already started or the maximum number of players is reached
        if (!gameStarted && players.size() < getMaxNumOfPlayers()) {
            players.add(new serverPlayer(UUID.randomUUID(), playerName, false, 0));
            this.numberOfHumanPlayersPresent++;
        }
    }

    public void addBots(Integer bot) {
        for (int i = 1; i < bot+1; i++) {
            String botName = generateFunnyBotName() + "_@Bot" +i;
            // Check if the game is already started or the maximum number of players is reached
            if (!isGameReady && players.size() < getMaxNumOfPlayers()) {
                players.add(new serverPlayer(UUID.randomUUID(), botName, false, 0));
            }
        }

    }

    public int getMaxNumOfPlayers() {
        // Define the maximum number of players for your game
        return 6; // For example, a maximum of 6 players
    }

    public void startGame() {
        // Check if there are enough players to start the game
        if (!gameStarted && players.size() >= getMinPlayersToStart()) {
            // Start the game
            gameStarted = true;
            // Implement your game logic here
            //TODO SWITCH FROM LOBBY ROOM TO TABLE
        }
    }

    private int getMinPlayersToStart() {
        // Define the minimum number of players required to start the game
        return 1; // For example, at least 2 players
    }

    public Integer getNumberOfHumanPlayersPresent() {
        return numberOfHumanPlayersPresent;
    }
    public Integer getNumOfHumanPlayersRequired() {
        return numOfHumanPlayersRequired;
    }

    public void setNumberOfHumanPlayersPresent(Integer numberOfHumanPlayersPresent) {
        this.numberOfHumanPlayersPresent = numberOfHumanPlayersPresent;
    }

    public boolean isFull() {
        return players.size() == getMaxNumOfPlayers();
    }
    //TODO NEED TO UPDATE LABEL AND PLAYERS IN LOBBY ROOM
    public void startCountdown() {
        new Thread(() -> {
            while (remainingTimeMillis > 0) {
                try {
                    Thread.sleep(1000);  // Sleep for 1 second
                    remainingTimeMillis -= 1000;  // Decrement remaining time
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    // Optionally, handle the interruption, e.g., by breaking the loop
                    break;
                }
            }
            // Notify when the countdown is over
            onCountdownComplete();
        }).start();
    }
    private void onCountdownComplete() {
        // Logic to execute when the countdown is over
        // For example, adding bots to the game or starting the game
        // If the desired number of players is not reached, add bots
        if (getNumberOfHumanPlayersPresent() < numOfHumanPlayersRequired) {
            int botsToAdd = getMaxNumOfPlayers()- getNumberOfHumanPlayersPresent();
            addBots(botsToAdd);
        }
        isGameReady = true;

    }
    public boolean isGameReady() {
        return isGameReady;
    }

    // Method to check if the waiting period is over
    public boolean isWaitingPeriodOver() {
        return remainingTimeMillis <= 0;
    }
}
