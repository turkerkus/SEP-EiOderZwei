package rmi;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameSessionManager {
    private Map<UUID, GameSession> gameSessions = new ConcurrentHashMap<>();

    public GameSessionManager() {
    }

    public UUID createGameSession(UUID clientID, String gameName, String playerName, Integer numOfHumanPlayersRequired) {
        UUID gameId = UUID.randomUUID();
        GameSession gameSession = new GameSession(clientID,gameName,gameId, playerName, numOfHumanPlayersRequired);
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

