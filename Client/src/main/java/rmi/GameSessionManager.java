package rmi;

import sharedClasses.ClientUIUpdateListener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameSessionManager {
    private Map<UUID, GameSession> gameSessions = new ConcurrentHashMap<>();
    private GameSessionCallback callback;

    public GameSessionManager() {
        this.callback = new GameSessionCallback() {
            @Override
            public void endGameSession(UUID gameId) {
                gameSessions.remove(gameId);
            }
        };
    }

    public UUID createGameSession(UUID clientID, ClientUIUpdateListener listener, String gameName, String playerName, Integer numOfHumanPlayersRequired) {
        UUID gameId = UUID.randomUUID();
        GameSession gameSession = new GameSession(this.callback, clientID,listener, gameName,gameId, playerName, numOfHumanPlayersRequired);
        gameSessions.put(gameId, gameSession);
        return gameId;
    }

    // Method to retrieve a game session by its ID
    public GameSession getGameSession(UUID gameId) {
        if (gameSessions.containsKey(gameId)) {
            return gameSessions.get(gameId);
        } else {
            // Handle the case where the UUID is not found in the map
            return null; // or throw an exception, log an error, etc.
        }
    }

    // Other management methods like joinGameSession, endGameSession, etc.

    public boolean doesGameExit(UUID gameId){
        boolean gameExists = gameSessions.containsKey(gameId);
        return gameExists;
    }
}

