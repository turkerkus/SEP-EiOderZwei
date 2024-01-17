package eoz.client.lobbyToTable;

import sharedClasses.ServerCard;

import javax.swing.*;
import java.util.Objects;

public class Card extends ServerCard {
    public void setImage() {
        if(Objects.equals(getType(), "Koerner")){
            if (getValue() == 2){
                image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/cards/drei.png")).toString());
            } else if (getValue() == 3) {
                image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/cards/drei.png")).toString());
            } else {
                image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/cards/vier.png")).toString());
            }

        } else if (Objects.equals(getType(), "Fuchs")) {
            image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/cards/fuchs.png")).toString());
        } else if (Objects.equals(getType(), "Hahn")) {
            image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/cards/hahn.png")).toString());
        } else if (Objects.equals(getType(), "BioKoerner")) {
            if (getValue() == 2 || getValue() == 1){
                image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/cards/zweib.png")).toString());
            }  else {
                image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/cards/dreib.png")).toString());
            }
        } else if (Objects.equals(getType(), "Kuckuck")) {
            image =  new ImageIcon(Objects.requireNonNull(getClass().getResource("/cards/kuckuck.png")).toString());
        }
    }

    private  ImageIcon image; // To display the card in JavaFX

    public Card(String type,  int value, boolean covered) {
        super(type, value, covered);
        setImage();
    }

    public ImageIcon getImage(){
        return image;
    }


}
