package eoz.client.lobbyToTable;

import javax.swing.*;
import java.util.*;

public class Spiellogik {
    // Initialisierung

    Spieler spieler1 = new Spieler(1, "spieler1", 0, 0, new ArrayList<Card>(), false);
    Spieler spieler2 = new Spieler(2, "spieler2", 0, 0, new ArrayList<Card>(), false);
    Spieler spieler3 = new Spieler(3, "spieler3", 0, 0, new ArrayList<Card>(), false);
    Spieler spieler4 = new Spieler(4, "spieler4", 0, 0, new ArrayList<Card>(), false);
    Spieler spieler5 = new Spieler(5, "spieler5", 0, 0, new ArrayList<Card>(), false);
    Spieler spieler6 = new Spieler(6, "spieler6", 0, 0, new ArrayList<Card>(), true); //Der letzte Spieler bekommt die Hahnkarte
    Spieler[] spielerArray = {spieler1, spieler2, spieler3, spieler4, spieler5, spieler6};

    int n = 0; //Zählvariable, nach 1000 Zügen terminiert es einfach

    Card hahnKarte = new Card(0, "Hahn", new ImageIcon(getClass().getResource("/cards/hahn.png").toString()), 0);

    public Deck getKartendeck() {
        return new Deck();
    }

    public Ablagestapel getAblagestapel() {
        return new Ablagestapel();
    }

    Ablagestapel ablagestapel = getAblagestapel();


    // Methoden
    public Boolean SpielzugManager(List<Spieler1> spielerArray, Table table) {
        int i = 0;
        if (table.getMoveCount() < 1000) {              //Spätestens nach 1000 Durchläufen terminiert das Spiel (sollte eigentlich nie dadurch passieren)
            for (Spieler1 spieler : spielerArray) {              // Überprüft ob einer der Spieler gewonnen hat
                if (spieler.getPunkte() >= 5) {
                    // TODO : Remove this after the SWITCH TO SCORE BOARD OR SHOW A DIALOGBOX is implemented in the startPlayerTurn() in tableController
                    // this is just a placeholder
                    System.out.print(spieler.getPlayerName() + "hat gewonnen");
                    return true;
                }
            }

        }
        return false;
    }

    private void SpielzugMoeglichkeiten(Spieler aktuelplayer) {
        for (Spieler spieler : spielerArray) {
            if (spieler.isHahnkarte()) {
                if (aktuelplayer.getKornzahl() < spieler.getKornzahl()) {
                    if (aktuelplayer.getKornzahl() >= 5) {
                        Spielzug(aktuelplayer, true, true);
                    } else {
                        Spielzug(aktuelplayer, true, false);
                    }
                } else {
                    if (aktuelplayer.getKornzahl() >= 5) {
                        Spielzug(aktuelplayer, false, true);
                    } else {
                        Spielzug(aktuelplayer, false, false);
                    }

                }

            }
        }
        //if (punkte.Spieler < punkte.Hahnkarte)  {           //Wenn der aktuelle Spieler weniger Punkte hat als der Spieler mit der Hahnkarte, bekommt er die Möglichkeit diese zu nehmen

        //    if (sumKoerner >= 5){                           //Wenn der aktuelle Spieler mindestens fünf Körner hat bekommt er die Möglichkeit diese einzutauschen
        //        Spielzug(Spieler, Hahnkarte,true,true);                        //Spielzug wird ausgeführt mit den Werten: true für Hahnkarte klauen, true für Karten eintauschen
        //    }
        //    else{                                           //Wenn der aktuelle Spieler weniger als fünf Körner hat bekommt er NICHT die Möglichkeit diese einzutauschen
        //        Spielzug(Spieler, Hahnkarte,true,false);                       //Spielzug wird ausgeführt mit den Werten: true für Hahnkarte klauen, false für Karten eintauschen
        //    }
        // }
        // else{                                               //Wenn der aktuelle Spieler gleich viele oder mehr Punkte hat als der Spieler mit der Hahnkarte, bekommt er NICHT die Möglichkeit diese zu nehmen

        //    if (sumKoerner >= 5){                           //Wenn der aktuelle Spieler mindestens fünf Körner hat bekommt er die Möglichkeit diese einzutauschen
        //        Spielzug(Spieler, Hahnkarte,false,true);                       //Spielzug wird ausgeführt mit den Werten: false für Hahnkarte klauen, true für Karten eintauschen
        //    }
        //    else{                                           //Wenn der aktuelle Spieler weniger als fünf Körner hat bekommt er NICHT die Möglichkeit diese einzutauschen
        //        Spielzug(Spieler, Hahnkarte,false,false);                      //Spielzug wird ausgeführt mit den Werten: false für Hahnkarte klauen, false für Karten eintauschen
        //    }
        //}
    }

