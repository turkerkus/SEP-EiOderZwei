package rmi;

import sharedClasses.ServerCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BioCardCombinator class calculates optimal egg exchange combinations for bioCorn cards.
 */
public class BioCardCombinator implements Serializable {
    Map<UUID, ServerCard> bioCornCards;

    Map<UUID, ServerCard> type1;
    Map<UUID, ServerCard> type2;
    Map<UUID, ServerCard> type3;

    ArrayList<ServerCard> bioCardCombination;

    Integer bioCornValue;
    Map<String, Integer> combination;
    private Integer eggPoints;


    // Constructor initializes the class with bioCornCards and calculates optimal combinations.
    public BioCardCombinator(Map<UUID, ServerCard> bioCornCards) {
        this.bioCornCards = bioCornCards;
        type1 = new ConcurrentHashMap<>();
        type2 = new ConcurrentHashMap<>();
        type3 = new ConcurrentHashMap<>();


        // Categorize bioCorn cards by type (type1, type2, type3).
        for (ServerCard serverCard : bioCornCards.values()) {

            switch (serverCard.getValue()) {
                case 1 -> type1.put(serverCard.getServeCardID(), serverCard);
                case 2 -> type2.put(serverCard.getServeCardID(), serverCard);
                case 3 -> type3.put(serverCard.getServeCardID(), serverCard);
            }
        }

        // Calculate the bioCornValue based on the total values of each type.
        System.out.println("number of type 1 bio cards are: " + type1.size());
        System.out.println("number of type 2 bio cards are: " + type2.size());
        System.out.println("number of type 3 bio cards are: " + type3.size());
        int totalValue = type1.size() + (type2.size() * 2) + (type3.size() * 3);
        bioCornValue = (totalValue / 5) * 5;
        System.out.println("with totalValue of: " + totalValue + " the bio Corn value is: " + bioCornValue);

        // Calculate optimal egg exchange combinations.
        bioCardCombination = calculate(totalValue / 5, bioCornValue);

    }

    // Main method for testing the BioCardCombinator class.
    public static void main(String[] args) {
        Map<UUID, ServerCard> bioCornCards = new ConcurrentHashMap<>();
        for (int i = 0; i < 3; i++) {
            bioCornCards.put(UUID.randomUUID(), new sharedClasses.ServerCard("BioKoerner", 1, true));
        }
        for (int i = 0; i < 3; i++) {
            bioCornCards.put(UUID.randomUUID(), new sharedClasses.ServerCard("BioKoerner", 2, true));
        }
        for (int i = 0; i < 3; i++) {
            bioCornCards.put(UUID.randomUUID(), new sharedClasses.ServerCard("BioKoerner", 3, true));
        }
        BioCardCombinator bioCardCombinator = new BioCardCombinator(bioCornCards);
        System.out.println(bioCardCombinator);
    }

    public ArrayList<ServerCard> calculate(Integer quotient, Integer Value) {
        calculateHelper(bioCornValue, type1.size(), type2.size(), type3.size());
        ArrayList<ServerCard> selectedCards = BioCornCombination(combination, bioCornCards);
        if (selectedCards.isEmpty() && quotient != 0) {
            return calculate(quotient - 1, Value - 5);
        } else return selectedCards; // Return the selected cards
    }

    public Map<String, Integer> getCombination() {
        return combination;
    }

    public Integer getEggPoints() {
        return eggPoints;
    }

    // toString method for debugging and displaying the optimal exchange result.
    @Override
    public String toString() {
        for (ServerCard card : bioCardCombination) {
            System.out.println(card.getServeCardID() + card.toString());
        }
        return "OptimalExchangeResult{" +
                "combination=" + combination +
                ", eggPoints=" + eggPoints +
                '}';
    }

    // Method to calculate optimal egg exchange combinations.
    public void calculateHelper(int bioCornValue, int type1Count, int type2Count, int type3Count) {

        int BIO_TO_EGG_CONVERSION_RATE = 5;
        int EGG_POINTS_PER_CONVERSION = 2;
        Map<String, Integer> optimalCombination = new HashMap<>();
        int optimalEggPoints = 0;

        // Generate all combinations
        for (int type1Used = 0; type1Used <= type1Count; type1Used++) {
            for (int type2Used = 0; type2Used <= type2Count; type2Used++) {
                for (int type3Used = 0; type3Used <= type3Count; type3Used++) {
                    int totalValue = type1Used + (type2Used * 2) + (type3Used * 3);
                    if (totalValue == bioCornValue) {
                        int currentEggPoints = (totalValue / BIO_TO_EGG_CONVERSION_RATE) * EGG_POINTS_PER_CONVERSION;
                        if (currentEggPoints > optimalEggPoints || (currentEggPoints == optimalEggPoints && totalValue < bioCornValue)) {
                            optimalCombination.clear();
                            optimalCombination.put("Type1", type1Used);
                            optimalCombination.put("Type2", type2Used);
                            optimalCombination.put("Type3", type3Used);
                            optimalEggPoints = currentEggPoints;
                        }
                    }
                }
            }
        }

        combination = optimalCombination;
        eggPoints = optimalEggPoints;
    }

    // Method to create a list of selected bioCorn cards based on the combination.
    public ArrayList<ServerCard> BioCornCombination(Map<String, Integer> combination, Map<UUID, ServerCard> bioCornCards) {
        ArrayList<ServerCard> selectedCards = new ArrayList<>();
        Map<String, Integer> typeCounter = new HashMap<>(combination);

        for (ServerCard card : bioCornCards.values()) {
            String type = "Type" + card.getValue(); // Assuming the card type is represented as "TypeX"
            if (typeCounter.containsKey(type) && typeCounter.get(type) > 0) {
                selectedCards.add(card);
                typeCounter.put(type, typeCounter.get(type) - 1);
            }
        }

        return selectedCards;
    }
}
