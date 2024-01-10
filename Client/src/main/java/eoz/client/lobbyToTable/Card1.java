package eoz.client.lobbyToTable;

import javax.swing.ImageIcon;
import java.io.Serializable;

public class Card1 implements Serializable {

    // Variables
    boolean covered;
    private final String type; // Give cards unique effects
    private final ImageIcon image; // To display the card in JavaFX
    final int value; // Card's value, especially for seed cards

    // Construct
    public Card1(String type, ImageIcon image, int value, boolean covered){
        this.covered = covered;
        this.type = type;
        this.image = image;
        this.value = value;
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
