package lorganisation.projecttbt.ui;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.ansi.ANSITerminal;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.Pair;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;

import java.io.IOException;

public class Button extends ActionWidget {

    private Utils.Align alignement;
    private StyledString string;

    public Button(Coords coords, StyledString string, Utils.Align alignement, Runnable action, boolean focusable, Pair<KeyStroke, String>... controls) {

        for (Pair<KeyStroke, String> control : controls) {
            addControl(control.getU(), control.getV());
        }

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
    public String render(ANSITerminal terminal) {

        try {
            return Utils.formattedLine(coords.getY(), coords.getX(), this.string, this.alignement, terminal.getTerminalSize().getColumns());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
