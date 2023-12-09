package eoz.client.lobbyToTable;

public class Deck {
    private  int  kartenAnzahl;
    private  final String [] type={"Koerner","Biokoerner","Fuch", "Kuckuck", "Hahn"};
    private  final  String []  value= {"0","1","2","3","4"};

    private Card [] deck ;

    //Consturtor
    public Deck(int kartenAnzahl, Card[] deste) {
        deck =new  Card[76];
        int index=1;

        for (int i=1; i<11; i++){
            deste [index++] =new Card(index,"Koerner" ,"2",2 );
            deste [index++] =new Card(index,"Koerner" ,"3",3 );
            deste [index++] =new Card(index,"Koerner" ,"4",4 );
            deste [index++] =new Card(index ,"Biokörner" ,"2",2);
            deste [index++] =new Card(index ,"Biokörner" ,"3",3);


        }
        for (int j=1 ;j< 9;j++){
            deste [index++] =new Card(index, "Biokörner" ,"1",1);
        }
        for (int k=1 ;k< 13;k++){
            deste [index++] =new Card( index,"Fuch" ,"0",0);
        }
        for (int l=1 ;l< 5;l++){
            deste [index++] =new Card(index, "Kuckuck" ,"0",0);
        }
        deste [index++] =new Card(index, "Hahn" ,"0",0);



    }
}
