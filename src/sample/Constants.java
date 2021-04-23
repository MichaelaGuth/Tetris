package sample;

/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 26. 8. 2018
 * Time: 18:28
 */
public class Constants {
    public static final int HRA_POCET_RADKU = 24;  // pocet radku herniho planu
    public static final int HRA_POCET_VIDITELNYCH_RADKU = 20;
    public static final int HRA_POCET_SLOUPCU = 10;  // pocet sloupcu herniho planu
    public static final int KOSTICKA_SIZE = 30;     // velikost jedne kosticky v pixelech
    public static final int POCET_KOSTICEK = 7;
    public static final int POCATECNI_RYCHLOST = 1000;
    public static final int POCATECNI_SOURADNICE = 0;
    public static final int POCET_BAREV = 5;

    public static final int SCORE_UMAZANI_RADKU = 100;
    public static final int[][] MATICE_OTOCENI = new int[][] {
        {0,-1},
        {1,0}
    };

    public static final int SCORE_LEVEL_2 = 2000;
    public static final int SCORE_LEVEL_3 = 5000;
    public static final int SCORE_LEVEL_4 = 10000;
    public static final int SCORE_LEVEL_5 = 20000;

    public static final int TIMER_LEVEL_1 = 1;
    public static final int TIMER_LEVEL_2 = 2;
    public static final int TIMER_LEVEL_3 = 3;
    public static final int TIMER_LEVEL_4 = 4;
    public static final int TIMER_LEVEL_5 = 5;
}
