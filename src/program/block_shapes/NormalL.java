package program.block_shapes;

import javafx.scene.image.Image;
import program.game.Block;

/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 23. 8. 2018
 * Time: 18:56
 */
public class NormalL extends Shape {
    public NormalL(Image image) {
        super(image); //zavola konstruktor predka
        this.shape = new Block[][] {
                {null,                  null,               null,   null},
                {new Block(image),      null,               null,   null},
                {new Block(image),      null,               null,   null},
                {new Block(image),      new Block(image),   null,   null},
        };
    }

}
