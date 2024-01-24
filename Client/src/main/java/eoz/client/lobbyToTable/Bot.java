package eoz.client.lobbyToTable;

import javafx.scene.control.Label;

import java.io.Serializable;
import java.util.UUID;

public class Bot extends Spieler implements Serializable {
    private int schwierigkeit;
    private boolean zug = false;
    public Bot(UUID id, String playerName, boolean hahnKarte, CardGridPane cardGridPane, Label playerLabel, int entrySchwierigkeit) {
        super(id, playerName, hahnKarte, cardGridPane, playerLabel);
        schwierigkeit = entrySchwierigkeit;
    }

    public boolean play(){
        //TODO
        return false;
    }
}
