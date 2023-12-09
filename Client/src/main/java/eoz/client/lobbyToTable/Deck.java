package eoz.client.lobbyToTable;

public class Deck {
    private  final String [] type={"Koerner","Biokoerner","Fuch", "Kuckuck", "Hahn"};
    private  final  String []  value= {"0","1","2","3","4"};


    public Deck( Card[] deck) {
        deck =new  Card[76];
        int index=1;

        for (int i=1; i<11; i++){
            deck[index++] =new Card(index,"Koerner" ,"2",2 );
            deck [index++] =new Card(index,"Koerner" ,"3",3 );
            deck [index++] =new Card(index,"Koerner" ,"4",4 );
            deck [index++] =new Card(index ,"Biokörner" ,"2",2);
            deck [index++] =new Card(index ,"Biokörner" ,"3",3);


        }
        for (int j=1 ;j< 9;j++){
            deck [index++] =new Card(index, "Biokörner" ,"1",1);
        }
        for (int k=1 ;k< 13;k++){
            deck [index++] =new Card( index,"Fuch" ,"0",0);
        }
        for (int l=1 ;l< 5;l++){
            deck [index++] =new Card(index, "Kuckuck" ,"0",0);
        }
        deck [index++] =new Card(index, "Hahn" ,"0",0);



    }
}
