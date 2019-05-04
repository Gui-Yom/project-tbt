package lorganisation.projecttbt;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.ui.Menu;
import lorganisation.projecttbt.ui.Text;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.Pair;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;

public class MainMenu extends Menu {

    public MainMenu() {

        super();
        addComponent(new Text(new Coords(1, 5),
                              new StyledString("Project: TBT",
                                               Pair.of(0, Anscapes.Colors.RED_BRIGHT.fg()),
                                               Pair.of(9, Anscapes.Colors.YELLOW.fg()),
                                               Pair.of(10, Anscapes.Colors.BLUE.fg()),
                                               Pair.of(11, Anscapes.Colors.GREEN.fg())),
                              Utils.Align.CENTER));
        addComponent(new Text(new Coords(5, 1),
                              new StyledString("Appuyez sur 'ENTER' pour commencer !",
                                               Pair.of(12, Anscapes.BLINK_SLOW),
                                               Pair.of(16, Anscapes.BLINK_OFF),
                                               Pair.of(0, Anscapes.Colors.YELLOW.fg())),
                              Utils.Align.CENTER));
    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        renderer.render(this);

        while (input.getInput() != 13) {}
    }
}
