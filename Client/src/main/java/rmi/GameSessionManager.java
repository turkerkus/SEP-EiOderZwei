package rmi;

import sharedClasses.ClientUIUpdateListener;
import sharedClasses.CustomTimer;
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
    private ServerCallback severCallBack;

    public GameSessionManager(ServerCallback callBack) {
        severCallBack = callBack;
        this.callback = new GameSessionCallback() {
            @Override
            public void endGameSession(UUID gameId) {
                GameSession gameSession = gameSessions.get(gameId);
                gameSessionIds.remove(gameSession.getGameName());
                gameSessions.remove(gameId);
            }

            @Override
            public Map<UUID, ServerPlayer> getPlayers(UUID gameId) {
                GameSession gameSession = getGameSession(gameId);
                if(gameSession != null) return gameSession.getServerPlayers();
                return null;
            }

            @Override
            public ServerPlayer getRoosterCardHolder(UUID gameId) {
                return getGameSession(gameId).getRoosterPlayer();
            }

            @Override
            public void drawCard(UUID clientId, UUID gameId) {
                GameSession gameSession = getGameSession(gameId);
                try {
                    if(doesGameExit(gameId)) gameSession.drawCard(clientId);
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
                //TODO REMOVE THIS PRINT LINE
                System.out.println("end player turn from BOT");
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

            @Override
            public void hahnKlauen(UUID gameId,UUID clientId) {
                try {
                    getGameSession(gameId).hahnKlauen(clientId);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void stealingInProgress(UUID gameId, UUID playerId, UUID targetId, ArrayList<ServerCard> selectedCards) {
                getGameSession(gameId).stealingInProgress(playerId,targetId,selectedCards);
            }

            @Override
            public boolean isGameSessionActive(UUID gameId) {
                GameSession gameSession = getGameSession(gameId);
                if(gameSession == null) return false;
                return gameSession.isActive();
            }

            @Override
            public void updateGameSessionList(ClientUIUpdateListener hostListener) throws RemoteException {
                severCallBack.updateGameSessionList(hostListener);

            }
        };
    }

    public Map<String, UUID> getGameSessionIds() {
        return gameSessionIds;
    }

    private Map<String,UUID>gameSessionIds = new ConcurrentHashMap<>();


    public UUID createGameSession(UUID clientID, ClientUIUpdateListener listener, String gameName, String playerName, Integer numOfHumanPlayersRequired, String botLevel) {
        UUID gameId = UUID.randomUUID();
        GameSession gameSession = new GameSession(this.callback, clientID,listener, gameName,gameId, playerName, numOfHumanPlayersRequired, botLevel);
        gameSessions.put(gameId, gameSession);
        gameSessionIds.put(gameName,gameId);
        try {
            severCallBack.updateGameSessionList(listener);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
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

