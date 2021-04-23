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
     * načtení herního plánu, nastavení skóre na nula
     */
    @FXML
    public void initialize() {
        Image image = ImageLoader.LoadImage("TetrisBackground.png");
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        container.setBackground(background);

        score = 0;                          //nastavení skóre na nula
        scoreLabel.setText(score+"");

        back = ImageLoader.LoadImage("ExitButton.png");
        backButton.setImage(back);

        retry = ImageLoader.LoadImage("RetryButton.png");
        retryButton.setImage(retry);

        playBackground = ImageLoader.LoadImage("Pole.png");
        GraphicsContext gc = gameBoardCanvas.getGraphicsContext2D();
        gc.drawImage(playBackground,0,0);                   //vykresleni hraciho pozadi

        frame.setImage(ImageLoader.LoadImage("Okrajpole.png"));

        nextBlockBackground = ImageLoader.LoadImage("NasledujiciKosticka.png");
        GraphicsContext gc2 = block.getGraphicsContext2D();
        gc2.drawImage(nextBlockBackground,0,0);                                                               //vykresleni hraciho pozadi

        //pictures si predem nactu do mapy, aby to bylo rychlejší
        blockImages = new HashMap<>(BlockEnum.values().length);
        blockImages.put(BlockEnum.SQUARE, ImageLoader.LoadImage("CtverecKosticka.png", BLOCK_SIZE, BLOCK_SIZE));
        blockImages.put(BlockEnum.TUBE, ImageLoader.LoadImage("TrubkaKosticka.png", BLOCK_SIZE, BLOCK_SIZE));
        blockImages.put(BlockEnum.BLOCK_T, ImageLoader.LoadImage("ShapeT.png", BLOCK_SIZE, BLOCK_SIZE));
        blockImages.put(BlockEnum.BLOCK_L, ImageLoader.LoadImage("LkoKosticka.png", BLOCK_SIZE, BLOCK_SIZE));
        blockImages.put(BlockEnum.BLOCK_Z, ImageLoader.LoadImage("ZkoKosticka.png", BLOCK_SIZE, BLOCK_SIZE));

        //napoveda
        if (Controller.pocethracu == 1) {
            Image Singleplayer = ImageLoader.LoadImage("HowToPlaySingleplayer.png");
            howToPlay.setImage(Singleplayer);
        } else {
            Image Multiplayer = ImageLoader.LoadImage("HowToPlayMultiplayer.png");
            howToPlay.setImage(Multiplayer);
        }

        GameInit(); //zahájí hru
    }

    /**
     * inicializace hry
     */
    public void GameInit() {
        currentShape = generateRandomBlock(blockImages);
        nextShape = generateRandomBlock(blockImages);
        gameBoard = new Block[GAME_NUMBER_OF_LINES][GAME_NUMBER_OF_COLUMNS];

        timeline = new Timeline(new KeyFrame(Duration.millis(INITIAL_FALLING_SPEED),          //vytvoreni TIMERU
                ae -> gameLoop()));                                                        //ae = Action Event
        timeline.setCycleCount(Animation.INDEFINITE);                                      //nastavení časovače tak, aby pokračoval, dokud jej něco nevypne
        timeline.play();
        scoreLevel = 1; //začíná se na levelu 1
    }

    /**
     * herní operace, který se mají provést v každém tiku timeru
     */
    public void gameLoop() {
        if (checkGameOver(gameBoard)) {           //pokud je splněná podmínka GameOver, zavolá funkci GameOver
            GameOver();
        }

        posun(Direction.DOWN);   //při každém "tiku" timeru posune kosticku o 1 dolu

        vykresleni(block, nextShape.getShape(), nextBlockBackground);   //vykreslí nasledujici kosticku do nahledu

        vymazZaplneneRadky();   //smaže plné řádky
        scoreLabel.setText(score+"");   //vypíše skóre

        scoreLevel = timerUp(levelUp(score));     //nastaví hru podle skóre na daný level

    }

    /**
     * pokud stiskneme tlačítko zpět, vrátí nás zpět do menu a vypne timer
     * @throws Exception
     */
    public void backButtonAction() throws Exception {
        Parent root = FXMLLoader.load(Controller.class.getResource("menu.fxml"));    //načtení popisu scény
        Main.stage.setScene(new Scene(root, 600, 800));                 //vytvoření scény a nastavení zobrazení
        Main.stage.show();

        timeline.stop();
    }

    /**
     * při najetí kurzorem myši na tlačítko - změna obrázku
     */
    public void backClickButton() {
        Image SinglePlayerclick = ImageLoader.LoadImage("ExitClickButton.png");
        backButton.setImage(SinglePlayerclick);
    }

    public void backReleaseButton() {
        backButton.setImage(back);
    }

    /**
     * restartuje hru
     */
    public void retryButtonAction() {
        timeline.stop();
        GameInit();
    }

    /**
     * při najetí kurzorem myši na tlačítko - změna obrázku
     */
    public void retryClickButton() {
        Image retryClick = ImageLoader.LoadImage("RetryClickButton.png");
        retryButton.setImage(retryClick);
    }

    public void retryReleaseButton() {
        retryButton.setImage(retry);
    }

    /**
     * obsluha stisku kláves
     * @param event
     */
    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                rotace(currentShape); //otočí aktuální kostičku o 90° doleva
                break;
            case DOWN:
                posun(Direction.DOWN);   //posune aktuální kostičku o 1 políčko dolů
                score = score + scoreLevel;   //přičte určené skóre
                break;
            case LEFT:
                posun(Direction.LEFT);
                break;
            case RIGHT:
                posun(Direction.RIGHT);
                break;
            case SPACE:
                while (posun(Direction.DOWN)) {      //vyvolává posun dokud to jde
                    score = score + 2* scoreLevel;     //přičte určené skóre
                }
                break;
            default:
                // nop
        }
        if (Controller.pocethracu == 2) {   //nastavení kláves pro multiplayer
            Image nahodnaBarva = randomColor(blockImages);
            switch (event.getCode()) {
                case DIGIT1:
                    //nastaví následující kostičku na Čtverec
                    nextShape = new Square(nahodnaBarva);
                    vykresleni(block, nextShape.getShape(), nextBlockBackground);
                    break;
                case DIGIT2:
                    //nastaví následující kostičku na MirrorL
                    nextShape = new MirrorL(nahodnaBarva);
                    vykresleni(block, nextShape.getShape(), nextBlockBackground);
                    break;
                case DIGIT3:
                    //nastaví následující kostičku na NormalL
                    nextShape = new NormalL(nahodnaBarva);
                    vykresleni(block, nextShape.getShape(), nextBlockBackground);
                    break;
                case DIGIT4:
                    //nastaví následující kostičku na Téčko
                    nextShape = new ShapeT(nahodnaBarva);
                    vykresleni(block, nextShape.getShape(), nextBlockBackground);
                    break;
                case DIGIT5:
                    //nastaví následující kostičku na NormalZ
                    nextShape = new NormalZ(nahodnaBarva);
                    vykresleni(block, nextShape.getShape(), nextBlockBackground);
                    break;
                case DIGIT6:
                    //nastaví následující kostičku na MirrorZ
                    nextShape = new MirrorZ(nahodnaBarva);
                    vykresleni(block, nextShape.getShape(), nextBlockBackground);
                    break;
                case DIGIT7:
                    //nastaví následující kostičku na Trubku
                    nextShape = new Tube(nahodnaBarva);
                    vykresleni(block, nextShape.getShape(), nextBlockBackground);
                    break;
            }
        }
    }

    /**
     * posune kostku danym smerem
     * @param direction direction, kterym sem a kostka posunout
     * @return true pokud se posunuti povedlo,jinak false
     */
    public boolean posun(Direction direction) {
        //nastavení posunutí souřadnic
        int x = currentShape.getX() + direction.getX();
        int y = currentShape.getY() + direction.getY();

        // Vytvoreni kopie hraciho pole s vlozenou kostickou s posunem dle smeru
        Block[][] copyPole = GameUtils.copy(gameBoard);
        VlozeniKostkyStatus status = GameUtils.vlozeniKosticky(currentShape, gameBoard, copyPole, direction);

        switch (status) {
            case OK:
                //posune kostičku a uloží ji do hracího pole
                vykresleni(gameBoardCanvas, copyPole, playBackground, GAME_NUMBER_OF_VISIBLE_LINES);
                currentShape.setX(x);
                currentShape.setY(y);
                return true;
            case KOLIZE_S_KOSTKOU_ZE_STRANY:
                //jdu na další řádek :-)
            case KOLIZE_SE_STENOU:
                //vykreslí pole stejně, jako kdyby se nic nestalo
                copyPole = GameUtils.copy(gameBoard);
                GameUtils.vlozeniKosticky(currentShape, gameBoard, copyPole, Direction.NONE);
                vykresleni(gameBoardCanvas, copyPole, playBackground, GAME_NUMBER_OF_VISIBLE_LINES);
                return true;
            case KOLIZE_S_KONCEM:
                //uloží spadlou kostku do hracího pole a nastaví novou následující kostičku
                copyPole = GameUtils.copy(gameBoard);
                GameUtils.vlozeniKosticky(currentShape, gameBoard, copyPole, Direction.NONE);
                gameBoard = copyPole;
                currentShape = nextShape;
                nextShape = generateRandomBlock(blockImages);
                return false;
        }

        return false;
    }

    /**
     * Otoci kostku o uhel dany matici {@link Constants#ROTATION_MATRIX}.
     * @param aktual shape k otoceni
     */
    public void rotace(Shape aktual) {

        // Otoceni
        int[][] otoceni = matrixMultiplication(ROTATION_MATRIX, aktual.getBody());

        /*
         * Pri otoceni muze dojit k presunu do jineho kvadrantu (- souradnice x nebo y), takze je potreba otocenou
         * kostku dat zpet.
         */
        int min = Integer.MAX_VALUE;

        for (int i = 0; i < otoceni[0].length; i++) {
            if (otoceni[0][i] < min) {
                min = otoceni[0][i];
            }
        }

        min = Math.abs(min);

        for (int i = 0; i < otoceni[0].length; i++) {
            otoceni[0][i] = otoceni[0][i] + min;
        }

        Block[][] tmp = aktual.createTvar(otoceni);


        currentShape.setShape(tmp);

        posun(Direction.NONE);
    }

    /**
     * vykresli pole a image do Canvasu
     * @param canvas
     * @param hraciPole
     * @param pozadi
     */
    public void vykresleni(Canvas canvas, Block[][] hraciPole, Image pozadi) {
        vykresleni(canvas, hraciPole, pozadi, hraciPole.length);
    }

    /**
     * vykresli pole a image do Canvasu
     * @param canvas
     * @param hraciPole
     * @param pozadi
     */
    public void vykresleni(Canvas canvas, Block[][] hraciPole, Image pozadi, int pocetViditelnychRadku) {

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Smazat vykreslene pictures z predchoziho tiku
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(pozadi,0,0);

        // offset od kdy zacit vykreslovat (prvni radky jsou neviditelne)
        int offset = hraciPole.length - pocetViditelnychRadku;

        for (int radek = 0; radek < hraciPole.length - offset; radek++) {
            for (int sloupec = 0; sloupec < hraciPole[0].length; sloupec++) {

                if (hraciPole[radek + offset][sloupec] != null) {
                    gc.drawImage(
                            hraciPole[radek + offset][sloupec].getBlock(),
                            sloupec * BLOCK_SIZE,
                            radek * BLOCK_SIZE
                    );
                }
            }
        }
    }

    /**
     * vymaze plné řádky a přičte určené skóre
     */
    public void vymazZaplneneRadky() {
        int scoreCounter = 0;
        for (int radek = 0; radek < gameBoard.length; radek++) {
            boolean kontrola = true;
            for (int sloupec = 0; sloupec < gameBoard[0].length; sloupec++) {
                if (gameBoard[radek][sloupec] == null) {
                    kontrola = false;
                    break;
                }
            }
            if (kontrola) {
                deleteLine(radek, gameBoard);
                gameBoard = moveTheRestBlocksDown(radek, gameBoard);
                vykresleni(gameBoardCanvas, gameBoard, playBackground, GAME_NUMBER_OF_VISIBLE_LINES);

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

    public int timerUp(int lvl) {
        int scorelevel = 1;
        switch (lvl) {
            case 1:
                timeline.rateProperty().setValue(TIMER_LEVEL_1);
                scorelevel = 1;
                break;
            case 2:
                timeline.rateProperty().setValue(TIMER_LEVEL_2);
                scorelevel = 10;
                break;
            case 3:
                timeline.rateProperty().setValue(TIMER_LEVEL_3);
                scorelevel = 50;
                break;
            case 4:
                timeline.rateProperty().setValue(TIMER_LEVEL_4);
                scorelevel = 100;
                break;
            case 5:
                timeline.rateProperty().setValue(TIMER_LEVEL_5);
                scorelevel = 1000;
                break;
            default:
                //nop
        }
        return scorelevel;
    }

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

    public void GameOver() {
        timeline.stop();

        // Priprava dialogu pro zadani jmena
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Game Over");
        dialog.setHeaderText("GAME OVER. Do you want to save your score");
        dialog.setContentText("Please enter your name loser:");

        // Zobrazeni dialogu pro zadani jmena
        Platform.runLater(() -> {
            String name;
            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                name = result.get().length() < 3 ? "Randomák" : result.get();
                Score tmp = new Score("HighScore.txt");
                tmp.SaveHighScore(score,name);
            }
        });
    }


}
