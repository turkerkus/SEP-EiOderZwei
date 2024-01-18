package rmi;

import sharedClasses.ServerPlayer;
import sharedClasses.ServerTable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameLogic {
    public ServerPlayer findWinningPlayer(Map<UUID, ServerPlayer> spielerArray, ServerTable serverTable) {
        if (serverTable.getMoveCount() < 1000) {              //Spätestens nach 1000 Durchläufen terminiert das Spiel (sollte eigentlich nie dadurch passieren)
            for (Map.Entry<UUID, ServerPlayer> entry  : spielerArray.entrySet()) {              // Überprüft ob einer der Spieler gewonnen hat
                ServerPlayer spieler = entry.getValue();
                if (spieler.getPunkte() >= 9 && spielerArray.size() == 2) {                 // 2 Spieler: 9 Eier
                    return spieler;
                } else if (spieler.getPunkte() >= 8 && spielerArray.size() == 3) {          // 3 Spieler: 8 Eier
                    return spieler;
                }else if (spieler.getPunkte() >= 7 && spielerArray.size() == 4) {           // 4 Spieler: 7 Eier
                    return spieler;
                }else if (spieler.getPunkte() >= 6 && spielerArray.size() == 5) {           // 6 Spieler: 5 Eier
                    return spieler;
                }else if (spieler.getPunkte() >= 5 && spielerArray.size() == 6) {           // 6 Spieler: 5 Eier
                    return spieler;
                }
            }
        }
        return null;
    }

}