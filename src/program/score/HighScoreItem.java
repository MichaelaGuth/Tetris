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
    private SimpleStringProperty Rank;
    private SimpleStringProperty PlayerName;
    private SimpleIntegerProperty HighScore;

    public HighScoreItem(String playerName, int highScore) {
        PlayerName = new SimpleStringProperty(playerName);
        HighScore = new SimpleIntegerProperty(highScore);
        Rank = new SimpleStringProperty("0");
    }

    public String getRank() {
        return Rank.get();
    }

    public void setRank(String rank) {
        this.Rank.set(rank);
    }

    public String getPlayerName() {
        return PlayerName.get();
    }

    public void setPlayerName(String playerName) {
        this.PlayerName.set(playerName);
    }

    public int getHighScore() {
        return HighScore.get();
    }

    public void setHighScore(int highScore) {
        this.HighScore.set(highScore);
    }
}
