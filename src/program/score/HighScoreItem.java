package program.score;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 26. 8. 2018
 * Time: 12:16
 */
public class HighScoreItem {
    private SimpleStringProperty rank;
    private SimpleStringProperty playerName;
    private SimpleIntegerProperty highScore;

    public HighScoreItem(String playerName, int highScore) {
        this.playerName = new SimpleStringProperty(playerName);
        this.highScore = new SimpleIntegerProperty(highScore);
        rank = new SimpleStringProperty("0");
    }

    public String getRank() {
        return rank.get();
    }

    public void setRank(String rank) {
        this.rank.set(rank);
    }

    public String getPlayerName() {
        return playerName.get();
    }

    public void setPlayerName(String playerName) {
        this.playerName.set(playerName);
    }

    public int getHighScore() {
        return highScore.get();
    }

    public void setHighScore(int highScore) {
        this.highScore.set(highScore);
    }
}
