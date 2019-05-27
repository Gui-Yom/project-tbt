package lorganisation.projecttbt.ui.screen;

import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.ColorMode;
import lorganisation.projecttbt.AssetsManager;
import lorganisation.projecttbt.IntegratedDevenv;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.ui.widget.Button;
import lorganisation.projecttbt.ui.widget.ImageWidget;
import lorganisation.projecttbt.ui.widget.InvisibleButton;
import lorganisation.projecttbt.ui.widget.Label;
import lorganisation.projecttbt.utils.*;
import org.jline.terminal.Terminal;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainScreen extends Screen {

    private boolean skip = false;

    public MainScreen(Terminal terminal) {

        super(terminal);

        try {

            BufferedImage image = ImageIO.read(AssetsManager.openResource("assets/images/logo.png"));

            ImageWidget background = new ImageWidget(new Coords(0, 0),
                                                     IntegratedDevenv.convert(image,
                                                                              ColorMode.RGB,
                                                                              (float) 130 / 800));

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
        //addComponent(lbl);

        Button btn = new Button(new Coords(0, 25),
                                new StyledString("Press ENTER to start",
                                                 Pair.of(0, Anscapes.BLINK_SLOW),
                                                 Pair.of(22, Anscapes.BLINK_OFF),
                                                 Pair.of(0, Anscapes.Colors.MAGENTA_BRIGHT.fg() + Anscapes.Colors.BLACK.bg())),
                                Utils.Align.CENTER,
                                () -> skip = true,
                                false,
                                KeyUtils.KEY_ENTER);
        addComponent(btn);

        InvisibleButton iBtn = new InvisibleButton(() -> {
            TerminalUtils.clearTerm();
            TerminalUtils.exitPrivateMode();
            System.exit(0);
        }, KeyUtils.KEY_ESCAPE);
        addComponent(iBtn);
    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        while (!skip) {

            renderer.render(this);

            keyPressed(input.readKey());

            TerminalUtils.clearTerm();
        }

    }
}