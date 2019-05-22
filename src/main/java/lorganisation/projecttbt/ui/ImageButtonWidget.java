package lorganisation.projecttbt.ui;

import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.TextImage;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.TerminalUtils;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;

import javax.swing.KeyStroke;

public class ImageButtonWidget extends Button {

    private Size size;
    private Utils.Align align;
    private TextImage image;

    public ImageButtonWidget(Coords coords, Size size, Utils.Align align, String imagePath, boolean focusable, StyledString text, Runnable function, KeyStroke... controls) {

        super(coords, text, align, function, focusable);

        this.size = size;

        //BufferedImage bufferedImage = AssetsManager.getImage(imagePath); TODO
        //this.image = new TextImage(image, size.getColumns(), size.getRows(), ColorMode.RGB); TODO
    }

    @Override
    public String paint(Terminal terminal) {

        Coords coords = TerminalUtils.coordinatesOfAlignedObject(this.coords.getY(), this.coords.getX(), image.getWidth(), align, terminal.getWidth());

        return Anscapes.cursorPos(coords.getY(), coords.getX()) + image.getImage() + Anscapes.moveLeft(image.getWidth() - (size.getColumns() + getText().length()) / 2) + getText();
    }
}
