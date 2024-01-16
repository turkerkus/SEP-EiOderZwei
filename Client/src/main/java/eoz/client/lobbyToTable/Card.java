package eoz.client.lobbyToTable;

import sharedClasses.ServerCard;

import javax.swing.*;

public class Card extends ServerCard {
    public void setImage() {
        if(getType() == "Koerner"){
            if (getValue() == 2){
                image = new ImageIcon(getClass().getResource("/cards/drei.png").toString());
            } else if (getValue() == 3) {
                image = new ImageIcon(getClass().getResource("/cards/drei.png").toString());
            } else {
                image = new ImageIcon(getClass().getResource("/cards/vier.png").toString());
            }

        } else if (getType() == "Fuchs") {
            image = new ImageIcon(getClass().getResource("/cards/fuchs.png").toString());
        } else if (getType() == "Hahn") {
            
        } else if (getType() == "BioKoerner") {
            if (getValue() == 2 || getValue() == 1){
                image = new ImageIcon(getClass().getResource("/cards/zweib.png").toString());
            }  else {
                image = new ImageIcon(getClass().getResource("/cards/dreib.png").toString());
            }
        } else if (getType() == "Kuckuck") {
            image =  new ImageIcon(getClass().getResource("/cards/kuckuck.png").toString());
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
