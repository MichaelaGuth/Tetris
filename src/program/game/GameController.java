package program.game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import program.*;
import program.menu.Controller;
import program.pictures.ImageLoader;
import program.score.Score;
import program.block_shapes.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static program.Constants.*;
import static program.game.GameUtils.*;

/**
 * Created by IntelliJ IDEA.
 * User: MichaelaGuth
 * Date: 22. 8. 2018
 * Time: 22:27
 */
public class GameController implements EventHandler<KeyEvent> {

    public Canvas gameBoardCanvas;
    private Image playBackground;

    public Canvas block;
    private Image nextBlockBackground;

    public Label scoreLabel;

    public AnchorPane anchorPane;

    public ImageView backButton;
    private Image back;

    public ImageView retryButton;
    private Image retry;

    public ImageView frame;

    public ImageView howToPlay;

    private Map<BlockEnum, Image> blockImages;

    private Shape currentShape;
    private Shape nextShape;
    private Block[][] gameBoard;

    private Timeline timeline;

    private int score;
    private int scoreLevel;

    /**
     * Initial procedure.
     * Sets graphic and initial values.
     */
    @FXML
    public void initialize() {

        // Setting Background on Anchor Pane
        Image image = ImageLoader.LoadImage("TetrisBackground.png");
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        anchorPane.setBackground(background);

        // Setting initial value to score
        score = 0;
        scoreLabel.setText(score+"");

        // Setting Back Button
        back = ImageLoader.LoadImage("ExitButton.png");
        backButton.setImage(back);

        // Setting Retry Button
        retry = ImageLoader.LoadImage("RetryButton.png");
        retryButton.setImage(retry);

        // Setting game board
        playBackground = ImageLoader.LoadImage("Pole.png");
        GraphicsContext gc = gameBoardCanvas.getGraphicsContext2D();
        gc.drawImage(playBackground,0,0);
        frame.setImage(ImageLoader.LoadImage("Okrajpole.png"));

        // Setting window for next block
        nextBlockBackground = ImageLoader.LoadImage("NasledujiciKosticka.png");
        GraphicsContext gc2 = block.getGraphicsContext2D();
        gc2.drawImage(nextBlockBackground,0,0);                     //draw hraciho pozadi

        // Loading pictures into hash map (faster)
        blockImages = new HashMap<>(BlockEnum.values().length);
        blockImages.put(BlockEnum.SQUARE, ImageLoader.LoadImage("CtverecKosticka.png", BLOCK_SIZE, BLOCK_SIZE));
        blockImages.put(BlockEnum.TUBE, ImageLoader.LoadImage("TrubkaKosticka.png", BLOCK_SIZE, BLOCK_SIZE));
        blockImages.put(BlockEnum.BLOCK_T, ImageLoader.LoadImage("ShapeT.png", BLOCK_SIZE, BLOCK_SIZE));
        blockImages.put(BlockEnum.BLOCK_L, ImageLoader.LoadImage("LkoKosticka.png", BLOCK_SIZE, BLOCK_SIZE));
        blockImages.put(BlockEnum.BLOCK_Z, ImageLoader.LoadImage("ZkoKosticka.png", BLOCK_SIZE, BLOCK_SIZE));

        // Setting window for help details
        if (Controller.numberOfPlayers == 1) {

            Image tmp = ImageLoader.LoadImage("HowToPlaySingleplayer.png");
            howToPlay.setImage(tmp);

        } else {

            Image tmp = ImageLoader.LoadImage("HowToPlayMultiplayer.png");
            howToPlay.setImage(tmp);

        }

        // Starting game.
        gameInit();
    }

