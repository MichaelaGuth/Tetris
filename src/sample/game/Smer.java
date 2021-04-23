package sample.game;

/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 26. 8. 2018
 * Time: 21:59
 */
public enum Smer {
    DOLU(0, 1),
    DOLEVA(-1, 0),
    DOPRAVA(1, 0),
    NIC(0,0);

    private int x;
    private int y;

    Smer(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
