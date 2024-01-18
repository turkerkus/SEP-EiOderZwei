package rmi;

import java.util.UUID;

public interface GameSessionCallback {
    void endGameSession(UUID gameId);
}
