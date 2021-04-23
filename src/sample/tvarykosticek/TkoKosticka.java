package sample.tvarykosticek;

import javafx.scene.image.Image;
import sample.game.Kosticka;

/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 24. 8. 2018
 * Time: 12:09
 */
public class TkoKosticka extends Tvar {
    public TkoKosticka(Image image) {
        super(image); //zavola konstruktor predka
        this.tvar = new Kosticka[][] {
                {null,null,null,null},
                {null,null,null,null},
                {null,new Kosticka(image),null,null},
                {new Kosticka(image),new Kosticka(image),new Kosticka(image),null},
        };
    }
}
