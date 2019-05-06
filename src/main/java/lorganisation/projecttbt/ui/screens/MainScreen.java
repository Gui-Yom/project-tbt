package lorganisation.projecttbt.ui.screens;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.ui.Label;
import lorganisation.projecttbt.ui.Screen;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.Pair;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;

public class MainScreen extends Screen {

    public MainScreen() {

        super();
        addComponent(new Label(new Coords(0, 2),
                              new StyledString("Project: TBT",
                                               Pair.of(0, Anscapes.Colors.RED_BRIGHT.fg()),
                                               Pair.of(9, Anscapes.Colors.YELLOW.fg()),
                                               Pair.of(10, Anscapes.Colors.BLUE.fg()),
                                               Pair.of(11, Anscapes.Colors.GREEN.fg())),
                              Utils.Align.CENTER));
        addComponent(new Label(new Coords(0, 8
        ),
                              new StyledString("Appuyez sur 'ENTER' pour commencer !",
                                               Pair.of(12, Anscapes.BLINK_SLOW),
                                               Pair.of(16, Anscapes.BLINK_OFF),
                                               Pair.of(0, Anscapes.Colors.YELLOW.fg())),
                              Utils.Align.CENTER));
    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        renderer.render(this);

        while (true) {
            int key = input.getInput();

            if (key == 13)
                break;
            if (key == 27) {
                Utils.clearTerm();
                System.exit(0);
            }
        }
    }
}
