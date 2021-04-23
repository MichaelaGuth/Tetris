package program.game;

import javafx.scene.image.Image;

/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 22. 8. 2018
 * Time: 21:32
 */
public class Block {
    private final Image block;

    public Block(Image image) {
        block = image;
    }

    public Image getBlock() {
        return block;
    }
}
