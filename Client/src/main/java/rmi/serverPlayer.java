package rmi;

import java.io.Serializable;
import java.util.UUID;

public class serverPlayer implements Serializable {

    private final UUID id;
    private final String name;
    private boolean hasHahnKarte;



    private int kornzahl;

    // Constructor, getters, setters
    public serverPlayer(UUID id, String name, boolean hasHahnKarte, int kornzahl) {
        this.id = id;
        this.name = name;
        this.hasHahnKarte = hasHahnKarte;
        this.kornzahl = kornzahl;
    }

    // Additional methods as needed
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isHasHahnKarte() {
        return hasHahnKarte;
    }

    public int getKornzahl() {
        return kornzahl;
    }
}