    private void Spielzug(Spieler aktuelspieler, boolean boolwert1, boolean boolwert2) {          //Übergeben wird der aktuelle Spieler
        String antwort = spieleranfrage(boolwert1, boolwert2);                                   //Frage an spieler was getan werden soll
        if (antwort == "karte") {                                   //Wenn der Spieler Karte ziehen will wird die Kartenziehmethode aufgerufen
            zieheKarte(aktuelspieler);
            if (aktuelspieler.isHahnkarte()) {                                   //Wenn die Hahnkarte im Besitz ist wird die Kartenziehmethode erneut aufgerufen
                zieheKarte(aktuelspieler);
            }
        }
        if (antwort == "umtauschen") {                              //Wenn der Spieler Koerner umtauschen will wird die Multiselectmethode aufgerufen
            multiselect(aktuelspieler);
            if (aktuelspieler.isHahnkarte()) {                                  //Wenn die Hahnkarte im Besitz ist wird die Kartenziehmethode aufgerufen
                zieheKarte(aktuelspieler);
            }
        }
        if (antwort == "hahnklau") {                                //Wenn der Spieler den Hahn klauen will wird Hahnklaumethode aufgerufen
            hahnklauen(aktuelspieler, hahnKarte);
        }
    }

    private void zieheKarte(Spieler spieler) {
        Deck kartendeck = getKartendeck();
        Card temp = kartendeck.pop();                                         //Die oberste Karte wird gezogen und zwischengespeichert
        //kartendeck.remove(0);
        if (temp.getType() == "Fuchs") {                                           //Wenn die oberste Karte ein Fuchs ist wird die Methode fuchsklau aufgerufen
            fuchsklau(spieler);
        }
        if (temp.getType() == "Koerner") {                                            //Wenn die oberste Karte eine Kornkarte ist wird diese dem Spieler hinzugefügt
            spieler.add(temp);
            int xyz = spieler.getKornzahl() + temp.getValue();
            spieler.setKornzahl(xyz);

        }
        if (temp.getType() == "Kuckuck") {                                        //Wenn die oberste Karte eine Kuckuckskarte ist erhält der Spieler einen Punkt
            spieler.add(temp);
            spieler.setPunkte(spieler.getPunkte() + 1);

        }

    }

    private void multiselect(Spieler spieler) {
        String antwort = umwandelnOptionen(spieler);
        if (antwort == "Biokornumwandlung") {
            biokornumwandlung(spieler);
        }
        if (antwort == "Gemischtumwandlung") {
            kornumwandlung(spieler);
        }

    }

