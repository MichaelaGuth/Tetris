package sample.game;

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
import sample.*;
import sample.menu.Controller;
import sample.obrazky.ImageLoader;
import sample.score.Score;
import sample.tvarykosticek.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static sample.Constants.*;
import static sample.game.GameUtils.*;

/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 22. 8. 2018
 * Time: 22:27
 */
public class GameController implements EventHandler<KeyEvent> {

    public Canvas gameBoard;
    private Image playBackground;

    public Canvas kosticka;
    private Image nasledujiciKostickaBackground;

    public Label scoreLabel;

    public AnchorPane container;

    public ImageView backButton;
    private Image back;

    public ImageView retryButton;
    private Image retry;

    public ImageView ramecek;

    public ImageView howToPlay;

    private Map<KostickaEnum, Image> kostickyImages;

    private Tvar aktualKosticka;
    private Tvar nasledujuKosticka;
    private Kosticka[][] hraciPole;

    private Timeline timeline;

    private int score;
    private int scorelvl;

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
        GraphicsContext gc = gameBoard.getGraphicsContext2D();
        gc.drawImage(playBackground,0,0);                   //vykresleni hraciho pozadi

        ramecek.setImage(ImageLoader.LoadImage("Okrajpole.png"));

        nasledujiciKostickaBackground = ImageLoader.LoadImage("NasledujiciKosticka.png");
        GraphicsContext gc2 = kosticka.getGraphicsContext2D();
        gc2.drawImage(nasledujiciKostickaBackground,0,0);                                                               //vykresleni hraciho pozadi

        //obrazky si predem nactu do mapy, aby to bylo rychlejší
        kostickyImages = new HashMap<>(KostickaEnum.values().length);
        kostickyImages.put(KostickaEnum.CTVEREC, ImageLoader.LoadImage("CtverecKosticka.png", KOSTICKA_SIZE, KOSTICKA_SIZE));
        kostickyImages.put(KostickaEnum.TRUBKA, ImageLoader.LoadImage("TrubkaKosticka.png", KOSTICKA_SIZE, KOSTICKA_SIZE));
        kostickyImages.put(KostickaEnum.TKO, ImageLoader.LoadImage("TkoKosticka.png", KOSTICKA_SIZE, KOSTICKA_SIZE));
        kostickyImages.put(KostickaEnum.LKO, ImageLoader.LoadImage("LkoKosticka.png", KOSTICKA_SIZE, KOSTICKA_SIZE));
        kostickyImages.put(KostickaEnum.ZKO, ImageLoader.LoadImage("ZkoKosticka.png", KOSTICKA_SIZE, KOSTICKA_SIZE));

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
        aktualKosticka = nahodnaKosticka(kostickyImages);
        nasledujuKosticka = nahodnaKosticka(kostickyImages);
        hraciPole = new Kosticka[HRA_POCET_RADKU][HRA_POCET_SLOUPCU];