    /**
     * Initial procedure.
     * Sets parameters for a new game. Starts a new game.
     */
    public void gameInit() {

        // Creating shapes.
        currentShape = generateRandomShape(blockImages);
        nextShape = generateRandomShape(blockImages);

        // Setting game board.
        gameBoard = new Block[GAME_NUMBER_OF_LINES][GAME_NUMBER_OF_COLUMNS];

        // Setting timer and starting game.
        timeline = new Timeline(new KeyFrame(Duration.millis(INITIAL_FALLING_SPEED),
                ae -> gameLoop()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // Setting score level.
        scoreLevel = 1;
    }

    /**
     * Game loop.
     * Contains operations that happen regularly with timer while the game is running.
     */
    public void gameLoop() {

        // Checking if the game is over.
        if (checkGameOver(gameBoard)) {
            gameOver();
        }

        // Moving current block down.
        moveCurrentBlock(Direction.DOWN);

        // Render the next shape in the next shape window.
        draw(block, nextShape.getShape(), nextBlockBackground);

        // Deleting full lines.
        deleteFullLines();

        // Updating score.
        scoreLabel.setText(score+"");

        // Setting level
        scoreLevel = timerUp(levelUp(score));                           // TODO

    }

    /** TODO: handle exception
     * Leaves the game back to menu.
     * @throws Exception
     */
    public void backButtonAction() throws Exception {
        Parent root = FXMLLoader.load(Controller.class.getResource("menu.fxml"));
        Main.stage.setScene(new Scene(root, 600, 800));
        Main.stage.show();

        timeline.stop();
    }

    /**
     * Changes image when the mouse moves on the back button.
     */
    public void onMouseEnterBackButton() {
        Image SinglePlayerClick = ImageLoader.LoadImage("ExitClickButton.png");
        backButton.setImage(SinglePlayerClick);
    }

    /**
     * Changes image when the mouse moves off the back button.
     */
    public void onMouseLeaveBackButton() {
        backButton.setImage(back);
    }

    /**
     * Restarts the game.
     */
    public void retryButtonAction() {
        timeline.stop();
        gameInit();
    }

    /**
     * Changes image when the mouse moves on the retry button.
     */
    public void onMouseEnterRetryButton() {
        Image retryClick = ImageLoader.LoadImage("RetryClickButton.png");
        retryButton.setImage(retryClick);
    }

    /**
     * Changes image when the mouse moves off the retry button.
     */
    public void onMouseLeaveRetryButton() {
        retryButton.setImage(retry);
    }

    /**
     * Acts based on keyboard events.
     * @param event The event.
     */
    @Override
    public void handle(KeyEvent event) {

        switch (event.getCode()) {

            case UP:
                // Rotates the current block.
                rotateShape(currentShape);
                break;

            case DOWN:
                // Moves the current block down and adds score.
                moveCurrentBlock(Direction.DOWN);
                score = score + scoreLevel;
                break;

            case LEFT:
                // Moves the current block left.
                moveCurrentBlock(Direction.LEFT);
                break;

            case RIGHT:
                // Moves the current block right.
                moveCurrentBlock(Direction.RIGHT);
                break;

            case SPACE:
                // Moves the current block down to the end and adds score.
                while (moveCurrentBlock(Direction.DOWN)) {
                    score = score + 2* scoreLevel;
                }
                break;

            default:
                // nop
        }

        // Checking if the multi player mode is on.
        if (Controller.numberOfPlayers == 2) {

            // Generating random color.
            Image randomColor = randomColor(blockImages);

            switch (event.getCode()) {

                case DIGIT1:
                    // Setting the next block to shape square.
                    nextShape = new Square(randomColor);
                    draw(block, nextShape.getShape(), nextBlockBackground);
                    break;

                case DIGIT2:
                    // Setting the next block to shape mirror L.
                    nextShape = new MirrorL(randomColor);
                    draw(block, nextShape.getShape(), nextBlockBackground);
                    break;

                case DIGIT3:
                    // Setting the next block to shape normal L.
                    nextShape = new NormalL(randomColor);
                    draw(block, nextShape.getShape(), nextBlockBackground);
                    break;

                case DIGIT4:
                    // Setting the next block to shape T.
                    nextShape = new ShapeT(randomColor);
                    draw(block, nextShape.getShape(), nextBlockBackground);
                    break;

                case DIGIT5:
                    // Setting the next block to shape normal Z.
                    nextShape = new NormalZ(randomColor);
                    draw(block, nextShape.getShape(), nextBlockBackground);
                    break;

                case DIGIT6:
                    // Setting the next block to shape mirror Z.
                    nextShape = new MirrorZ(randomColor);
                    draw(block, nextShape.getShape(), nextBlockBackground);
                    break;

                case DIGIT7:
                    // Setting the next block to shape tube.
                    nextShape = new Tube(randomColor);
                    draw(block, nextShape.getShape(), nextBlockBackground);
                    break;
            }
        }
    }

    /**
     * Moves the current block in given direction.
     * @param direction The given direction.
     * @return  TRUE - if moving was successful
     *          else FALSE
     */
    public boolean moveCurrentBlock(Direction direction) {

        // Testing the new coordinates.
        int x = currentShape.getX() + direction.getX();
        int y = currentShape.getY() + direction.getY();
        Block[][] copyArray = GameUtils.copy(gameBoard);
        blockInsertStatus status = GameUtils.insertBlock(currentShape, gameBoard, copyArray, direction);

        switch (status) {
            
            case OK:
                // Moves the block.
                draw(gameBoardCanvas, copyArray, playBackground, GAME_NUMBER_OF_VISIBLE_LINES);
                currentShape.setX(x);
                currentShape.setY(y);
                return true;
                
            case COLLISION_WITH_OTHER_BLOCK_FROM_SIDE:
                // .... :-)
                
            case COLLISION_WITH_WALL:
                // Nothing happens.
                copyArray = GameUtils.copy(gameBoard);
                GameUtils.insertBlock(currentShape, gameBoard, copyArray, Direction.NONE);
                draw(gameBoardCanvas, copyArray, playBackground, GAME_NUMBER_OF_VISIBLE_LINES);
                return true;
                
            case COLLISION_WITH_END:
                // Saves the current block to game board and generates new block.
                copyArray = GameUtils.copy(gameBoard);
                GameUtils.insertBlock(currentShape, gameBoard, copyArray, Direction.NONE);
                gameBoard = copyArray;
                currentShape = nextShape;
                nextShape = generateRandomShape(blockImages);
                return false;
        }

        return false;
    }

    /**
     * Rotates the current shape with the help of {@link Constants#ROTATION_MATRIX}.
     * @param currentShape The current shape.
     */
    public void rotateShape(Shape currentShape) {

        // Creating the new matrix with coordinates of rotated shape.
        int[][] rotatedShapeInNumbers = matrixMultiplication(ROTATION_MATRIX, currentShape.getBody());

        // Checking if the new matrix is not in negative numbers
        int min = Integer.MAX_VALUE;

        for (int i = 0; i < rotatedShapeInNumbers[0].length; i++) {
            if (rotatedShapeInNumbers[0][i] < min) {
                min = rotatedShapeInNumbers[0][i];
            }
        }

        min = Math.abs(min);

        for (int i = 0; i < rotatedShapeInNumbers[0].length; i++) {
            rotatedShapeInNumbers[0][i] = rotatedShapeInNumbers[0][i] + min;
        }

        // Creating the new rotated shape.
        Block[][] rotatedShape = currentShape.createShape(rotatedShapeInNumbers);
        this.currentShape.setShape(rotatedShape);

        // Render the new shape.
        moveCurrentBlock(Direction.NONE);
    }

    /**
     * Renders the game board and background to canvas.
     * @param canvas The canvas.
     * @param gameBoard The game board.
     * @param background The background.
     */
    public void draw(Canvas canvas, Block[][] gameBoard, Image background) {
        draw(canvas, gameBoard, background, gameBoard.length);
    }

    /**
     * Renders the game board and background to canvas.
     * @param canvas The canvas.
     * @param gameBoard The game board.
     * @param background The background.
     * @param numberOfVisibleLines Number of visible lines.
     */
    public void draw(Canvas canvas, Block[][] gameBoard, Image background, int numberOfVisibleLines) {

        // Setting canvas for drawing.
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Clearing canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Drawing background.
        gc.drawImage(background,0,0);

        // Drawing game board.
        int offset = gameBoard.length - numberOfVisibleLines;
        for (int row = 0; row < gameBoard.length - offset; row++) {
            for (int col = 0; col < gameBoard[0].length; col++) {

                if (gameBoard[row + offset][col] != null) {
                    gc.drawImage(
                            gameBoard[row + offset][col].getBlock(),
                            col * BLOCK_SIZE,
                            row * BLOCK_SIZE
                    );
                }
            }
        }
    }

    /**
     * Deletes full lines from game board and adds score.
     */
    public void deleteFullLines() {

        int scoreCounter = 0;

        for (int row = 0; row < gameBoard.length; row++) {

            boolean check = true;

            for (int col = 0; col < gameBoard[0].length; col++) {

                if (gameBoard[row][col] == null) {
                    check = false;
                    break;
                }

            }

            if (check) {

                deleteLine(row, gameBoard);
                gameBoard = moveTheRestBlocksDown(row, gameBoard);
                draw(gameBoardCanvas, gameBoard, playBackground, GAME_NUMBER_OF_VISIBLE_LINES);

                scoreCounter++;
            }

        }

        switch (scoreCounter) {
            case 1:
                score = score + SCORE_DELETED_LINE * scoreLevel;
                break;
            case 2:
                score = score + SCORE_DELETED_LINE * scoreLevel * 3;
                break;
            case 3:
                score = score + SCORE_DELETED_LINE * scoreLevel * 5;
                break;
            default:
                //nop
        }

    }

    /**
     * Speeds up the game depending on game level.
     * @param lvl The game level.
     * @return Score level. // TODO weird function
     */
    public int timerUp(int lvl) {

        int scoreLevel = 1;

        switch (lvl) {

            case 1:
                timeline.rateProperty().setValue(TIMER_LEVEL_1);
                scoreLevel = 1;
                break;

            case 2:
                timeline.rateProperty().setValue(TIMER_LEVEL_2);
                scoreLevel = 10;
                break;

            case 3:
                timeline.rateProperty().setValue(TIMER_LEVEL_3);
                scoreLevel = 50;
                break;

            case 4:
                timeline.rateProperty().setValue(TIMER_LEVEL_4);
                scoreLevel = 100;
                break;

            case 5:
                timeline.rateProperty().setValue(TIMER_LEVEL_5);
                scoreLevel = 1000;
                break;

            default:
                //nop
        }

        return scoreLevel;
    }

    /**
     * Finds the level based on the given score.
     * @param score The given score.
     * @return The game level.
     */
    public int levelUp(int score) {
        int lvl = 1;

        if (score >= SCORE_LEVEL_5) {
            lvl = 5;
        } else if (score >= SCORE_LEVEL_4) {
            lvl = 4;
        } else if (score >= SCORE_LEVEL_3) {
            lvl = 3;
        } else if (score >= SCORE_LEVEL_2) {
            lvl = 2;
        }

        return lvl;
    }

    /**
     * Game Over.
     * Stops the game.
     */
    public void gameOver() {

        // Stopping the game loop.
        timeline.stop();

        // Creating dialog.
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Game Over");
        dialog.setHeaderText("GAME OVER. Do you want to save your score?");
        dialog.setContentText("Please enter your name loser:");

        // Saving score.
        Platform.runLater(() -> {
            String name;
            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                name = result.get().length() < 3 ? "Randomák" : result.get();
                Score tmp = new Score("HighScore.txt");
                tmp.saveHighScore(score,name);
            }
        });
    }


}
