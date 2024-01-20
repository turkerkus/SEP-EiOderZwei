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

    /**
     * Removes the rooster card from the cell (1, 0) if it exists.
     * If the cell (1, 0) is empty or contains a different card, no action is taken.
     */
    public void removeRoosterCard() {
        // Check if cell (1, 0) contains a rooster card
        if (isCellOccupied(1, 0)) {
            Node roosterCard = getCardInCell(getCellKey(1, 0));

            //get the last card
            int[] nextCell = getNextAvailableCell();
            int row = nextCell[0];
            int col = nextCell[1];
            Node lastCard = getCardInCell(getCellKey(row, col));


            // Remove the rooster card from cell (1, 0)
            removeCard(roosterCard);

            // Put the last card at the first cell
            if (lastCard != null){
                addCard(lastCard, 1,0);
            }

        }
    }


    // Get the card Node in the specified cell
    public Node getCardInCell(Integer key) {
        int row = key / this.getColumnCount();
        int col = key % this.getColumnCount();
        for (Node child : this.getChildren()) {
            if (GridPane.getRowIndex(child) == row && GridPane.getColumnIndex(child) == col) {
                return child;
            }
        }
        return null;
    }

    //This method helps you to always put the rooster card in cell (1,0)
    /**
     * Adds a rooster card to the custom CardGridPane, replacing any card in cell (1, 0) if it's occupied.
     * If the cell (1, 0) is occupied, the existing card is moved to the next available cell.
     *
     * @param roosterCard The Node representing the rooster card to be added.
     */
    public void addRoosterCard(Node roosterCard) {
        int row = 1;            // Default row for the rooster card
        int col = 0;            // Default column for the rooster card
        Node existingCard;   // Stores any existing card in cell (1, 0)

        // Check if cell (1, 0) is occupied with a card
        if (isCellOccupied(1, 0)) {
            // Get the existing card in cell (1, 0)
            existingCard = getCardInCell(getCellKey(1, 0));

            // Remove the existing card from cell (1, 0)
            removeCard(existingCard);

            // Add the provided rooster card to cell (1, 0)
            this.add(roosterCard, 1, 0);

            // Find the next available cell for the existing card
            int[] nextCell = getNextAvailableCell();
            row = nextCell[0];
            col = nextCell[1];

            // Add the existing card to the next available cell
            this.add(existingCard, row, col);
        }

        // Add the rooster card to its designated cell
        this.addCard(roosterCard, row, col);
    }



    // Method to check if a cell is occupied
    public boolean isCellOccupied(int row, int col) {
        return cellOccupancy.getOrDefault(getCellKey(row, col), false);
    }
}
