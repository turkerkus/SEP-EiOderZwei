package eoz.client.lobbyToTable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.ImageIcon;

public class Card {

    // Variables
    private int id; // Unique Cards in the deck.
    private String type; // Give cards unique effects
    private ImageIcon image; // To display the card in JavaFX
    private int value; // Card's value, especially for seed cards

    // Construct
    public Card(int id, String type, ImageIcon image, int value){
        this.id = id;
        this.type = type;
        this.image = image;
        this.value = value;
    }

    // Methods
    public int getID(){
        return id;
    }

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
