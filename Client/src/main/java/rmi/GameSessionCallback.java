package rmi;

import sharedClasses.ServerCard;
import sharedClasses.ServerPlayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public interface GameSessionCallback extends Serializable {
    void endGameSession(UUID gameId);

    Map<UUID, ServerPlayer>  getPlayers(UUID gameId);

    ServerPlayer getRoosterCardHolder(UUID gameId);

    void drawCard(UUID clientId, UUID gameId);

    void karteUmtauschen(UUID gameId,UUID clientId, Integer eggPoints, ArrayList<ServerCard> selectedCards);

    void endPlayerTurn(UUID gameId);
    void stealOneCard(UUID gameId, UUID targetId, ArrayList<ServerCard> selectedCards, UUID clientId);


    void stealAllCards(UUID gameID, UUID target, UUID clientId);
}
