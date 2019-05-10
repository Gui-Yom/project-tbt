package lorganisation.projecttbt.ui;

import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.Pair;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Terminal;

public class Button extends ActionWidget {

    private Utils.Align alignement;
    private StyledString string;

    public Button(Coords coords, StyledString string, Utils.Align alignement, Runnable action, boolean focusable, Pair<Integer, String>... controls) {

        setControls(Utils.pairArrayToMap(controls));

        this.string = string;
        this.alignement = alignement;
        this.coords = coords;
        this.action = action;

        setFocusable(focusable);
        setVisible(true);
    }

    public StyledString getText() {
        return this.string;
    }

    @Override
    public String render(Terminal terminal) {

        return Utils.formattedLine(coords.getY(), coords.getX(), this.string, this.alignement, terminal.getWidth());
    }
}
