package sharedClasses;

import javax.swing.*;
import java.io.Serializable;
import java.util.UUID;

public class Card implements Serializable {

    // Variables
    boolean covered;
    private final String type; // Give cards unique effects
    private final ImageIcon image; // To display the card in JavaFX
    final int value; // Card's value, especially for seed cards

    public UUID getId() {
        return id;
    }

    private final UUID id;

    // Construct
    public Card(String type, ImageIcon image, int value, boolean covered){
        this.covered = covered;
        this.type = type;
        this.image = image;
        this.value = value;
        this.id = UUID.randomUUID();
    }

    // Methods


    public String getType(){
        return type;
    }

    public ImageIcon getImage(){
        return image;
    }

    public int getValue(){
        return value;
    }
}
