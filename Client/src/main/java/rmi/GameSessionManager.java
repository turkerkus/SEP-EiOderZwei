package rmi;

import sharedClasses.ClientUIUpdateListener;
import sharedClasses.ServerCard;
import sharedClasses.ServerPlayer;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameSessionManager implements Serializable {
    private Map<UUID, GameSession> gameSessions = new ConcurrentHashMap<>();
    private GameSessionCallback callback;

    public GameSessionManager() {
        this.callback = new GameSessionCallback() {
            @Override
            public void endGameSession(UUID gameId) {
                gameSessions.remove(gameId);
            }

            @Override
            public Map<UUID, ServerPlayer> getPlayers(UUID gameId) {
                return getGameSession(gameId).getServerPlayers();
            }

            @Override
            public ServerPlayer getRoosterCardHolder(UUID gameId) {
                return getGameSession(gameId).getRoosterPlayer();
            }

            @Override
            public void drawCard(UUID clientId, UUID gameId) {
                try {
                    getGameSession(gameId).drawCard(clientId);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void karteUmtauschen(UUID gameId,UUID clientId, Integer eggPoints, ArrayList<ServerCard> selectedCards) {
                try {
                    getGameSession(gameId).karteUmtauschen(clientId,eggPoints,selectedCards);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void endPlayerTurn(UUID gameId) {
                getGameSession(gameId).endPlayerTurn();
            }

            @Override
            public void stealOneCard(UUID gameId, UUID targetId, ArrayList<ServerCard> selectedCards, UUID clientId) {
                getGameSession(gameId).stealOneCard(targetId,selectedCards,clientId);
            }

            @Override
            public void stealAllCards(UUID gameID, UUID target, UUID clientId) {
                getGameSession(gameID).stealAllCards(target,clientId);
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

