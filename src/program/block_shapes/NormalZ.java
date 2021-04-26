package program.block_shapes;

import javafx.scene.image.Image;
import program.game.Block;

/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 23. 8. 2018
 * Time: 19:15
 */
public class NormalZ extends Shape {
    public NormalZ(Image image) {
        super(image);
        this.shape = new Block[][] {
                {null,                  null,                   null,               null},
                {null,                  null,                   null,               null},
                {new Block(image),      new Block(image),       null,               null},
                {null,                  new Block(image),       new Block(image),   null},
        };
    }
}
