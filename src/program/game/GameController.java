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
 * User: Kimiko
 * Date: 22. 8. 2018
 * Time: 22:27
 */
public class GameController implements EventHandler<KeyEvent> {

    public Canvas gameBoardCanvas;
    private Image playBackground;

    public Canvas block;
    private Image nextBlockBackground;

    public Label scoreLabel;

    public AnchorPane container;

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
     * TODO
     * načtení herního plánu, nastavení skóre na nula
     */
    @FXML
    public void initialize() {
        Image image = ImageLoader.LoadImage("TetrisBackground.png");
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        container.setBackground(background);

        score = 0;                                                      //nastavení skóre na nula
        scoreLabel.setText(score+"");

        back = ImageLoader.LoadImage("ExitButton.png");
        backButton.setImage(back);

        retry = ImageLoader.LoadImage("RetryButton.png");
        retryButton.setImage(retry);

        playBackground = ImageLoader.LoadImage("Pole.png");
        GraphicsContext gc = gameBoardCanvas.getGraphicsContext2D();
        gc.drawImage(playBackground,0,0);                           //render hraciho pozadi

        frame.setImage(ImageLoader.LoadImage("Okrajpole.png"));

        nextBlockBackground = ImageLoader.LoadImage("NasledujiciKosticka.png");
        GraphicsContext gc2 = block.getGraphicsContext2D();
        gc2.drawImage(nextBlockBackground,0,0);                     //render hraciho pozadi

        //pictures si predem nactu do mapy, aby to bylo rychlejší
        blockImages = new HashMap<>(BlockEnum.values().length);
        blockImages.put(BlockEnum.SQUARE, ImageLoader.LoadImage("CtverecKosticka.png", BLOCK_SIZE, BLOCK_SIZE));
        blockImages.put(BlockEnum.TUBE, ImageLoader.LoadImage("TrubkaKosticka.png", BLOCK_SIZE, BLOCK_SIZE));
        blockImages.put(BlockEnum.BLOCK_T, ImageLoader.LoadImage("ShapeT.png", BLOCK_SIZE, BLOCK_SIZE));
        blockImages.put(BlockEnum.BLOCK_L, ImageLoader.LoadImage("LkoKosticka.png", BLOCK_SIZE, BLOCK_SIZE));
        blockImages.put(BlockEnum.BLOCK_Z, ImageLoader.LoadImage("ZkoKosticka.png", BLOCK_SIZE, BLOCK_SIZE));

        //napoveda
        if (Controller.numberOfPlayers == 1) {
            Image Singleplayer = ImageLoader.LoadImage("HowToPlaySingleplayer.png");
            howToPlay.setImage(Singleplayer);
        } else {
            Image Multiplayer = ImageLoader.LoadImage("HowToPlayMultiplayer.png");
            howToPlay.setImage(Multiplayer);
        }

        GameInit(); //zahájí hru
    }

    /**
     * TODO
     * inicializace hry
     */
    public void GameInit() {

        currentShape = generateRandomShape(blockImages);
        nextShape = generateRandomShape(blockImages);

        gameBoard = new Block[GAME_NUMBER_OF_LINES][GAME_NUMBER_OF_COLUMNS];

        timeline = new Timeline(new KeyFrame(Duration.millis(INITIAL_FALLING_SPEED),        //vytvoreni TIMERU
                ae -> gameLoop()));                                                         //ae = Action Event
        timeline.setCycleCount(Animation.INDEFINITE);                                       //nastavení časovače tak, aby pokračoval, dokud jej něco nevypne
        timeline.play();

        scoreLevel = 1;                                                                     //začíná se na levelu 1
    }

