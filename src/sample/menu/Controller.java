package sample.menu;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import sample.Main;
import sample.game.GameController;
import sample.obrazky.ImageLoader;
import sample.score.ScoreController;

public class Controller {
    public AnchorPane Pain;
    public ImageView singleplayerButton;
    public ImageView multiplayerButton;
    public ImageView highScoresButton;
    public ImageView exitButton;
    public Image singleplayer;
    public Image multiplayer;
    public Image highscores;
    public Image exit;
    public static int pocethracu;

    /**
     * načtení obrázků
     */
    @FXML
    public void initialize() throws Exception {
        Image image = ImageLoader.LoadImage("MenuBackground.png");
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        Pain.setBackground(background);

        singleplayer = ImageLoader.LoadImage("SinglePlayerButton.png");
        singleplayerButton.setImage(singleplayer);

        multiplayer = ImageLoader.LoadImage("MultiplayerButton.png");
        multiplayerButton.setImage(multiplayer);

        highscores = ImageLoader.LoadImage("HighScoreButton.png");
        highScoresButton.setImage(highscores);

        exit = ImageLoader.LoadImage("ExitButton.png");
        exitButton.setImage(exit);
    }

    /**
     * ukončí program
     */
    public void exitButtonAction() {
        Platform.exit();
    }

    /**
     * při najetí kurzorem myši na tlačítko - změna obrázku
     */
    public void exitClickButton() {
        Image Exitclick = ImageLoader.LoadImage("ExitClickButton.png");
        exitButton.setImage(Exitclick);
    }

    public void exitReleaseButton() {
        exitButton.setImage(exit);
    }

    /**
     * přepne obrazovku na harcí plán, nastaví počet hráčů na 1
     * @throws Exception
     */
    public void singleplayerButtonAction() throws Exception {
        pocethracu = 1;

        FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("game.fxml"));
        Parent root = fxmlLoader.load();

        Main.stage.setScene(new Scene(root, 600, 800));                         //vytvoření scény a nastavení zobrazení
        Main.stage.show();                                                                    //zobrazí připravenou scénu

        Main.stage.getScene().setOnKeyPressed(fxmlLoader.<GameController>getController());      //
    }

    /**
     * při najetí kurzorem myši na tlačítko - změna obrázku
     */
    public void singleplayerClickButton() {
        Image SinglePlayerclick = ImageLoader.LoadImage("SinglePlayerClickButton.png");
        singleplayerButton.setImage(SinglePlayerclick);
    }

    public void singleplayerReleaseButton() {
        singleplayerButton.setImage(singleplayer);
    }

    /**
     * přepne obrazovku na harcí plán, nastaví počet hráčů na 1
     * @throws Exception
     */
    public void multiplayerButtonAction() throws Exception {
        pocethracu = 2;

        FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("game.fxml"));
        Parent root = fxmlLoader.load();

        Main.stage.setScene(new Scene(root, 600, 800));                         //vytvoření scény a nastavení zobrazení
        Main.stage.show();                                                                    //zobrazí připravenou scénu

        Main.stage.getScene().setOnKeyPressed(fxmlLoader.<GameController>getController());
    }

    /**
     * při najetí kurzorem myši na tlačítko - změna obrázku
     */
    public void multiplayerClickButton() {
        Image Multiplayerclick = ImageLoader.LoadImage("MultiplayerClickButton.png");
        multiplayerButton.setImage(Multiplayerclick);
    }

    public void multiplayerReleaseButton() {
        multiplayerButton.setImage(multiplayer);
    }

    /**
     * přepne obrazovku na Score Board
     * @throws Exception
     */
    public void highScoresButtonAction() throws Exception {
        Parent root = FXMLLoader.load(ScoreController.class.getResource("score1.fxml"));    //načtení popisu scény
        Main.stage.setScene(new Scene(root, 600, 800));                 //vytvoření scény a nastavení zobrazení
        Main.stage.show();
    }

    /**
     * při najetí kurzorem myši na tlačítko - změna obrázku
     */
    public void highScoreClickButton() {
        Image Highscoreclick = ImageLoader.LoadImage("HighScoreClickButton.png");
        highScoresButton.setImage(Highscoreclick);
    }

    public void highScoreReleaseButton() {
        highScoresButton.setImage(highscores);
    }
}