    private String umwandelnOptionen(Spieler spieler) {
        List<Card> hand = spieler.getHand();
        int bioges = 0;
        int gesamt = 0;
        for (Card card : hand) {
            if ("Biokörner".equals(card.getType())) {
                bioges = +card.getValue();
            }
        }
        for (Card card : hand) {
            if ("Koerner".equals(card.getType()) | "Biokörner".equals(card.getType())) {
                gesamt = +card.getValue();
            }
        }
        if (bioges >= 5) {
            System.out.println("1.)Biokörner umwandeln?\n2-)Körner gemischt umwandeln? \n3-)Nichts weiter umwandeln?");
            Scanner scan = new Scanner(System.in);
            int x = scan.nextInt();
            switch (x) {
                case 1:
                    return "Biokornumwandlung";

                case 2:
                    return "Gemischtumwandlung";

                case 3:
                    return "Nichts";

                default:
                    return "Ungültige Auswahl";
            }
        }
        if (gesamt >= 5) {
            System.out.println("1.)Körner gemischt umwandeln? \n2-)Nichts weiter umwandeln?");
            Scanner scan = new Scanner(System.in);
            int x = scan.nextInt();
            switch (x) {
                case 1:
                    return "Gemischtumwandlung";

                case 2:
                    return "Nichts";

                default:
                    return "Ungültige Auswahl";
            }

        }
        return "Nichts";


    }

    private void biokornumwandlung(Spieler spieler) {        //Die Methode wandelt Biokörner in Eier um
        List<Card> hand = spieler.getHand();
        List<Card> x = biokornauswahl(hand);
        List<Card> unterschied = new ArrayList<>(hand);         // Unterschied reproduziert die Karten als Liste die herausgenommen wurden
        unterschied.removeAll(x);
        int biokornmenge = 0;
        for (int i = 0; i <= unterschied.size(); i++) { //bikörner zahl durch 5 teilbar ist ->fügen wir keine Karte ablagestapel
            Card a = unterschied.get(i);
            biokornmenge = biokornmenge + a.getValue();
        }
        if(biokornmenge<5){
        System.out.println("Ungültige Auswahl! Wähle erneut aus");
        biokornumwandlung(spieler);
       }
       else{
            int size = unterschied.size();
            ablagestapel.pushCardAblagestapel(unterschied.get(size - 1));//nur letzte Element geht Ablagestapel
            spieler.setHand(x);
            int eier = (int) Math.floor(biokornmenge / 5);          // Sieht komisch aus i dunno
            spieler.setPunkte(eier*2+spieler.getPunkte());
        }
    }










    private List<Card> biokornauswahl(List<Card> tmphand) {            //Die Methode sammelt die Biokartenauswahl vom Spieler
        Scanner scan = new Scanner(System.in);
        int x = scan.nextInt();
        if (x == 1 ) {
            for(int i = 0; i <= tmphand.size(); i++){
                Card a = tmphand.get(i);
                if (a.getValue() == 1 && Objects.equals(a.getType(), "Biokörner")){
                    tmphand.remove(i);                                                          //Von der temporären Hand muss ein Biokorn mit value 1 abgezogen werden
                    biokornauswahl(tmphand);
                    return tmphand;
                }
            }

        }

        if (x == 2 ) {
            for(int i = 0; i <= tmphand.size(); i++){
                Card a = tmphand.get(i);
                if (a.getValue() == 2 && Objects.equals(a.getType(), "Biokörner")){
                    tmphand.remove(i);                                                          //Von der temporären Hand muss ein Biokorn mit value 1 abgezogen werden
                    biokornauswahl(tmphand);
                    return tmphand;
                }
            }

        }

        if (x == 3 ) {
            for(int i = 0; i <= tmphand.size(); i++){
                Card a = tmphand.get(i);
                if (a.getValue() == 3 && Objects.equals(a.getType(), "Biokörner")){
                    tmphand.remove(i);                                                          //Von der temporären Hand muss ein Biokorn mit value 1 abgezogen werden
                    biokornauswahl(tmphand);
                    return tmphand;
                }
            }

        }
        return new ArrayList<>();
    }


