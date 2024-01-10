package eoz.client.lobbyToTable;

import javax.swing.*;
import java.io.Serializable;

public class HandCard extends Card1 implements Serializable {
    public HandCard(String type, ImageIcon image, int value, boolean covered) {
        super(type, image, value, covered);
    }
    public String toString(){
        return String.valueOf(this.getValue());
    }
}
