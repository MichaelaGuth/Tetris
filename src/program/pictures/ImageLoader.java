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
     * TODO
     * načte obrázek z daného souboru
     * @param fileName název souboru, kde se nachízí obrázek
     * @return daný obrázek
     */
    public static Image LoadImage(String fileName) {
        return new Image(ImageLoader.class.getResource(fileName).toExternalForm());
    }

    /**
     * TODO
     * načte obrázek z daného souboru v určené velikosti
     * @param fileName název souboru, kde se nachízí obrázek
     * @param width šířka v pixelech
     * @param height výška v pixelech
     * @return daný obrázek
     */
    public static Image LoadImage(String fileName, int width, int height) {
        return new Image(
                ImageLoader.class.getResource(fileName).toExternalForm(),
                width,
                height,
                true,
                false);
    }

}
