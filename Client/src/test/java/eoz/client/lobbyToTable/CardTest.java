package eoz.client.lobbyToTable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    private  final String koerner;  //Biokörner  ,NormalKörner
    private  final String name; // 1,2,3,4,Fuchs,Kuckucks,Hahn
    private final  int value;
    //alternativ als enum
    //enum Bio  { //enum weil es aus vorbestimmte Sache  enstehen
    //    BioKoerner, Koerner;
     //   private  static  final Bio[] bios= Bio.values(); //erstellen eine Array bio und fügen values
    //    public  static Bio getBio(int i) { //wen wir 0 geben würden ,würde es uns BioKoerner zurückgeben für 1 Koerner
            //usw
    //        return Bio.bios[i];
     //   }

 //   }

    public CardTest(String koerner , String name,int value){
        this.koerner=koerner;
        this.name=name;
        this.value=value;

    }


    @Override
    public String toString() {
        return "Alter{" +
                "koerner='" + koerner + '\'' +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getKoerner() {
        return koerner;
    }


    @Test
    void getID() {
    }

    @Test
    void getType() {
    }

    @Test
    void getImage() {
    }


}