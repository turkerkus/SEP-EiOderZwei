package eoz.client.lobbyToTable;

import javax.swing.*;
import java.util.*;

public class Spiellogik {

    // Initialisierung
    Spieler spieler1 = new Spieler(1,"spieler1", 0, new ArrayList<Card>() , false );
    Spieler spieler2 = new Spieler(2,"spieler2", 0, new ArrayList<Card>(), false );
    Spieler spieler3 = new Spieler(3,"spieler3", 0, new ArrayList<Card>(), false );
    Spieler spieler4 = new Spieler(4,"spieler4", 0, new ArrayList<Card>(), false );
    Spieler spieler5 = new Spieler(5,"spieler5", 0, new ArrayList<Card>(), false );
    Spieler spieler6 = new Spieler(6,"spieler6", 0, new ArrayList<Card>(), true ); //Der letzte Spieler bekommt die Hahnkarte
    Spieler[] spielerArray = {spieler1, spieler2, spieler3, spieler4, spieler5, spieler6};

    int n = 0; //Zählvariable, nach 1000 Zügen terminiert es einfach

    Card hahnKarte= new Card(0,"Hahn",new ImageIcon(getClass().getResource("/cards/hahn.png").toString()),0);

    public Deck getKartendeck(){
        return  new Deck();
    }

    // Methoden
    public void SpielzugManager(){
        int i = 0;
        while (n < 1000) {              //Spätestens nach 1000 Durchläufen terminiert das Spiel (sollte eigentlich nie dadurch passieren)
            for (Spieler spieler : spielerArray) {              // Überprüft ob einer der Spieler gewonnen hat
                if (spieler.getPunkte() >= 5){
                    System.out.print(spieler.getName() + "hat gewonnen");
                    return;
                }
            }
            Spieler aktuellplayer = spielerArray[i];        //Die Reihenfolge ist abhängig von der Reihenfolge des Arrays, die aktuelle Position von i bestimmt den aktuellen Spieler
            SpielzugMoeglichkeiten(aktuellplayer);
            if (i > spielerArray.length-1){                 //Wenn das Ende es Arrays überschritten wird, gehen wir zurück zum ersten Spieler
                i=0;
            }
            else {
                i++;
            }
        }
    }

    private void SpielzugMoeglichkeiten(Spieler aktuelplayer) {
        for (Spieler spieler : spielerArray) {
            if (spieler.isHahnkarte()) {
                if (aktuelplayer.getPunkte() < spieler.getPunkte()){
                    if(aktuelplayer.getPunkte()>=5){
                        Spielzug(aktuelplayer,true,true);
                    }
                    else{
                        Spielzug(aktuelplayer,true,false);
                    }
                }
                else {
                    if(aktuelplayer.getPunkte()>=5){
                        Spielzug(aktuelplayer,false,true);
                    }
                    else{
                        Spielzug(aktuelplayer,false,false);
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
    private void Spielzug(Spieler aktuelspieler,boolean boolwert1,boolean boolwert2){          //Übergeben wird der aktuelle Spieler
        String antwort=spieleranfrage(boolwert1,boolwert2);                                   //Frage an spieler was getan werden soll
        if (antwort == "karte"){                                   //Wenn der Spieler Karte ziehen will wird die Kartenziehmethode aufgerufen
            zieheKarte(aktuelspieler);
            if (aktuelspieler.isHahnkarte()){                                   //Wenn die Hahnkarte im Besitz ist wird die Kartenziehmethode erneut aufgerufen
                zieheKarte(aktuelspieler);
            }
        }
        if (antwort=="umtauschen"){                              //Wenn der Spieler Koerner umtauschen will wird die Multiselectmethode aufgerufen
            multiselect(aktuelspieler);
            if (aktuelspieler.isHahnkarte()) {                                  //Wenn die Hahnkarte im Besitz ist wird die Kartenziehmethode aufgerufen
                zieheKarte(aktuelspieler);
            }
        }
        if (antwort=="hahnklau"){                                //Wenn der Spieler den Hahn klauen will wird Hahnklaumethode aufgerufen
            hahnklauen(aktuelspieler,hahnKarte);
        }
    }
    private void zieheKarte(Spieler spieler){
        Deck kartendeck = getKartendeck();
        Card temp = kartendeck.pop();                                         //Die oberste Karte wird gezogen und zwischengespeichert
        //kartendeck.remove(0);
        if (temp.getType() == "Fuchs"){                                           //Wenn die oberste Karte ein Fuchs ist wird die Methode fuchsklau aufgerufen
            fuchsklau();
        }
        if (temp.getType() == "Koerner"){                                            //Wenn die oberste Karte eine Kornkarte ist wird diese dem Spieler hinzugefügt
            spieler.add(temp);
        }
        if (temp.getType() == "Kuckuck") {                                        //Wenn die oberste Karte eine Kuckuckskarte ist erhält der Spieler einen Punkt
            spieler.add(temp);
            spieler.setPunkte(spieler.getPunkte()+ 1);

        }

    }
    private void multiselect(Spieler spieler){
        String antwort = umwandelnOptionen(spieler);
        if (antwort == "Biokornumwandlung"){
            biokornumwandlung(spieler);
        }
        if (antwort == "Gemischtumwandlung"){
            kornumwandlung(spieler);
        }

    }
    private String umwandelnOptionen(Spieler spieler) {
        List<Card> hand=spieler.getHand();
        int bioges=0;
        int gesamt=0;
        for (Card card : hand) {
            if ("Biokörner".equals(card.getType())) {
                bioges=+ card.getValue();
            }
        }
        for (Card card : hand) {
            if ("Koerner".equals(card.getType()) | "Biokörner".equals(card.getType())) {
                gesamt=+ card.getValue();
            }
        }
        if (bioges >= 5){
            System.out.println("1.)Biokörner umwandeln?\n2-)Körner gemischt umwandeln? \n3-)Nichts weiter umwandeln?");
            Scanner scan = new Scanner(System.in);
            int x = scan.nextInt();
            switch (x){
                case 1 :
                    return "Biokornumwandlung";

                case 2 :
                    return "Gemischtumwandlung";

                case 3 :
                    return "Nichts";

                default:
                    return "Ungültige Auswahl";
            }
        }
        if (gesamt >= 5) {
            System.out.println("1.)Körner gemischt umwandeln? \n2-)Nichts weiter umwandeln?");
            Scanner scan = new Scanner(System.in);
            int x = scan.nextInt();
            switch (x){
                case 1 :
                    return "Gemischtumwandlung";

                case 2 :
                    return "Nichts";

                default:
                    return "Ungültige Auswahl";
            }

        }
        return "Nichts";


    }
    private void biokornumwandlung(Spieler spieler){        //Die Methode wandelt Biokörner in Eier um

    }
    private int biokornauswahl(Spieler spieler){            //Die Methode sammelt die Biokartenauswahl vom Spieler
        List<Card> hand=spieler.getHand();
        Scanner scan = new Scanner(System.in);
        return scan.nextInt();

    }

    private void kornumwandlung(Spieler spieler){

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
    public void fuchsklau(){

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
