package program;

/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 26. 8. 2018
 * Time: 18:28
 */
public class Constants {
    public static final int GAME_NUMBER_OF_LINES = 24;  // pocet radku herniho planu
    public static final int GAME_NUMBER_OF_VISIBLE_LINES = 20;
    public static final int GAME_NUMBER_OF_COLUMNS = 10;  // pocet sloupcu herniho planu
    public static final int BLOCK_SIZE = 30;     // velikost jedne kosticky v pixelech
    public static final int NUMBER_OF_BLOCKS = 7;
    public static final int INITIAL_FALLING_SPEED = 1000;
    public static final int INITIAL_COORDINATES = 0;
    public static final int NUMBER_OF_COLORS = 5;

    public static final int SCORE_DELETED_LINE = 100;
    public static final int[][] ROTATION_MATRIX = new int[][] {
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
