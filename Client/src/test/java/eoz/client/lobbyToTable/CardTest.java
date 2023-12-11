package eoz.client.lobbyToTable;

import javafx.scene.image.Image;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class CardTest{
    ImageIcon img= new ImageIcon(getClass().getResource("/cards/zwei.png").toString());
    Card test = new Card(1999,"test", img,42);

    @Test
    void getID() {
        assertEquals(test.getID(), 1999);
    }

    @Test
    void getType() {
        assertEquals(test.getType(), "test");
    }

    @Test
    void getImage() {
        assertEquals(test.getImage(),img);
    }

    @Test
    void getValue() {
        assertEquals(test.getValue(), 42);
    }
}