    private void kornumwandlung(Spieler spieler){//Die Methode wandelt Körner jeder Art in Eier um
        List<Card> hand = spieler.getHand();
        List<Card> x = kornauswahl(hand);
        List<Card> unterschied = new ArrayList<>(hand);         // Unterschied reproduziert die Karten als Liste die herausgenommen wurden
        unterschied.removeAll(x);
        int biokornmenge = 0;
        for (int i = 0; i <= unterschied.size(); i++) { //bikörner zahl durch 5 teilbar ist ->fügen wir keine Karte ablagestapel
            Card a = unterschied.get(i);
            biokornmenge = biokornmenge + a.getValue();
        }
        if(biokornmenge<5){
            System.out.println("Ungültige Auswahl! Wähle erneut aus");
            kornumwandlung(spieler);
        }
        else{
            int size = unterschied.size();
            ablagestapel.pushCardAblagestapel(unterschied.get(size - 1));//nur letzte Element geht Ablagestapel
            spieler.setHand(x);
            int eier = (int) Math.floor(biokornmenge / 5);          // Sieht komisch aus i dunno
            spieler.setPunkte(eier+spieler.getPunkte());
        }



    }
    private List<Card> kornauswahl(List<Card> tmphand) {            //Die Methode sammelt die Kornkartenauswahl vom Spieler
        Scanner scan = new Scanner(System.in);
        int x = scan.nextInt();
        if (x == 1 ) {
            for(int i = 0; i <= tmphand.size(); i++){
                Card a = tmphand.get(i);
                if (a.getValue() == 1 && Objects.equals(a.getType(), "Biokörner")){
                    tmphand.remove(i);                                                          //Von der temporären Hand muss ein Korn mit value 1 abgezogen werden
                    kornauswahl(tmphand);
                    return tmphand;
                }
            }

        }

        if (x == 2 ) {
            for(int i = 0; i <= tmphand.size(); i++){
                Card a = tmphand.get(i);
                if (a.getValue() == 2 && ((Objects.equals(a.getType(), "Biokörner")) ||(Objects.equals(a.getType(), "Körner")))){
                    tmphand.remove(i);                                                          //Von der temporären Hand muss ein Korn mit value 1 abgezogen werden
                    kornauswahl(tmphand);
                    return tmphand;
                }
            }

        }

        if (x == 3 ) {
            for(int i = 0; i <= tmphand.size(); i++){
                Card a = tmphand.get(i);
                if (a.getValue() == 3 && ((Objects.equals(a.getType(), "Biokörner")) ||(Objects.equals(a.getType(), "Körner")))){
                    tmphand.remove(i);                                                          //Von der temporären Hand muss ein Korn mit value 1 abgezogen werden
                    kornauswahl(tmphand);
                    return tmphand;
                }
            }

        }
        if (x == 4 ) {
            for(int i = 0; i <= tmphand.size(); i++){
                Card a = tmphand.get(i);
                if (a.getValue() == 4 && Objects.equals(a.getType(), "Körner")){
                    tmphand.remove(i);                                                          //Von der temporären Hand muss ein Korn mit value 1 abgezogen werden
                    kornauswahl(tmphand);
                    return tmphand;
                }
            }

        }
        return new ArrayList<>();
    }

