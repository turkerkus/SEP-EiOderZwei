package eoz.client.lobbyToTable;

import javafx.scene.image.Image;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest{
    Card test = new Card(1999,"test","resources/image/Ei.jpg",42);

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
        assertEquals(test.getImage(), "resources/image/Ei.jpg");
    }

    @Test
    void getValue() {
        assertEquals(test.getValue(), 42);
    }
}