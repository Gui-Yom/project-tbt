package lorganisation.projecttbt.ui.widget;

import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.TextImage;
import lorganisation.projecttbt.utils.Coords;
import org.jline.terminal.Terminal;

public class ImageWidget extends Widget {

    private TextImage image;

    public ImageWidget(Coords coords, TextImage image) {

        super(coords);
        this.image = image;
        setFocusable(false);
    }

    @Override
    public String paint(Terminal terminal) {

        return Anscapes.cursorPos(coords.getY(), coords.getX()) + image.getImage();
    }

    public int getImageWidth() {

        return image.getWidth();
    }

    public int getImageHeight() {

        return image.getHeight();
    }

    public TextImage getImage() {

        return image;
    }

    public void setImage(TextImage image) {

        this.image = image;
    }
}
