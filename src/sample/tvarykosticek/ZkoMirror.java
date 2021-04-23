package sample.tvarykosticek;

import javafx.scene.image.Image;
import sample.game.Kosticka;

/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 23. 8. 2018
 * Time: 19:15
 */
public class ZkoMirror extends Tvar {
    public ZkoMirror (Image image) {
        super(image); //zavola konstruktor predka
        this.tvar = new Kosticka[][] {
                {null,null,null,null},
                {null,null,null,null},
                {null,new Kosticka(image),new Kosticka(image),null},
                {new Kosticka(image),new Kosticka(image),null,null},
        };

    }
}
