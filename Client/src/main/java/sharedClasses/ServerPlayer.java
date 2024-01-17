package sharedClasses;


import java.io.Serializable;
import java.util.List;
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

    private  String serverPlayerName;
    int punkte;
    Hand cardHand;
    private int kornzahl;
    private List<ServerCard> hand;
    private  boolean  hahnKarte;

    public boolean isBot() {
        return isBot;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }

    private boolean isBot = false;
    public ServerPlayer(UUID id, String playerName, boolean hahnKarte, int kornzahl) {
        this.serverPlayerId = id;
        this.serverPlayerName = playerName;
        this.hahnKarte = hahnKarte;
        this.kornzahl = kornzahl;

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

    public int getKornzahl() {return kornzahl;}

    public void setKornzahl(int kornzahl) {this.kornzahl += kornzahl;}

    public Hand getCardHand() {
        return cardHand;
    }

    public int getCardCount(){
        return cardHand.getHandCards().size();
    }

    public void add( ServerCard serverCard) {
        hand.add(serverCard);
    }

    public void remove(ServerCard serverCard) { hand.remove(serverCard); }

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

    public int getNewCard(){
        return newCard;
    }
    public boolean getCardDrawn(){
        return cardDrawn;
    }

    public void setCardDrawn(boolean cardDrawn) {
        this.cardDrawn = cardDrawn;
    }

    public void setNewCard(int newCard) {
        this.newCard = newCard;
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
