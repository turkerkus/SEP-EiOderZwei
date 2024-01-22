package sharedClasses;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ServerPlayer implements Serializable {
    private boolean aussteigen = false;
    private boolean leftServer = false;
    private boolean cardDrawn = false;
    private int newCard;
    private final UUID serverPlayerId;


    public boolean isAussteigen() {
        return aussteigen;
    }

    public void setServerPlayerName(String serverPlayerName) {
        this.serverPlayerName = serverPlayerName;
    }

    private  String serverPlayerName;
    int punkte;
    Hand cardHand;

    private  boolean  hahnKarte;

    public boolean isBot() {
        return isBot;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }

    private boolean isBot = false;
    public ServerPlayer(UUID id, String playerName, boolean hahnKarte) {
        this.serverPlayerId = id;
        this.serverPlayerName = playerName;
        this.hahnKarte = hahnKarte;
        this.cardHand = new Hand();

    }



    public UUID getServerPlayerId() {
        return serverPlayerId;
    }

    public String getServerPlayerName() {
        return serverPlayerName;
    }


    public int getPunkte() {return punkte;}

    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }

    public void increasePointsBy(int punkte){
        this.punkte += punkte;
    }
    public void decreasePointsBy(int punkte){
        this.punkte -= punkte;
    }

    // Raises the Egg Counter by 1
    public void raisePunkte() {
        punkte += 1;
    }

    public int getBioKornzahl() {return cardHand.getNumOfBioCornCard();}

    public int getKornzahl(){return cardHand.getNumOfCornCard();}

    public ServerCard getKuckuckCard(){
        return cardHand.getKuckuck();
    }


    public Hand getCardHand() {
        return cardHand;
    }

    public int getCardCount(){
        return cardHand.size();
    }

    public void addCard( ServerCard serverCard) {
        cardHand.addCardToHand(serverCard);

    }

    public void remove(UUID cardID, String cardType) {
        cardHand.removeCard(cardID, cardType);
    }

    public boolean hatHahnKarte() {
        return hahnKarte;
    }

    public void setHahnKarte(boolean hahnKarte) {
        this.hahnKarte = hahnKarte;
    }

    public void einsteigen(){
        aussteigen = false;
    }

    public void aussteigen(){
        System.out.println("Aussteigen: " + aussteigen );
        aussteigen = true;
        System.out.println("in Game: " );
    }

    public boolean inGame(){
        return !aussteigen;
    }

    public boolean getLeftServer() { //Checks whether the player has left the game, true when play has left.
        return leftServer;
    }

    public void setLeftServer(boolean bool) {
        leftServer = bool;
    }


    @Override
    public int hashCode() {
        return Objects.hash(serverPlayerId);
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(serverPlayerName, ((ServerPlayer) obj).getServerPlayerName())
                && ((ServerPlayer)obj).getCardHand().equals(cardHand);
    }

    @Override
    public String toString() {
        return "(" + getServerPlayerName()+ " "+ getServerPlayerId()+ ")";
    }

}
