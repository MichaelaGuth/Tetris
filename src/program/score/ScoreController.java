package program.score;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import program.Main;
import program.menu.Controller;
import program.pictures.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 22. 8. 2018
 * Time: 22:30
 */
public class ScoreController {
    public AnchorPane Pain;
    private Image Back;
    public ImageView BackButton;
    public TableView Table;
    public TableColumn Players;
    public TableColumn Scores;
    public TableColumn Ranks;

    @FXML
    public void initialize() {
        Image image = ImageLoader.LoadImage("TetrisBackground.png");
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        Pain.setBackground(background);

        Back = ImageLoader.LoadImage("BackButton.png");
        BackButton.setImage(Back);

        ArrayList<String> scores;
        Score score = new Score("HighScore.txt");
        scores = score.loadScore();
        List list = new ArrayList();

        Ranks.setCellValueFactory(new PropertyValueFactory("Poradi"));
        Players.setCellValueFactory(new PropertyValueFactory("JmenoHrace"));
        Scores.setCellValueFactory(new PropertyValueFactory("HighScore"));

        for (String line : scores) {
            String[] tmp = score.split(line);
            list.add(new HighScoreItem(tmp[0], Integer.parseInt(tmp[1])));
        }

        ObservableList data = FXCollections.observableList(list);
        SortedList<HighScoreItem> sortedList = new SortedList<HighScoreItem>(data,
                (HighScoreItem item1, HighScoreItem item2) ->
                {
                   return item2.getHighScore()-item1.getHighScore();
                });
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setRank((i+1)+".");
        }
        Table.setItems(sortedList);
        //Table.getSortOrder().add(Scores);
    }

    /**
     * pokud stiskneme tlačítko zpět, vrátí nás zpět do menu a vypne timer
     * @throws Exception
     */
    public void backButtonAction() throws Exception {
        Parent root = FXMLLoader.load(Controller.class.getResource("menu.fxml"));    //načtení popisu scény
        Main.stage.setScene(new Scene(root, 600, 800));                 //vytvoření scény a nastavení zobrazení
        Main.stage.show();
    }

    /**
     * při najetí kurzorem myši na tlačítko - změna obrázku (rozsvícení tlačítka)
     */
    public void onMouseEnterBackButton() {
        BackButton.setImage(ImageLoader.LoadImage("onMouseEnterBackButton.png"));
    }

    /**
     * změna obrázku
     */
    public void onMouseLeaveBackButton() {
        BackButton.setImage(Back);
    }
}
