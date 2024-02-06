package sharedClasses;

import java.util.ArrayList;
import java.util.UUID;

public interface TableControllerInterface {

    void setCurrentPlayerID(UUID playerID);

    void updateTimerLabel(Integer timeLeft);

    void hahnKarteGeben(UUID playerId);

    void playerLeftGameSession(UUID disconnectedPlayerID, String botName);

    void hasDrawnACard(UUID playerId, ServerCard serverCard);

    void changeRoosterPlayer(UUID oldRoosterPlayerID, UUID newRoosterPlayerID);

    void startGameUiUpdate();

    void drawnKuckuckCard(UUID playerID, ServerCard kuckuckCard);

    void drawnFoxCard(UUID playerID, ServerCard foxCard);

    void cardDiscarded(UUID playerID, ServerCard discardedCard, Integer eggPoints, ArrayList<ServerCard> selectedCards);

    void oneCardStolen(UUID target, ServerCard stolenCard, UUID playerId);

    void allCardsStolen(UUID target, UUID playerId);

    void updateChat(String message, UUID playerId);

    void switchToResults(ServerPlayer winner);

    void stealingCardCompleted(UUID target, UUID thief, ArrayList<ServerCard> stollenCards);

    void removeFoxCard(ServerCard foxCard);
}