    private String spieleranfrage(boolean hahn ,boolean tausch){
        if (hahn && tausch) {
            System.out.println(" Bitte wählen Sie eine der folgenden Optionen (Geben Sie nur die Nummer der gewählten Option an)\nSie haben folgende Möglichkeiten:");
            System.out.println("1.) Karte umtauschen\n2-) Neue Karte ziehen\n3-)Hahn klauen");
            Scanner scan = new Scanner(System.in);
            int x = scan.nextInt();
            switch (x){
                case 1 :
                    return "umtauschen";

                case 2 :
                    return "karte";

                case 3 :
                    return "hahnklau";

                default:
                    return "Ungültige Auswahl";
            }
        } else if (hahn && !tausch) {
            System.out.println(" Bitte wählen Sie eine der folgenden Optionen (Geben Sie nur die Nummer der gewählten Option an)\nSie haben folgende Möglichkeiten:");
            System.out.println("1-) Neue Karte ziehen\n2-)Hahn klauen");
            Scanner scan = new Scanner(System.in);
            int x = scan.nextInt();
            switch (x){
                case 1 :
                    return "karte";

                case 2 :
                    return "hanhklau";

                default:
                    return "Ungültige Auswahl";
            }
        } else if (!hahn && tausch) {
            System.out.println(" Bitte wählen Sie eine der folgenden Optionen (Geben Sie nur die Nummer der gewählten Option an)\nSie haben folgende Möglichkeiten:");
            System.out.println("1-) Neue Karte ziehen\n2-)Karte umtauschen");
            Scanner scan = new Scanner(System.in);
            int x = scan.nextInt();
            switch (x){
                case 1 :
                    return "karte";

                case 2 :
                    return "umtauschen";

                default:
                    return "Ungültige Auswahl";
            }
        } else {
            return "karte";
        }



    }
    private void hahnklauen(Spieler aktuelSpieler,Card hahnKarte){
        aktuelSpieler.add(hahnKarte);
        aktuelSpieler.setHahnkarte(true);

    }
    ArrayList<Spieler> spielerListe = new ArrayList<>();
    public void fuchsklau(Spieler spieler) {
        for (Spieler players : spielerArray) {
            List<Card> hand = players.getHand();
            if (!hand.isEmpty() && players!=spieler){
                spielerListe.add(players);

                }
            }
        if (spielerListe.isEmpty()){
            System.out.println("Es gibt keine offene Karte");
        }
        else {
            System.out.println("Sie können eine von folgenden Spieler wählen :(Bitte geben Sie Nummer von gewählten Spieler");
            for (Spieler x : spielerListe){
                System.out.println(x.getId() + " " + x.getName());

            }
            Scanner scan = new Scanner(System.in);
            int a = scan.nextInt();
            Spieler gewaehlter = null;
            for (Spieler spiel : spielerListe) {
                if (spiel.getId() == a) {
                    gewaehlter = spiel;
                    break; // Brechen Sie die Schleife ab, sobald der Spieler gefunden wurde
                }
            }
            System.out.println("Wählen Sie eine von dieser Optionen\n 1-)eine beliebige Karte \n2-)alle bis auf eine \n");
            Scanner scanb= new Scanner(System.in);
            int b=scanb.nextInt();
            switch(b){
                case 1:
                    Card gewaehlteKarte= new Card(0,"gewaehlte karte",new ImageIcon(getClass().getResource("/cards/hahn.png").toString()),0);//Javafx
                    spieler.add(gewaehlteKarte);
                    int asdf = spieler.getKornzahl() + gewaehlteKarte.getValue();
                    spieler.setKornzahl(asdf);
                    gewaehlter.remove(gewaehlteKarte);
                    int asdfgh = gewaehlter.getKornzahl() - gewaehlteKarte.getValue();
                    gewaehlter.setKornzahl(asdfgh);


                case 2:
                    //Opferauswahl(){                   //Serveranfrage erstellen
                    //TODO
                }
            }


        }




    public  void  eilegen(Spieler spieler){ //noch nicht fertig
        List<Card> hand=spieler.getHand();
        int bioges=0;
        int gesamt=0;
        for (Card card : hand) {
            if ("Biokörner".equals(card.getType())) {
                bioges=+ card.getValue();
            }
        }
        int bioei=(bioges/5)*2;
        for (Card card : hand) {
            if ("Koerner".equals(card.getType()) | "Biokörner".equals(card.getType())) {
                gesamt=+ card.getValue();
            }
        }
        int korner=gesamt/5;
        System.out.println(" Bitte wählen Sie eine der folgenden Optionen (Geben Sie nur die Nummer der gewählten Option an)\nSie haben folgende Möglichkeiten:");
        System.out.println("1.)Sie können Ihre Körnerkarten in" +bioei+ "Eier umwandeln \n2-)oder Sie können Körnerkarten in" +korner+ "Eier umwandeln\n");
        Scanner scan = new Scanner(System.in);
        int x = scan.nextInt();


    }
}