    /**
     * TODO
     * herní operace, který se mají provést v každém tiku timeru
     */
    public void gameLoop() {

        if (checkGameOver(gameBoard)) {                                 //pokud je splněná podmínka gameOver, zavolá funkci gameOver
            gameOver();
        }

        moveCurrentBlock(Direction.DOWN);                               //při každém "tiku" timeru posune kosticku o 1 dolu

        render(block, nextShape.getShape(), nextBlockBackground);       //vykreslí nasledujici kosticku do nahledu

        deleteFullLines();                                              //smaže plné řádky
        scoreLabel.setText(score+"");                                   //vypíše skóre

        scoreLevel = timerUp(levelUp(score));                           //nastaví hru podle skóre na daný level

    }

    /** TODO: handle exception
     * pokud stiskneme tlačítko zpět, vrátí nás zpět do menu a vypne timer
     * @throws Exception
     */
    public void backButtonAction() throws Exception {
        Parent root = FXMLLoader.load(Controller.class.getResource("menu.fxml"));       //načtení popisu scény
        Main.stage.setScene(new Scene(root, 600, 800));                         //vytvoření scény a nastavení zobrazení
        Main.stage.show();

        timeline.stop();
    }

    /**
     * TODO
     * při najetí kurzorem myši na tlačítko - změna obrázku
     */
    public void onMouseEnterBackButton() {
        Image SinglePlayerClick = ImageLoader.LoadImage("ExitClickButton.png");
        backButton.setImage(SinglePlayerClick);
    }

    /**
     * TODO
     */
    public void onMouseLeaveBackButton() {
        backButton.setImage(back);
    }

    /**
     * TODO
     * restartuje hru
     */
    public void retryButtonAction() {
        timeline.stop();
        GameInit();
    }

    /**
     * TODO
     * při najetí kurzorem myši na tlačítko - změna obrázku
     */
    public void onMouseEnterRetryButton() {
        Image retryClick = ImageLoader.LoadImage("RetryClickButton.png");
        retryButton.setImage(retryClick);
    }

    /**
     * TODO
     */
    public void onMouseLeaveRetryButton() {
        retryButton.setImage(retry);
    }

    /**
     * TODO
     * obsluha stisku kláves
     * @param event
     */
    @Override
    public void handle(KeyEvent event) {

        switch (event.getCode()) {

            case UP:
                rotateShape(currentShape);                      //otočí aktuální kostičku o 90° doleva
                break;

            case DOWN:
                moveCurrentBlock(Direction.DOWN);               //posune aktuální kostičku o 1 políčko dolů
                score = score + scoreLevel;                     //přičte určené skóre
                break;

            case LEFT:
                moveCurrentBlock(Direction.LEFT);
                break;

            case RIGHT:
                moveCurrentBlock(Direction.RIGHT);
                break;

            case SPACE:
                while (moveCurrentBlock(Direction.DOWN)) {      //vyvolává moveCurrentBlock dokud to jde
                    score = score + 2* scoreLevel;              //přičte určené skóre
                }
                break;
            default:
                // nop
        }

        if (Controller.numberOfPlayers == 2) {   //nastavení kláves pro multiPlayerImage

            Image randomColor = randomColor(blockImages);

            switch (event.getCode()) {

                case DIGIT1:
                    //nastaví následující kostičku na Čtverec
                    nextShape = new Square(randomColor);
                    render(block, nextShape.getShape(), nextBlockBackground);
                    break;

                case DIGIT2:
                    //nastaví následující kostičku na MirrorL
                    nextShape = new MirrorL(randomColor);
                    render(block, nextShape.getShape(), nextBlockBackground);
                    break;

                case DIGIT3:
                    //nastaví následující kostičku na NormalL
                    nextShape = new NormalL(randomColor);
                    render(block, nextShape.getShape(), nextBlockBackground);
                    break;

                case DIGIT4:
                    //nastaví následující kostičku na Téčko
                    nextShape = new ShapeT(randomColor);
                    render(block, nextShape.getShape(), nextBlockBackground);
                    break;

                case DIGIT5:
                    //nastaví následující kostičku na NormalZ
                    nextShape = new NormalZ(randomColor);
                    render(block, nextShape.getShape(), nextBlockBackground);
                    break;

                case DIGIT6:
                    //nastaví následující kostičku na MirrorZ
                    nextShape = new MirrorZ(randomColor);
                    render(block, nextShape.getShape(), nextBlockBackground);
                    break;

                case DIGIT7:
                    //nastaví následující kostičku na Trubku
                    nextShape = new Tube(randomColor);
                    render(block, nextShape.getShape(), nextBlockBackground);
                    break;
            }
        }
    }

