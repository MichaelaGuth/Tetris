package sample.tvarykosticek;

import javafx.scene.image.Image;
import sample.game.Kosticka;

/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 23. 8. 2018
 * Time: 19:14
 */
public class LkoMirror extends Tvar {
    public LkoMirror(Image image) {
        super(image); //zavola konstruktor predka
        this.tvar = new Kosticka[][] {
                {null,null,null,null},
                {null,new Kosticka(image),null,null},
                {null,new Kosticka(image),null,null},
                {new Kosticka(image),new Kosticka(image),null,null},
        };

    }
}
