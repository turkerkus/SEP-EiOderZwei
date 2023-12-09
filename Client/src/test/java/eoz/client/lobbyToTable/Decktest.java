package eoz.client.lobbyToTable;

public class Decktest {
    private  int  kartenAnzahl;
    private  final String [] koerner={"Koerner","Biokoerner"};
    private  final  String []  name= {"1","2","3","4","Fuch", "Kuckuck", "Hahn"};

    private  CardTest [] deck ;

    //Consturtor
    public Decktest(int kartenAnzahl, CardTest[] deste) {
        deck=new  CardTest[76];
        int index=1;

        for (int i=1; i<11; i++){
            deck [index++] =new CardTest("Koerner" ,"2" ,2);
            deck [index++] =new CardTest("Koerner" ,"3" ,3);
            deck [index++] =new CardTest("Koerner" ,"4" ,4);
            deck [index++] =new CardTest( "Biokörner" ,"2",2);
            deck [index++] =new CardTest( "Biokörner" ,"3",4);


        }
        for (int j=1 ;j< 9;j++){
            deck [index++] =new CardTest( "Biokörner" ,"1",1);
        }
        for (int k=1 ;k< 13;k++){
            deck [index++] =new CardTest( "Fuch" ,"0",0);
        }
        for (int l=1 ;l< 5;l++){
            deck [index++] =new CardTest( "Kuckuck" ,"0",0);
        }
        deck [index++] =new CardTest( "Hahn" ,"0",0);



    }
}

