package lorganisation.projecttbt.ui.screens;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.ui.Button;
import lorganisation.projecttbt.ui.InvisibleButton;
import lorganisation.projecttbt.ui.Label;
import lorganisation.projecttbt.ui.Screen;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.Pair;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

public class MainScreen extends Screen {

    private boolean skip = false;

    public MainScreen() {

        super();
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
                                false, //FIXME
                                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        addComponent(btn);

        InvisibleButton ibtn = new InvisibleButton(() -> {
            Utils.clearTerm();
            System.exit(0);
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        addComponent(ibtn);
    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        renderer.render(this);

        keyPressed(input.readKey());

        if (!skip)
            display(input, renderer);

    }
}
