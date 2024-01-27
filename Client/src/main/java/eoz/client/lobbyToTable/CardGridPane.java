package eoz.client.lobbyToTable;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CardGridPane extends GridPane {
    // This map will hold the occupancy status for each cell
    private final Map<Integer, Boolean> cellOccupancy = new ConcurrentHashMap<>();
    private int nextAvailableRow = 1;
    private int nextAvailableCol = 0;

    public void setStartFromZero(boolean startFromZero) {
        this.startFromZero = startFromZero;
    }

    private boolean startFromZero = true;

    public int getStartRow() {
        return startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    private int startRow ;
    private int endRow ;

    public void setRow(){
        if (startFromZero){
            this.startRow = 1;
            this.endRow = 6;
        } else {
            this.startRow = 0;
            this.endRow = 5;
        }
    }

    public CardGridPane() {
        // Assuming the grid has rows 0-5 and columns 0-5
        // We only allow card placement in rows 1-4 and columns 0-4
        for (int row = startRow; row <= endRow; row++) {
            for (int col = 0; col <= 5; col++) {
                cellOccupancy.put(getCellKey(row, col), false);
            }
        }
    }

    // Utility method to create a unique key for each cell based on its row and column
    // Assuming a fixed column count of 6
    private static final int COLUMN_COUNT = 6;

    public Integer getCellKey(int row, int col) {
        return row * COLUMN_COUNT + col;
    }


    // Method to add a card to a cell
    public void addCard(Node card, int row, int col) {
        if (row < startRow || row > endRow || col < 0 || col > 5) {
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
        System.out.println();
        boolean found = false;
        for (int row = startRow; row <= endRow; row++) {
            for (int col = 0; col <= 5; col++) {
                Integer key = getCellKey(row, col);
                Boolean isOccupied = cellOccupancy.get(key);

                if (isOccupied == null || !isOccupied) {
                    nextAvailableRow = row;
                    nextAvailableCol = col;
                    found = true;
                    break;
                }
            }
            if (found) break;
        }

        if (!found) {
            nextAvailableRow = -1; // Indicates no available cell
            nextAvailableCol = -1; // Indicates no available cell
        }
        System.out.println();
    }


    // Method to find a node by its row and column in a GridPane
    private Node getNodeByRowColumnIndex(final int row, final int column) {
        Node result = null;
        for (Node node : this.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row &&
                    GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }

    // Method to get the number of available (empty) cells
    public int getNumberOfAvailableCells() {
        int availableCells = 0;
        for (int row = startRow; row <= endRow; row++) {
            for (int col = 0; col <= 5; col++) {
                Node node = getNodeByRowColumnIndex(row, col);
                if (node == null) {
                    availableCells++;
                }
            }
        }
        return availableCells;
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
    public void removeRoosterCard(UUID hahnCardID ) {
        Platform.runLater(() -> {
            // Iterate through the children of the playerLabelGrid
            for (Node node : this.getChildren()) {
                if (node instanceof ImageView) {
                    ImageView imageView = (ImageView) node;
                    String cardId = imageView.getId();

                    // Check if the cardId matches the hahnCardID
                    if (cardId != null && cardId.equals(hahnCardID.toString())) {
                        // Remove the matching ImageView from the playerLabelGrid
                        this.getChildren().remove(imageView);
                        break; // Exit the loop after removing the card
                    }
                }
            }
        });
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
        int row = 0;            // Default row for the rooster card
        int col = 1;            // Default column for the rooster card

        // Add the rooster card to its designated cell
        this.addCard(roosterCard, row, col);
    }



    // Method to check if a cell is occupied
    public boolean isCellOccupied(int row, int col) {
        return cellOccupancy.getOrDefault(getCellKey(row, col), false);
    }
}
