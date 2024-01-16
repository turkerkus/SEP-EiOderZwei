package sharedClasses;

import javax.swing.*;
import java.io.Serializable;

public class HandServerCard extends ServerCard implements Serializable {
    public HandServerCard(String type,  int value, boolean covered) {
        super(type,  value, covered);
    }
    public String toString(){
        return String.valueOf(this.getValue());
    }
}
