package sample.obrazky;

import javafx.scene.image.Image;


/**
 * Created by IntelliJ IDEA.
 * User: Kimiko
 * Date: 2. 9. 2018
 * Time: 17:08
 */
public class ImageLoader {

    /**
     * načte obrázek z daného souboru
     * @param nazevSouboru název souboru, kde se nachízí obrázek
     * @return daný obrázek
     */
    public static Image LoadImage(String nazevSouboru) {
        Image image = new Image(ImageLoader.class.getResource(nazevSouboru).toExternalForm());
        return image;
    }

    /**
     * načte obrázek z daného souboru v určené velikosti
     * @param nazevSouboru název souboru, kde se nachízí obrázek
     * @param width šířka v pixelech
     * @param height výška v pixelech
     * @return daný obrázek
     */
    public static Image LoadImage(String nazevSouboru, int width, int height) {
        Image image = new Image(ImageLoader.class.getResource(nazevSouboru).toExternalForm(),width,height,true,false);
        return image;
    }
}
