package program.score;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 26. 8. 2018
 * Time: 12:06
 */
public class Score {
    private String fileName;

    public Score(String fileName){
        this.fileName = fileName;
    }

    /**
     * uloží dané skóre do souboru
     * @param highScore skóre
     * @param playerName jméno hráče
     */
    public void SaveHighScore(int highScore, String playerName) {
        FileWriter fw;                                                      //FileWriter je třída, která umožňuje zápis dat do textového souboru
        try {                                                                //try-catch blok pro odchytávání vyjimek v programu. Zde v kodu muze vyjimku vyhodit FileWriter
            fw = new FileWriter(fileName, true);                //Vytvoření instance třídy FileWriter (append = jestli soubor již existuje, bude pokračovat na konci tohoto souboru)
            fw.write(playerName+": " + highScore + "\n");               //metoda FileWriter pro uloženi textu do souboru ("\n" = znak pro konec radku)
            fw.flush();                                                     //Metoda flush() se používá pro okamžité vepsání do souboru
            fw.close();                                                     //Metoda close() se používá pro ukončení zápisu do souboru
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * přečte textový soubor, uloží ho do textového pole
     * @return textové pole HighScore
     */
    ArrayList<String> loadScore() {
        BufferedReader br;                                              // Trida usnadnujici cteni ze souboru, ale pro pouziti potrebuje jeste nejakou tridu Reader jako je FileReader
        ArrayList<String> scores = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(fileName));      //Vytvoreni instance tridy BufferedReader, ktery pro cteni ze souboru bude pouzivat instanci tridy FileReader
            String radek;
            while ((radek=br.readLine())!=null) {
                scores.add(radek);
            }
            br.close();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return scores;
    }

    /**
     * rozdělí line na dvě části podle ": "
     * @param line řádek, který chceme rozdělit
     * @return rozdělený řádek v textovém poli
     */
    String[] split(String line) {
        return line.split(": ");
    }

}
