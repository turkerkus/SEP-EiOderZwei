package sharedClasses;

import javax.swing.*;
import java.io.Serializable;
import java.util.UUID;

public class ServerCard implements Serializable {

    public boolean isCovered() {
        return covered;
    }

    // Variables
    boolean covered;
    private final String type; // Give cards unique effects

    final int value; // ServerCard's value, especially for seed cards

    public UUID getId() {
        return id;
    }

    private final UUID id;

    // Construct
    public ServerCard(String type,  int value, boolean covered){
        this.covered = covered;
        this.type = type;
        this.value = value;
        this.id = UUID.randomUUID();
    }

    // Methods


    public String getType(){
        return type;
    }



    public int getValue(){
        return value;
    }
}
