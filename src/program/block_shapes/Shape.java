package program.block_shapes;

import javafx.scene.image.Image;
import program.Constants;
import program.game.Block;

/**
 * Created by IntelliJ IDEA.
 * User: MichaelaGuth
 * Date: 23. 8. 2018
 * Time: 18:48
 */
public abstract class Shape {

    protected Block[][] shape;
    private int x = Constants.GAME_NUMBER_OF_COLUMNS / 2;
    private int y = Constants.INITIAL_COORDINATES;
    protected Image image;

    private final int SHAPE_SIZE = 4;

    public Shape(Image image) {
        this.image = image;
    }

    public Block[][] getShape() {
        return shape;
    }

    public void setShape(Block[][] shape) {
        this.shape = shape;
    }

    /**
     * Creates a matrix for rotation.
     * @return Matrix with coordinates.
     */
    public int[][] getBody() {

        int[][] coordinates = new int[2][4];
        int k = 0;
        Block[][] shape = getShape();

        for (int col = 0; col < shape[0].length; col++) {
            for (int row = 0; row < shape.length; row++) {

                Block block = shape[row][col];
                if (block != null) {
                    coordinates[0][k] = row;
                    coordinates[1][k] = col;
                    k++;
                }
            }
        }

        return coordinates;
    }

    /**
     * Creates a shape from matrix with coordinates.
     * @param coordinates The matrix with coordinates.
     * @return Shape in array of Blocks.
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