    /**
     * TODO
     * posune kostku danym smerem
     * @param direction direction, kterym sem a kostka posunout
     * @return true pokud se posunuti povedlo,jinak false
     */
    public boolean moveCurrentBlock(Direction direction) {
        //nastavení posunutí souřadnic
        int x = currentShape.getX() + direction.getX();
        int y = currentShape.getY() + direction.getY();

        // Vytvoreni kopie hraciho pole s vlozenou kostickou s posunem dle smeru
        Block[][] copyArray = GameUtils.copy(gameBoard);
        blockInsertStatus status = GameUtils.insertBlock(currentShape, gameBoard, copyArray, direction);

        switch (status) {
            
            case OK:
                //posune kostičku a uloží ji do hracího pole
                render(gameBoardCanvas, copyArray, playBackground, GAME_NUMBER_OF_VISIBLE_LINES);
                currentShape.setX(x);
                currentShape.setY(y);
                return true;
                
            case COLLISION_WITH_OTHER_BLOCK_FROM_SIDE:
                //jdu na další řádek :-)
                
            case COLLISION_WITH_WALL:
                //vykreslí pole stejně, jako kdyby se nic nestalo
                copyArray = GameUtils.copy(gameBoard);
                GameUtils.insertBlock(currentShape, gameBoard, copyArray, Direction.NONE);
                render(gameBoardCanvas, copyArray, playBackground, GAME_NUMBER_OF_VISIBLE_LINES);
                return true;
                
            case COLLISION_WITH_END:
                //uloží spadlou kostku do hracího pole a nastaví novou následující kostičku
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
     * TODO
     * Otoci kostku o uhel dany matici {@link Constants#ROTATION_MATRIX}.
     * @param currentShape shape k otoceni
     */
    public void rotateShape(Shape currentShape) {

        // Otoceni
        int[][] rotatedShapeInNumbers = matrixMultiplication(ROTATION_MATRIX, currentShape.getBody());

        /*
         * Pri otoceni muze dojit k presunu do jineho kvadrantu (- souradnice x nebo y), takze je potreba otocenou
         * kostku dat zpet.
         */
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

        Block[][] rotatedShape = currentShape.createShape(rotatedShapeInNumbers);

        this.currentShape.setShape(rotatedShape);

        moveCurrentBlock(Direction.NONE);
    }

    /**
     * TODO
     * vykresli pole a image do Canvasu
     * @param canvas
     * @param gameBoard
     * @param background
     */
    public void render(Canvas canvas, Block[][] gameBoard, Image background) {
        render(canvas, gameBoard, background, gameBoard.length);
    }

    /**
     * TODO
     * vykresli pole a image do Canvasu
     * @param canvas
     * @param gameBoard
     * @param background
     */
    public void render(Canvas canvas, Block[][] gameBoard, Image background, int numberOfVisibleLines) {

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Smazat vykreslene pictures z predchoziho tiku
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(background,0,0);

        // offset od kdy zacit vykreslovat (prvni radky jsou neviditelne)
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
     * TODO
     * vymaze plné řádky a přičte určené skóre
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
                render(gameBoardCanvas, gameBoard, playBackground, GAME_NUMBER_OF_VISIBLE_LINES);

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
     *  TODO
     * @param lvl
     * @return
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
     * TODO
     * @param score
     * @return
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
     * TODO
     */
    public void gameOver() {
        timeline.stop();

        // Priprava dialogu pro zadani jmena
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Game Over");
        dialog.setHeaderText("GAME OVER. Do you want to save your score?");
        dialog.setContentText("Please enter your name loser:");

        // Zobrazeni dialogu pro zadani jmena
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
