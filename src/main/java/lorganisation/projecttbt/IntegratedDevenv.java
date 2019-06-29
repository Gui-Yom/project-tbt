package lorganisation.projecttbt;

import com.limelion.anscapes.ColorMode;
import com.limelion.anscapes.ImgConverter;
import com.limelion.anscapes.TextImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Regroupe des méthodes utilitaires pour la création de contenu.
 */
public class IntegratedDevenv {

    public static void devenv() {

        // Met en place l'environement de développement.
        // Extrait les ressources du jeu
        System.out.println("Mise en place de l'environnement de développement :");
        System.out.println("    Extraction des ressources ...");

        if (!AssetsManager.extract("bots.txt")) {
            System.err.println("Unable to extract 'bots.txt'.");
        }
        for (String file : AssetsManager.gameMapFiles())
            if (!AssetsManager.extract(file))
                System.err.println("Unable to extract '" + file + "'.");
        for (String file : AssetsManager.gameCharacterFiles())
            if (!AssetsManager.extract(file))
                System.err.println("Unable to extract '" + file + "'.");

        System.out.println("Mise en place de l'environnement de développement :");
        System.out.println("    Extraction des ressources ... OK");
    }

    /**
     * Print to stdout
     *
     * @param mode
     * @param reduction
     * @param f
     */
    public static void convert(ColorMode mode, float reduction, File f) {

        if (!f.exists()) {
            System.err.println("Given file doesn't exist. ('" + f.getAbsolutePath() + "')");
            System.exit(-1);
        }

        try {
            BufferedImage image = ImageIO.read(f);
            System.out.print(convert(image, mode, reduction));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts BufferedImage to TextImage
     *
     * @param image     BufferedImage
     * @param mode      ColorMode used to convert
     * @param reduction reduction ratio
     *
     * @return converted TextImage
     */
    public static TextImage convert(BufferedImage image, ColorMode mode, float reduction) {

        ImgConverter converter = ImgConverter.builder()
                                             .mode(mode)
                                             .scale(reduction)
                                             .scaling(ImgConverter.Scaling.SMOOTH)
                                             .build();

        return converter.convert(image);
    }
}
