package program.game;

import javafx.scene.image.Image;
import program.block_shapes.*;

import java.util.Map;
import java.util.Random;

import static program.Constants.NUMBER_OF_COLORS;
import static program.Constants.NUMBER_OF_BLOCKS;

/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 26. 8. 2018
 * Time: 18:10
 */
public class GameUtils {

    /**
     * TODO
     * zkopíruje pole kostiček
     * @param src
     * @return
     */
    public static Block[][] copy(Block src[][]) {
        Block[][] copy = new Block[src.length][src[0].length];

        for (int i = 0; i < src.length; i++) {

            for (int j = 0; j < src[0].length; j++) {
                copy[i][j] = src[i][j];
            }
        }

        return copy;
    }

    /**
     * TODO
     */
    public enum blockInsertStatus {
        OK, COLLISION_WITH_WALL, COLLISION_WITH_END, COLLISION_WITH_OTHER_BLOCK_FROM_SIDE
    }

      /**
     * pokusi se vlozit kostku do hraciho pole
     * @param kostka kostka
     * @param zdroj puvodni hraci pole
     * @param novePole hraci pole pro vlozeni kostky
     * @param direction direction posunu
     * @return
     */
    public static blockInsertStatus insertBlock(Shape kostka, Block[][] zdroj, Block[][] novePole, Direction direction) {

        int x = kostka.getX() + direction.getX(); //změna souřednic
        int y = kostka.getY() + direction.getY();

        blockInsertStatus status = blockInsertStatus.OK;

        loop: for (int radek = 0; radek < kostka.getShape().length; radek++) {
            for (int sloupec = 0; sloupec < kostka.getShape()[radek].length; sloupec++) {

                if (kostka.getShape()[radek][sloupec] == null) {     // kontrola zda ma kostka na danem miste kosticku
                    /*
                     * Aby se kostka nezasekla prazdnym mistem, tak jej ignorovat.
                     * [.][.]
                     *    [.][.]       [.][.]
                     *                    [.][.]
                     * [.]        =>   [.]
                     * [.]             [.]
                     * [.]             [.]
                     * [.]             [.]
                     */
                    continue;
                }

                if (sloupec + x >= zdroj[0].length || sloupec + x < 0) {                    //kontrola jestli kostka nenarazila na levou nebo pravou stěnou
                    status = blockInsertStatus.COLLISION_WITH_WALL;
                    break loop;
                }

                if (radek + y == zdroj.length) {                                            //kontrola jestli kostka nedopadla ke spodní hraně
                    status = blockInsertStatus.COLLISION_WITH_END;
                    break loop;
                }

                if (zdroj[radek + y][sloupec + x] == null) {                                //kontrola jestli na místě kam se má posunout kostka je místo
                    novePole[radek + y][sloupec + x] = kostka.getShape()[radek][sloupec];
                } else {
                    if (direction != Direction.DOWN) {
                        status = blockInsertStatus.COLLISION_WITH_OTHER_BLOCK_FROM_SIDE;
                    } else {
                        status = blockInsertStatus.COLLISION_WITH_END;
                    }
                    break loop;
                }
            }
        }

        return status;
    }

    /**
     * vynasobi 2 matice
     * @param matrix1
     * @param matrix2
     * @return
     */
    public static int[][] matrixMultiplication(int[][] matrix1, int[][] matrix2) {
        int[][] newMatrix = new int[matrix1.length][matrix2[0].length];
        int j = 0;
        for (int newMatrixLine = 0; newMatrixLine < newMatrix.length; newMatrixLine++) {
            for (int newMatrixColumn = 0; newMatrixColumn < newMatrix[0].length; newMatrixColumn++) {
                for (int i = 0; i < matrix1[0].length; i++) {
                    j = j + matrix1[newMatrixLine][i] * matrix2[i][newMatrixColumn];
                }
                newMatrix[newMatrixLine][newMatrixColumn] = j;
                j = 0;
            }
        }
        return newMatrix;
    }

    /**
     * umaze lineNumber z matice
     * @param lineNumber lineNumber ke smazani
     * @param gameBoard
     */
    public static void deleteLine(int lineNumber, Block[][] gameBoard) {
        for (int col = 0; col < gameBoard[0].length; col++) {
            gameBoard[lineNumber][col] = null;
        }
    }

    /**
     * posune kosticky, tak aby se vyplnil prazdny radek
     * @param emptyLine souradnice prazdneho radku
     * @param gameBoard hraci pole
     * @return
     */
    public static Block[][] moveTheRestBlocksDown(int emptyLine, Block[][] gameBoard) {
        Block[][] newGameBoard = GameUtils.copy(gameBoard);

        for (int line = emptyLine - 1; line > 0; line--) {
            for (int col = 0; col < gameBoard[0].length; col++) {
                newGameBoard[line+1][col] = gameBoard[line][col];
            }
        }
        return newGameBoard;
    }

    /**
     * zkontroluje, jestli neni gameOver
     * @param gameBoard hraci pole ke kontrole
     * @return true pokud nastal gameOver jinak false
     */
    public static boolean checkGameOver(Block[][] gameBoard) {
        for (int col = 0; col < gameBoard[0].length; col++) {
            if (gameBoard[4][col] != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * vygeneruje náhodnou kostičku
     * @param blocksImages mapa pro obrázky kostiček
     * @return vrací náhodnou kostičku
     */
    public static Shape generateRandomBlock(Map<BlockEnum, Image> blocksImages) {
        Random random = new Random();
        int randomIndex = random.nextInt(NUMBER_OF_BLOCKS);
        Image randomColor = randomColor(blocksImages);  //nastaví náhodnou barvu pro kosticky

        switch (randomIndex) {
            case 0:
                return new Square(randomColor);
            case 1:
                return new MirrorL(randomColor);
            case 2:
                return new NormalL(randomColor);
            case 3:
                return new ShapeT(randomColor);
            case 4:
                return new Tube(randomColor);
            case 5:
                return new MirrorZ(randomColor);
            case 6:
                return new NormalZ(randomColor);
            default:
                return new NormalZ(randomColor);
        }
    }

    /**
     * nastaví náhodnou barvu
     * @param blocksImages
     * @return
     */
    public static Image randomColor(Map<BlockEnum, Image> blocksImages) {
        Random random = new Random();
        int randomIndex = random.nextInt(NUMBER_OF_COLORS);

        return blocksImages.get(BlockEnum.values()[randomIndex]);
    }



}
