package rmi;

import sharedClasses.ServerCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MixedCornCardCombinator implements Serializable {

    Map<UUID, ServerCard> mixedCards;

    Map<UUID, ServerCard> bioCards;
    Map<UUID, ServerCard> cornCards;


    ArrayList<ServerCard> mixedCardCombination;

    Integer mixedCardValue;
    Map<String, Integer> combination;
    private Integer eggPoints;


    public MixedCornCardCombinator(Map<UUID, ServerCard> mixedCards) {
        this.mixedCards = mixedCards;
        bioCards = new ConcurrentHashMap<>();
        cornCards = new ConcurrentHashMap<>();


        // Categorize bioCorn cards by type (type1, type2, type3).
        for (ServerCard serverCard : mixedCards.values()) {
            String cardType = serverCard.getType();

            if ("BioKoerner".equals(cardType)) {
                bioCards.put(serverCard.getServeCardID(), serverCard);
            } else if ("Koerner".equals(cardType)) {
                cornCards.put(serverCard.getServeCardID(), serverCard);
            }
        }

        // Calculate the bioCornValue based on the total values of each type.
        System.out.println("number of type 1 bio cards are: " + bioCards.size());
        System.out.println("number of type 2 bio cards are: " + cornCards.size());
        int totalValue = 0;
        // Calculate total value of bio cards
        for (ServerCard bioCard : bioCards.values()) {
            totalValue += bioCard.getValue();
        }

        // Calculate total value of corn cards
        for (ServerCard cornCard : cornCards.values()) {
            totalValue += cornCard.getValue();
        }
        mixedCardValue = (totalValue / 5) * 5;
        System.out.println("with totalValue of: " + totalValue + " the bio Corn value is: " + mixedCardValue);

        // Calculate optimal egg exchange combinations.
        mixedCardCombination = calculate(totalValue / 5, mixedCardValue);

    }

    // Main method for testing the BioCardCombinator class.
    public static void main(String[] args) {
        Map<UUID, ServerCard> bioCornCards = new ConcurrentHashMap<>();
        for (int i = 0; i < 3; i++) {
            bioCornCards.put(UUID.randomUUID(), new sharedClasses.ServerCard("Koerner", 1, true));
        }
        for (int i = 0; i < 3; i++) {
            bioCornCards.put(UUID.randomUUID(), new sharedClasses.ServerCard("BioKoerner", 2, true));
        }
        for (int i = 0; i < 3; i++) {
            bioCornCards.put(UUID.randomUUID(), new sharedClasses.ServerCard("Koerner", 3, true));
        }
        MixedCornCardCombinator bioCardCalculator = new MixedCornCardCombinator(bioCornCards);
        System.out.println(bioCardCalculator);
    }

    public ArrayList<ServerCard> calculate(Integer quotient, Integer Value) {
        calculateHelper(mixedCardValue, bioCards.size(), cornCards.size());
        ArrayList<ServerCard> selectedCards = mixedCombination(combination, mixedCards);
        if (selectedCards.isEmpty() && quotient != 0) {
            return calculate(quotient - 1, Value - 5);
        } else return selectedCards; // Return the selected cards
    }

    public void calculateHelper(int totalValue, int bioCornValue, int cornValue) {

        Map<String, Integer> optimalCombination = new HashMap<>();
        int optimalEggPoints = 0;
        int CORN_BIOCORNT_TO_EGG_CONVERSION_RATE = 5;

        for (int bioCornUsed = 0; bioCornUsed <= bioCornValue; bioCornUsed++) {
            for (int cornUsed = 0; cornUsed <= cornValue; cornUsed++) {
                int combinedValue = bioCornUsed + cornUsed;
                if (combinedValue == totalValue) {
                    int currentEggPoints = combinedValue / CORN_BIOCORNT_TO_EGG_CONVERSION_RATE;
                    if (currentEggPoints > optimalEggPoints || (currentEggPoints == optimalEggPoints && cornUsed > optimalCombination.getOrDefault("Corn", 0))) {
                        optimalCombination.clear();
                        optimalCombination.put("BioCorn", bioCornUsed);
                        optimalCombination.put("Corn", cornUsed);
                        optimalEggPoints = currentEggPoints;
                    }
                }
            }
        }

        combination = optimalCombination;
        eggPoints = optimalEggPoints;
    }

    // Method to create a list of selected bioCorn cards based on the combination.
    public ArrayList<ServerCard> mixedCombination(Map<String, Integer> combination, Map<UUID, ServerCard> bioCornCards) {
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

    public Map<String, Integer> getCombination() {
        return combination;
    }

    public Integer getEggPoints() {
        return eggPoints;
    }

    // toString method for debugging and displaying the optimal exchange result.
    @Override
    public String toString() {
        for (ServerCard card : mixedCardCombination) {
            System.out.println(card.getServeCardID() + card.toString());
        }
        return "OptimalExchangeResult{" +
                "combination=" + combination +
                ", eggPoints=" + eggPoints +
                '}';
    }

}

