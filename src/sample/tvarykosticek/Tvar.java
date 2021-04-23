package sample.tvarykosticek;

import javafx.scene.image.Image;
import sample.Constants;
import sample.game.Kosticka;

/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 23. 8. 2018
 * Time: 18:48
 */
public abstract class Tvar {
    protected Kosticka[][] tvar;
    protected int x = Constants.HRA_POCET_SLOUPCU / 2;
    protected int y = Constants.POCATECNI_SOURADNICE;

    public Tvar(Image image) {
        this.image = image;
    }

    public Kosticka[][] getTvar() {
        return tvar;
    }

    protected Image image;

    public void setTvar(Kosticka[][] tvar) {
        this.tvar = tvar;
    }

    public int[][] getBody() {
        int[][] souradnice = new int[2][4];
        int k = 0;
        Kosticka[][] kostka = getTvar();
        for (int j = 0; j<kostka[0].length; j++) {
            for (int i = 0; i<kostka.length; i++) {
                Kosticka bod = kostka[i][j];
                if (bod != null) {
                    souradnice[0][k] = i;
                    souradnice[1][k] = j;
                    k++;
                }
            }
        }
        return souradnice;
    }

    public Kosticka[][] createTvar(int[][] souradnice) {
        Kosticka[][] tvar = new Kosticka[4][4];      //TODO 4 = konstanta
        for (int sloupec = 0; sloupec < souradnice[0].length; sloupec++) {
            tvar[souradnice[0][sloupec]][souradnice[1][sloupec]] = new Kosticka(image);
        }
        return tvar;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
