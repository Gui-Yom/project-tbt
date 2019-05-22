package lorganisation.projecttbt.ui.screens;

import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.ColorMode;
import lorganisation.projecttbt.AssetsManager;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.ui.*;
import lorganisation.projecttbt.utils.*;
import org.jline.terminal.Terminal;

import javax.imageio.ImageIO;
import javax.swing.KeyStroke;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainScreen extends Screen {

    private boolean skip = false;

    public MainScreen(Terminal terminal) {

        super(terminal);

        try {

            BufferedImage image = ImageIO.read(AssetsManager.openResource("assets/images/logo.png"));

            ImageWidget background = new ImageWidget(new Coords(0, 0),
                                                     image,
                                                     ColorMode.RGB,
                                                     7);

            addComponent(background);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Label lbl = new Label(new Coords(0, 2),
                              new StyledString("Project: TBT",
                                               Pair.of(0, Anscapes.Colors.RED_BRIGHT.fg()),
                                               Pair.of(9, Anscapes.Colors.YELLOW.fg()),
                                               Pair.of(10, Anscapes.Colors.BLUE.fg()),
                                               Pair.of(11, Anscapes.Colors.GREEN.fg())),
                              Utils.Align.CENTER);
        addComponent(lbl);

        Button btn = new Button(new Coords(0, 8),
                                new StyledString("Press 'ENTER' to start",
                                                 Pair.of(12, Anscapes.BLINK_SLOW),
                                                 Pair.of(16, Anscapes.BLINK_OFF),
                                                 Pair.of(0, Anscapes.Colors.YELLOW.fg())),
                                Utils.Align.CENTER,
                                () -> skip = true,
                                false,
                                KeyUtils.KEY_ENTER);
        addComponent(btn);

        InvisibleButton iBtn = new InvisibleButton(() -> {
            TerminalUtils.clearTerm();
            System.exit(0);
        }, KeyUtils.KEY_ESCAPE);
        addComponent(iBtn);

        InvisibleButton refreshBtn = new InvisibleButton(() -> {}, KeyStroke.getKeyStroke('c'));
        addComponent(refreshBtn);
    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        while (!skip) {

            /*
            ImageWidget background = (ImageWidget) getComponents().get(0);
            if (background.getImageHeight() != input.getTerminal().getHeight()) {
                background.setImage(new TextImage());
            }

             */

            renderer.render(this);

            keyPressed(input.readKey());

            TerminalUtils.clearTerm();
        }

    }
}
