package sample.score;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 26. 8. 2018
 * Time: 12:16
 */
public class HighScoreItem {
    private SimpleStringProperty Poradi;
    private SimpleStringProperty JmenoHrace;
    private SimpleIntegerProperty HighScore;

    public HighScoreItem(String jmenoHrace, int highScore) {
        JmenoHrace = new SimpleStringProperty(jmenoHrace);
        HighScore = new SimpleIntegerProperty(highScore);
        Poradi = new SimpleStringProperty("0");
    }

    public String getPoradi() {
        return Poradi.get();
    }

    public void setPoradi(String poradi) {
        this.Poradi.set(poradi);
    }

    public String getJmenoHrace() {
        return JmenoHrace.get();
    }

    public void setJmenoHrace(String jmenoHrace) {
        this.JmenoHrace.set(jmenoHrace);
    }

    public int getHighScore() {
        return HighScore.get();
    }

    public void setHighScore(int highScore) {
        this.HighScore.set(highScore);
    }
}
