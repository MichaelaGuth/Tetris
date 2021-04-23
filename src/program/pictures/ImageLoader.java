package program.pictures;

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
     * @param fileName název souboru, kde se nachízí obrázek
     * @return daný obrázek
     */
    public static Image LoadImage(String fileName) {
        Image image = new Image(ImageLoader.class.getResource(fileName).toExternalForm());
        return image;
    }

    /**
     * načte obrázek z daného souboru v určené velikosti
     * @param fileName název souboru, kde se nachízí obrázek
     * @param width šířka v pixelech
     * @param height výška v pixelech
     * @return daný obrázek
     */
    public static Image LoadImage(String fileName, int width, int height) {
        Image image = new Image(ImageLoader.class.getResource(fileName).toExternalForm(),width,height,true,false);
        return image;
    }
}