        timeline = new Timeline(new KeyFrame(Duration.millis(POCATECNI_RYCHLOST),          //vytvoreni TIMERU
                ae -> gameLoop()));                                                        //ae = Action Event
        timeline.setCycleCount(Animation.INDEFINITE);                                      //nastavení časovače tak, aby pokračoval, dokud jej něco nevypne
        timeline.play();
        scorelvl = 1; //začíná se na levelu 1
    }

    /**
     * herní operace, který se mají provést v každém tiku timeru
     */
    public void gameLoop() {
        if (kontrolaGameOver(hraciPole)) {           //pokud je splněná podmínka GameOver, zavolá funkci GameOver
            GameOver();
        }

        posun(Smer.DOLU);   //při každém "tiku" timeru posune kosticku o 1 dolu

        vykresleni(kosticka, nasledujuKosticka.getTvar(), nasledujiciKostickaBackground);   //vykreslí nasledujici kosticku do nahledu

        vymazZaplneneRadky();   //smaže plné řádky
        scoreLabel.setText(score+"");   //vypíše skóre

        scorelvl = timerUp(levelUp(score));     //nastaví hru podle skóre na daný level

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
                rotace(aktualKosticka); //otočí aktuální kostičku o 90° doleva
                break;
            case DOWN:
                posun(Smer.DOLU);   //posune aktuální kostičku o 1 políčko dolů
                score = score + scorelvl;   //přičte určené skóre
                break;
            case LEFT:
                posun(Smer.DOLEVA);
                break;
            case RIGHT:
                posun(Smer.DOPRAVA);
                break;
            case SPACE:
                while (posun(Smer.DOLU)) {      //vyvolává posun dokud to jde
                    score = score + 2*scorelvl;     //přičte určené skóre
                }
                break;
            default:
                // nop
        }
        if (Controller.pocethracu == 2) {   //nastavení kláves pro multiplayer
            Image nahodnaBarva = nahodnaBarva(kostickyImages);
            switch (event.getCode()) {
                case DIGIT1:
                    //nastaví následující kostičku na Čtverec
                    nasledujuKosticka = new Ctverec(nahodnaBarva);
                    vykresleni(kosticka, nasledujuKosticka.getTvar(), nasledujiciKostickaBackground);
                    break;
                case DIGIT2:
                    //nastaví následující kostičku na LkoMirror
                    nasledujuKosticka = new LkoMirror(nahodnaBarva);
                    vykresleni(kosticka, nasledujuKosticka.getTvar(), nasledujiciKostickaBackground);
                    break;
                case DIGIT3:
                    //nastaví následující kostičku na LkoNormal
                    nasledujuKosticka = new LkoNormal(nahodnaBarva);
                    vykresleni(kosticka, nasledujuKosticka.getTvar(), nasledujiciKostickaBackground);
                    break;
                case DIGIT4:
                    //nastaví následující kostičku na Téčko
                    nasledujuKosticka = new TkoKosticka(nahodnaBarva);
                    vykresleni(kosticka, nasledujuKosticka.getTvar(), nasledujiciKostickaBackground);
                    break;
                case DIGIT5:
                    //nastaví následující kostičku na ZkoNormal
                    nasledujuKosticka = new ZkoNormal(nahodnaBarva);
                    vykresleni(kosticka, nasledujuKosticka.getTvar(), nasledujiciKostickaBackground);
                    break;
                case DIGIT6:
                    //nastaví následující kostičku na ZkoMirror
                    nasledujuKosticka = new ZkoMirror(nahodnaBarva);
                    vykresleni(kosticka, nasledujuKosticka.getTvar(), nasledujiciKostickaBackground);
                    break;
                case DIGIT7:
                    //nastaví následující kostičku na Trubku
                    nasledujuKosticka = new Trubka(nahodnaBarva);
                    vykresleni(kosticka, nasledujuKosticka.getTvar(), nasledujiciKostickaBackground);
                    break;
            }
        }
    }

    /**
     * posune kostku danym smerem
     * @param smer smer, kterym sem a kostka posunout
     * @return true pokud se posunuti povedlo,jinak false
     */
    public boolean posun(Smer smer) {
        //nastavení posunutí souřadnic
        int x = aktualKosticka.getX() + smer.getX();
        int y = aktualKosticka.getY() + smer.getY();

        // Vytvoreni kopie hraciho pole s vlozenou kostickou s posunem dle smeru
        Kosticka[][] copyPole = GameUtils.copy(hraciPole);
        VlozeniKostkyStatus status = GameUtils.vlozeniKosticky(aktualKosticka, hraciPole, copyPole, smer);

        switch (status) {
            case OK:
                //posune kostičku a uloží ji do hracího pole
                vykresleni(gameBoard, copyPole, playBackground, HRA_POCET_VIDITELNYCH_RADKU);
                aktualKosticka.setX(x);
                aktualKosticka.setY(y);
                return true;
            case KOLIZE_S_KOSTKOU_ZE_STRANY:
                //jdu na další řádek :-)
            case KOLIZE_SE_STENOU:
                //vykreslí pole stejně, jako kdyby se nic nestalo
                copyPole = GameUtils.copy(hraciPole);
                GameUtils.vlozeniKosticky(aktualKosticka, hraciPole, copyPole, Smer.NIC);
                vykresleni(gameBoard, copyPole, playBackground, HRA_POCET_VIDITELNYCH_RADKU);
                return true;
            case KOLIZE_S_KONCEM:
                //uloží spadlou kostku do hracího pole a nastaví novou následující kostičku
                copyPole = GameUtils.copy(hraciPole);
                GameUtils.vlozeniKosticky(aktualKosticka, hraciPole, copyPole, Smer.NIC);
                hraciPole = copyPole;
                aktualKosticka = nasledujuKosticka;
                nasledujuKosticka = nahodnaKosticka(kostickyImages);
                return false;
        }

        return false;
    }

    /**
     * Otoci kostku o uhel dany matici {@link Constants#MATICE_OTOCENI}.
     * @param aktual tvar k otoceni
     */
    public void rotace(Tvar aktual) {

        // Otoceni
        int[][] otoceni = nasobeniMatic(MATICE_OTOCENI, aktual.getBody());

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

        Kosticka[][] tmp = aktual.createTvar(otoceni);


        aktualKosticka.setTvar(tmp);

        posun(Smer.NIC);
    }

    /**
     * vykresli pole a image do Canvasu
     * @param canvas
     * @param hraciPole
     * @param pozadi
     */
    public void vykresleni(Canvas canvas, Kosticka[][] hraciPole, Image pozadi) {
        vykresleni(canvas, hraciPole, pozadi, hraciPole.length);
    }

    /**
     * vykresli pole a image do Canvasu
     * @param canvas
     * @param hraciPole
     * @param pozadi
     */
    public void vykresleni(Canvas canvas, Kosticka[][] hraciPole, Image pozadi, int pocetViditelnychRadku) {

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Smazat vykreslene obrazky z predchoziho tiku
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(pozadi,0,0);

        // offset od kdy zacit vykreslovat (prvni radky jsou neviditelne)
        int offset = hraciPole.length - pocetViditelnychRadku;

        for (int radek = 0; radek < hraciPole.length - offset; radek++) {
            for (int sloupec = 0; sloupec < hraciPole[0].length; sloupec++) {

                if (hraciPole[radek + offset][sloupec] != null) {
                    gc.drawImage(
                            hraciPole[radek + offset][sloupec].getKosticka(),
                            sloupec * KOSTICKA_SIZE,
                            radek * KOSTICKA_SIZE
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
        for (int radek = 0; radek < hraciPole.length; radek++) {
            boolean kontrola = true;
            for (int sloupec = 0; sloupec < hraciPole[0].length; sloupec++) {
                if (hraciPole[radek][sloupec] == null) {
                    kontrola = false;
                    break;
                }
            }
            if (kontrola) {
                umazRadek(radek, hraciPole);
                hraciPole = posunZbytekDolu(radek,hraciPole);
                vykresleni(gameBoard, hraciPole, playBackground,HRA_POCET_VIDITELNYCH_RADKU);

                scoreCounter++;
            }
        }

        switch (scoreCounter) {
            case 1:
                score = score + SCORE_UMAZANI_RADKU * scorelvl;
                break;
            case 2:
                score = score + SCORE_UMAZANI_RADKU * scorelvl * 3;
                break;
            case 3:
                score = score + SCORE_UMAZANI_RADKU * scorelvl * 5;
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
