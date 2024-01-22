package sharedClasses;

import java.io.Serializable;
import java.util.UUID;

public class ServerCard implements Serializable {

    public boolean isCovered() {
        return covered;
    }

    // Variables
    boolean covered;
    private final String type; // Give cards unique effects

    private final int value; // ServerCard's value, especially for seed cards

    public UUID getServeCardID() {
        return serveCardID;
    }

    private final UUID serveCardID;
    private int[] cardCell;

    // Construct
    public ServerCard(String type,  int value, boolean covered){
        this.covered = covered;
        this.type = type;
        this.value = value;
        this.serveCardID = UUID.randomUUID();
    }

    // Methods


    public String getType(){
        return type;
    }

    public int[] getCardCell() {
        return cardCell;
    }

    public void setCardCell(int[] cardCell) {
        this.cardCell = cardCell;
    }





    public int getValue(){
        return value;
    }
    @Override
    public String toString() {
        return "(" + getValue()+ " "+ getType()+ ")";
    }
}
