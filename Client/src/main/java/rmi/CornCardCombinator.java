package rmi;

import sharedClasses.ServerCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CornCardCombinator implements Serializable {

    Map<UUID, ServerCard> cornCards;

    Map<UUID, ServerCard> type1;
    Map<UUID, ServerCard> type2;
    Map<UUID, ServerCard> type3;
    Map<UUID, ServerCard> type4;

    ArrayList<ServerCard> cornCardCombination;

    Integer cornValue;
    Map<String, Integer> combination;
    private Integer eggPoints;

    public CornCardCombinator(Map<UUID, ServerCard> cornCards) {
        this.cornCards = cornCards;
        type1 = new ConcurrentHashMap<>();
        type2 = new ConcurrentHashMap<>();
        type3 = new ConcurrentHashMap<>();
        type4 = new ConcurrentHashMap<>();


        // Categorize bioCorn cards by type (type1, type2, type3).
        for (ServerCard serverCard : cornCards.values()) {
            switch (serverCard.getValue()) {
                case 1 -> type1.put(serverCard.getServeCardID(), serverCard);
                case 2 -> type2.put(serverCard.getServeCardID(), serverCard);
                case 3 -> type3.put(serverCard.getServeCardID(), serverCard);
                case 4 -> type4.put(serverCard.getServeCardID(), serverCard);
            }
        }

        // Calculate the bioCornValue based on the total values of each type.
        System.out.println("number of type 1 corn cards are: " + type1.size());
        System.out.println("number of type 2 corn cards are: " + type2.size());
        System.out.println("number of type 3 corn cards are: " + type3.size());
        System.out.println("number of type 4 corn cards are: " + type4.size());
        int totalValue = type1.size() + (type2.size() * 2) + (type3.size() * 3) + (type4.size() * 4);
        cornValue = (totalValue / 5) * 5;
        System.out.println("with totalValue of: " + totalValue + " the  Corn value is: " + cornValue);

        // Calculate optimal egg exchange combinations.
        calculateHelper(cornValue, type1.size(), type2.size(), type3.size(), type4.size());


        // after do the first calculation and there is no combination reduce cornValue and try again
        cornCardCombination = calculate(totalValue / 5, cornValue);
    }

    // Main method for testing the BioCardCombinator class.
    public static void main(String[] args) {
        Map<UUID, ServerCard> cornCards = new ConcurrentHashMap<>();
        for (int i = 0; i < 3; i++) {
            cornCards.put(UUID.randomUUID(), new sharedClasses.ServerCard("Koerner", 1, true));
        }
        for (int i = 0; i < 3; i++) {
            cornCards.put(UUID.randomUUID(), new sharedClasses.ServerCard("Koerner", 2, true));
        }
        for (int i = 0; i < 3; i++) {
            cornCards.put(UUID.randomUUID(), new sharedClasses.ServerCard("Koerner", 3, true));
        }
        for (int i = 0; i < 3; i++) {
            cornCards.put(UUID.randomUUID(), new sharedClasses.ServerCard("Koerner", 4, true));
        }
        CornCardCombinator cornCardCombinator = new CornCardCombinator(cornCards);
        System.out.println(cornCardCombinator);
    }

    public ArrayList<ServerCard> calculate(Integer quotient, Integer Value) {
        calculateHelper(Value, type1.size(), type2.size(), type3.size(), type4.size());
        ArrayList<ServerCard> selectedCards = cornCombination(combination, cornCards);

        if (selectedCards.isEmpty() && quotient != 0) {
            return calculate(quotient - 1, Value - 5); // Recursively calculate with reduced Value
        } else return selectedCards; // Return the selected cards
    }

    public void calculateHelper(int cornValue, int type1Count, int type2Count, int type3Count, int type4Count) {
        int CORN_TO_EGG_CONVERSION_RATE = 5;
        Map<String, Integer> optimalCombination = new HashMap<>();
        int optimalEggPoints = 0;

        // Generate all combinations
        for (int type1Used = 0; type1Used <= type1Count; type1Used++) {
            for (int type2Used = 0; type2Used <= type2Count; type2Used++) {
                for (int type3Used = 0; type3Used <= type3Count; type3Used++) {
                    for (int type4Used = 0; type4Used <= type4Count; type4Used++) {
                        int totalValue = type1Used + (type2Used * 2) + (type3Used * 3) + (type4Used * 4);
                        if (totalValue == cornValue) {
                            int currentEggPoints = totalValue / CORN_TO_EGG_CONVERSION_RATE;
                            if (currentEggPoints > optimalEggPoints || (currentEggPoints == optimalEggPoints && totalValue < cornValue)) {
                                optimalCombination.clear();
                                optimalCombination.put("Type1", type1Used);
                                optimalCombination.put("Type2", type2Used);
                                optimalCombination.put("Type3", type3Used);
                                optimalCombination.put("Type4", type4Used);
                                optimalEggPoints = currentEggPoints;
                            }
                        }
                    }
                }
            }
        }

        combination = optimalCombination;
        eggPoints = optimalEggPoints;
    }

    // Method to create a list of selected bioCorn cards based on the combination.
    public ArrayList<ServerCard> cornCombination(Map<String, Integer> combination, Map<UUID, ServerCard> cornCards) {
        ArrayList<ServerCard> selectedCards = new ArrayList<>();
        Map<String, Integer> typeCounter = new HashMap<>(combination);

        for (ServerCard card : cornCards.values()) {
            String type = "Type" + card.getValue(); // Assuming the card type is represented as "TypeX"
            if (typeCounter.containsKey(type) && typeCounter.get(type) > 0) {
                selectedCards.add(card);
                typeCounter.put(type, typeCounter.get(type) - 1);
            }
        }

        return selectedCards;
    }

    public Integer getEggPoints() {
        return eggPoints;
    }

    // toString method for debugging and displaying the optimal exchange result.
    @Override
    public String toString() {
        for (ServerCard card : cornCardCombination) {
            System.out.println(card.getServeCardID() + card.toString());
        }
        return "OptimalExchangeResult{" +
                "combination=" + combination +
                ", eggPoints=" + eggPoints +
                '}';
    }

}
