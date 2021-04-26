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
    protected final int SHAPE_SIZE = 4;

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

    /**
     * TODO
     * @return
     */
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

    /**
     * TODO
     * @param coordinates
     * @return
     */
    public Block[][] createShape(int[][] coordinates) {

        Block[][] shape = new Block[SHAPE_SIZE][SHAPE_SIZE];

        for (int col = 0; col < coordinates[0].length; col++) {
            shape[coordinates[0][col]][coordinates[1][col]] = new Block(image);
        }

        return shape;
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
