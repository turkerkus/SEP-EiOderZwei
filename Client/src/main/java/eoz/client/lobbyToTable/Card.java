package eoz.client.lobbyToTable;

import javafx.scene.image.Image;
import sharedClasses.ServerCard;

import javax.swing.*;
import java.util.Objects;

public class Card extends ServerCard {


    public void setImage() {
        if(Objects.equals(getType(), "Koerner")){
            if (getValue() == 2){
                image = new Image(Objects.requireNonNull(getClass().getResource("/cards/zwei.png")).toExternalForm());
            } else if (getValue() == 3) {
                image = new Image(Objects.requireNonNull(getClass().getResource("/cards/drei.png")).toExternalForm());
            } else {
                image = new Image(Objects.requireNonNull(getClass().getResource("/cards/vier.png")).toExternalForm());
            }

        } else if (Objects.equals(getType(), "Fuchs")) {
            image = new Image(Objects.requireNonNull(getClass().getResource("/cards/fuchs.png")).toExternalForm());
        } else if (Objects.equals(getType(), "Hahn")) {
            image = new Image(Objects.requireNonNull(getClass().getResource("/cards/hahn.png")).toExternalForm());
        } else if (Objects.equals(getType(), "BioKoerner")) {
            if (getValue() == 1){
                image = new Image(Objects.requireNonNull(getClass().getResource("/cards/einb.png")).toExternalForm());
            }else if (getValue() == 2){
                image = new Image(Objects.requireNonNull(getClass().getResource("/cards/zweib.png")).toExternalForm());
            }  else {
                image = new Image(Objects.requireNonNull(getClass().getResource("/cards/dreib.png")).toExternalForm());
            }
        } else if (Objects.equals(getType(), "Kuckuck")) {
            image =  new Image(Objects.requireNonNull(getClass().getResource("/cards/kuckuck.png")).toExternalForm());
        }
    }

    private Image image; // To display the card in JavaFX

    public Card(String type,  int value, boolean covered) {
        super(type, value, covered);
        setImage();
    }

    public Image getImage(){
        return image;
    }


}
