package rmi;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameSessionManager {
    private Map<UUID, GameSession> gameSessions= new HashMap<>();

    public GameSessionManager() {
    }

    public UUID createGameSession(String playerName, Integer numOfHumanPlayersRequired) {
        UUID gameId = UUID.randomUUID();
        GameSession gameSession = new GameSession(gameId, playerName, numOfHumanPlayersRequired);
        gameSessions.put(gameId, gameSession);
        return gameId;
    }

    // Method to retrieve a game session by its ID
    public GameSession getGameSession(UUID gameId) {
        return gameSessions.get(gameId);
    }

    // Other management methods like joinGameSession, endGameSession, etc.
}

