package program.block_shapes;

import javafx.scene.image.Image;
import program.game.Block;

/**
 * Created by IntelliJ IDEA.
 * User: MichaelaGuth
 * Date: 23. 8. 2018
 * Time: 19:15
 */
public class Tube extends Shape {
    public Tube(Image image) {
        super(image);
        this.shape = new Block[][] {
                {new Block(image),  null,   null,   null},
                {new Block(image),  null,   null,   null},
                {new Block(image),  null,   null,   null},
                {new Block(image),  null,   null,   null}
        };

    }
}
