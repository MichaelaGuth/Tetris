package sample.tvarykosticek;

import javafx.scene.image.Image;
import sample.game.Kosticka;

/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 23. 8. 2018
 * Time: 19:15
 */
public class Trubka extends Tvar {
    public Trubka(Image image) {
        super(image); //zavola konstruktor predka
        this.tvar = new Kosticka[][] {
                {new Kosticka(image),null,null,null},
                {new Kosticka(image),null,null,null},
                {new Kosticka(image),null,null,null},
                {new Kosticka(image),null,null,null}
        };

    }
}
