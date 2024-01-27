package rmi;

import sharedClasses.ServerPlayer;
import sharedClasses.ServerTable;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class GameLogic implements Serializable {


    private ServerPlayer winner = null; // Initialize the winner variable
    public ServerPlayer getWinner() {
        return winner;
    }

    public ServerPlayer findWinningPlayer(Map<UUID, ServerPlayer> spielerArray, ServerTable serverTable) {
        System.out.println();
        System.out.println("From Game Logic Checking the winner");



        if (serverTable.getMoveCount() < 1000) {
            int requiredPoints = switch (spielerArray.size()) {
                case 2 -> 9;
                case 3 -> 8;
                case 4 -> 7;
                case 5 -> 6;
                case 6 -> 5;
                default -> 0;
            };

            for (Map.Entry<UUID, ServerPlayer> entry : spielerArray.entrySet()) {
                ServerPlayer spieler = entry.getValue();
                System.out.println(spieler.getServerPlayerName() + " has " + spieler.getPunkte() + " points");

                if (spieler.getPunkte() >= requiredPoints) {
                    winner = spieler; // Set the winner
                    break; // No need to continue checking if we found a winner
                }
            }
        }

        System.out.println();
        return winner; // Return the winner or null if there's no winner yet
    }


}
