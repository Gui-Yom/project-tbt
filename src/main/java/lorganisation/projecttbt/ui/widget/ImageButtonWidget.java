package lorganisation.projecttbt.ui.widget;

import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.ColorMode;
import com.limelion.anscapes.ImgConverter;
import com.limelion.anscapes.TextImage;
import lorganisation.projecttbt.AssetsManager;
import lorganisation.projecttbt.IntegratedDevenv;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.TerminalUtils;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;

import javax.imageio.ImageIO;
import javax.swing.KeyStroke;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageButtonWidget extends Button {

    private Size size;
    private Utils.Align align;
    private TextImage image;

    public ImageButtonWidget(Coords coords, Size size, Utils.Align align, String imagePath, boolean focusable, StyledString text, Runnable function, KeyStroke control) {

        super(coords, text, align, control.toString().replaceAll("pressed", "").trim(), function, focusable, control);

        this.size = size;
        this.align = align;

        try {
            BufferedImage bufferedImage = ImageIO.read(AssetsManager.openResource(imagePath));
            TextImage textImage = IntegratedDevenv.convert(bufferedImage, ColorMode.RGB,
                                                           (float) size.getColumns() / bufferedImage.getWidth());
            String imageString = textImage.getImage();
            imageString = imageString.replaceAll(System.lineSeparator(), Anscapes.moveDown(1) + Anscapes.moveLeft(textImage.getWidth()));

            this.image = new TextImage(imageString, size.getColumns(), size.getRows(), ColorMode.RGB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String paint(Terminal terminal) {

        if (image == null) {
            return Anscapes.cursorPos(coords.getY(), coords.getX()) + "null image";
        }

        Coords coords = TerminalUtils.coordinatesOfAlignedObject(this.coords.getY(), this.coords.getX(), image.getWidth(), align, terminal.getWidth());

        // FIXME why do i have to divide height by 2

        StringBuilder visual = new StringBuilder(image.getImage());
        visual.insert(0, Anscapes.cursorPos(coords.getY(), coords.getX()));
        visual.append(Anscapes.cursorPos(coords.getY() + (int) Math.ceil((float) image.getHeight() / 2), coords.getX() + image.getWidth() / 2 - getText().length() / 2));
        visual.append(getText());

        //Anscapes.cursorPos(coords.getY(), coords.getX()) + image.getImage() + Anscapes.cursorPos(coords.getY() + this.size.getRows(), coords.getX()) + getText()
        return visual.toString();
    }

    public Size getSize() {

        return new Size(this.size.getColumns(), this.size.getRows() / 2);
    }

    public void onFocus() {

        int height = (int) Math.ceil((float) image.getHeight() / 2);
        Size size = getSize();
        size.setRows(2 + height);

        System.out.print(TerminalUtils.makeLine(new Coords(getCoords().getX() - 1, getCoords().getY() - 1), new Coords(getCoords().getX() + 2, getCoords().getY() - 1), '\u2588', Anscapes.Colors.MAGENTA_BRIGHT.fg()));
        TerminalUtils.writeAt(getCoords().getX() - 2, getCoords().getY() - 1,  Anscapes.Colors.MAGENTA_BRIGHT.fg() + '▀');
        System.out.print(TerminalUtils.makeLine(new Coords(coords.getX() + size.getColumns() - 3, coords.getY() + height + 1), new Coords(coords.getX() + size.getColumns(), coords.getY() + height + 1), '\u2588', Anscapes.Colors.CYAN_BRIGHT.fg()));
        TerminalUtils.writeAt(coords.getX() + size.getColumns() - 1, coords.getY() + height - 1, Anscapes.Colors.CYAN_BRIGHT.fg() + '\u2584');

    }
}
