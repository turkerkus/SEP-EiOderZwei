package eoz.client.lobbyToTable;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Map;

public class CardGridPane extends GridPane {
    // This map will hold the occupancy status for each cell
    private final Map<Integer, Boolean> cellOccupancy = new HashMap<>();
    private int nextAvailableRow = 1;
    private int nextAvailableCol = 0;

    public CardGridPane() {
        // Assuming the grid has rows 0-5 and columns 0-5
        // We only allow card placement in rows 1-4 and columns 0-4
        for (int row = 1; row <= 4; row++) {
            for (int col = 0; col <= 4; col++) {
                cellOccupancy.put(getCellKey(row, col), false);
            }
        }
    }

    // Utility method to create a unique key for each cell based on its row and column
    private Integer getCellKey(int row, int col) {
        return row * this.getColumnCount() + col;
    }

    // Method to add a card to a cell
    public void addCard(Node card, int row, int col) {
        if (row < 1 || row > 4 || col < 0 || col > 4) {
            // Outside allowed range
            return;
        }

        Integer key = getCellKey(row, col);
        if (cellOccupancy.get(key) == null || !cellOccupancy.get(key)) {
            this.add(card, col, row);
            cellOccupancy.put(key, true);
            updateNextAvailableCell();
        }
    }
    public void updateNextAvailableCell() {
        for (int row = 1; row <= 4; row++) {
            for (int col = 0; col <= 4; col++) {
                Integer key = getCellKey(row, col);
                if (cellOccupancy.get(key) == null || !cellOccupancy.get(key)) {
                    nextAvailableRow = row;
                    nextAvailableCol = col;
                    return;
                }
            }
        }
        nextAvailableRow = -1; // Indicates no available cell
        nextAvailableCol = -1; // Indicates no available cell
    }

    public int[] getNextAvailableCell() {
        if (nextAvailableRow == -1 || nextAvailableCol == -1) {
            return null; // No available cell
        }
        return new int[]{nextAvailableRow, nextAvailableCol};
    }

    // Method to remove a card from a cell
    public void removeCard(Node card) {
        // Find the cell that contains this card
        for (Map.Entry<Integer, Boolean> entry : cellOccupancy.entrySet()) {
            if (entry.getValue()) {
                Node nodeInCell = getCardInCell(entry.getKey());
                if (nodeInCell == card) {
                    this.getChildren().remove(card);
                    cellOccupancy.put(entry.getKey(), false);
                    updateNextAvailableCell();
                    break;
                }
            }
        }
    }

    // Get the card Node in the specified cell
    private Node getCardInCell(Integer key) {
        int row = key / this.getColumnCount();
        int col = key % this.getColumnCount();
        for (Node child : this.getChildren()) {
            if (GridPane.getRowIndex(child) == row && GridPane.getColumnIndex(child) == col) {
                return child;
            }
        }
        return null;
    }

    // Method to check if a cell is occupied
    public boolean isCellOccupied(int row, int col) {
        return cellOccupancy.getOrDefault(getCellKey(row, col), false);
    }
}
