package program.menu;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import program.Main;
import program.game.GameController;
import program.pictures.ImageLoader;
import program.score.ScoreController;

public class Controller {
    public AnchorPane anchorPane;
    public ImageView singlePlayerButton;
    public ImageView multiPlayerButton;
    public ImageView highScoresButton;
    public ImageView exitButton;
    public Image singlePlayerImage;
    public Image multiPlayerImage;
    public Image highScoreImage;
    public Image exit;
    public static int numberOfPlayers;

    /**
     * TODO: make constants with file names
     * TODO: handle exception
     * načtení obrázků
     */
    @FXML
    public void initialize() throws Exception {

        Image image = ImageLoader.LoadImage("MenuBackground.png");
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        anchorPane.setBackground(background);

        singlePlayerImage = ImageLoader.LoadImage("SinglePlayerButton.png");
        singlePlayerButton.setImage(singlePlayerImage);

        multiPlayerImage = ImageLoader.LoadImage("MultiplayerButton.png");
        multiPlayerButton.setImage(multiPlayerImage);

        highScoreImage = ImageLoader.LoadImage("HighScoreButton.png");
        highScoresButton.setImage(highScoreImage);

        exit = ImageLoader.LoadImage("ExitButton.png");
        exitButton.setImage(exit);
    }

    /**
     * TODO
     * ukončí program
     */
    public void exitButtonAction() {
        Platform.exit();
    }

    /**
     * TODO
     * při najetí kurzorem myši na tlačítko - změna obrázku
     */
    public void onMouseEnterExitButton() {
        exitButton.setImage(ImageLoader.LoadImage("ExitClickButton.png"));
    }

    public void onMouseLeaveExitButton() {
        exitButton.setImage(exit);
    }

    /**
     * TODO: handle exception
     * přepne obrazovku na harcí plán, nastaví počet hráčů na 1
     * @throws Exception
     */
    public void singlePlayerButtonAction() throws Exception {
        numberOfPlayers = 1;

        FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("game.fxml"));
        Parent root = fxmlLoader.load();

        Main.stage.setScene(new Scene(root, 600, 800));                         //vytvoření scény a nastavení zobrazení
        Main.stage.show();                                                                    //zobrazí připravenou scénu

        Main.stage.getScene().setOnKeyPressed(fxmlLoader.<GameController>getController());      //
    }

    /**
     * TODO
     * při najetí kurzorem myši na tlačítko - změna obrázku
     */
    public void onMouseEnterSinglePlayerButton() {
        Image image = ImageLoader.LoadImage("SinglePlayerClickButton.png");
        singlePlayerButton.setImage(image);
    }

    /**
     * TODO
     */
    public void onMouseLeaveSinglePlayerButton() {
        singlePlayerButton.setImage(singlePlayerImage);
    }

    /**
     * TODO: handle exception
     * přepne obrazovku na harcí plán, nastaví počet hráčů na 1
     * @throws Exception
     */
    public void multiPlayerButtonAction() throws Exception {
        numberOfPlayers = 2;

        FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("game.fxml"));
        Parent root = fxmlLoader.load();

        Main.stage.setScene(new Scene(root, 600, 800));                         //vytvoření scény a nastavení zobrazení
        Main.stage.show();                                                                    //zobrazí připravenou scénu

        Main.stage.getScene().setOnKeyPressed(fxmlLoader.<GameController>getController());
    }

    /**
     * při najetí kurzorem myši na tlačítko - změna obrázku
     */
    public void onMouseEnterMultiPlayerButton() {
        Image image = ImageLoader.LoadImage("MultiplayerClickButton.png");
        multiPlayerButton.setImage(image);
    }

    public void onMouseLeaveMultiPlayerButton() {
        multiPlayerButton.setImage(multiPlayerImage);
    }

    /**
     * TODO: handle exception
     * přepne obrazovku na Score Board
     * @throws Exception
     */
    public void highScoresButtonAction() throws Exception {
        Parent root = FXMLLoader.load(ScoreController.class.getResource("score1.fxml"));        //načtení popisu scény
        Main.stage.setScene(new Scene(root, 600, 800));                                 //vytvoření scény a nastavení zobrazení
        Main.stage.show();
    }

    /**
     * TODO
     * při najetí kurzorem myši na tlačítko - změna obrázku
     */
    public void onMouseEnterHighScoreButton() {
        highScoresButton.setImage(ImageLoader.LoadImage("HighScoreClickButton.png"));
    }

    /**
     * TODO
     */
    public void highScoreReleaseButton() {
        highScoresButton.setImage(highScoreImage);
    }
}