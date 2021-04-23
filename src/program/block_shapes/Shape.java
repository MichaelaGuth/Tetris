package program.block_shapes;

import javafx.scene.image.Image;
import program.Constants;
import program.game.Block;

/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 23. 8. 2018
 * Time: 18:48
 */
public abstract class Shape {
    protected Block[][] shape;
    protected int x = Constants.GAME_NUMBER_OF_COLUMNS / 2;
    protected int y = Constants.INITIAL_COORDINATES;

    public Shape(Image image) {
        this.image = image;
    }

    public Block[][] getShape() {
        return shape;
    }

    protected Image image;

    public void setShape(Block[][] shape) {
        this.shape = shape;
    }

    public int[][] getBody() {
        int[][] souradnice = new int[2][4];
        int k = 0;
        Block[][] kostka = getShape();
        for (int j = 0; j<kostka[0].length; j++) {
            for (int i = 0; i<kostka.length; i++) {
                Block bod = kostka[i][j];
                if (bod != null) {
                    souradnice[0][k] = i;
                    souradnice[1][k] = j;
                    k++;
                }
            }
        }
        return souradnice;
    }

    public Block[][] createTvar(int[][] souradnice) {
        Block[][] tvar = new Block[4][4];      //TODO 4 = konstanta
        for (int sloupec = 0; sloupec < souradnice[0].length; sloupec++) {
            tvar[souradnice[0][sloupec]][souradnice[1][sloupec]] = new Block(image);
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
