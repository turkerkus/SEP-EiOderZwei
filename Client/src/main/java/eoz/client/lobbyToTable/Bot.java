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
        setZug(true);
        switch (schwierigkeit) {
            case 1:
                playSchwierigkeitLeicht();
                break;
            case 2:
                playSchwierigkeitMittel();
                break;
            case 3:
                playSchwierigkeitSchwer();
                break;
        }
        return true;
    }


    public void setZug(boolean entryZug){
        zug = entryZug;
    }

    public void playSchwierigkeitLeicht(){

    }
    public void playSchwierigkeitMittel(){

    }
    public void playSchwierigkeitSchwer(){

    }
    public int getSchwierigkeit() {
        return schwierigkeit;
    }

    public void setUID(UUID ID){
    }
}
