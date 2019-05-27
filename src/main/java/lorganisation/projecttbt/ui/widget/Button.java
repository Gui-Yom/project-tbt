package lorganisation.projecttbt.ui.widget;

import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.TerminalUtils;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Terminal;

import javax.swing.KeyStroke;

public class Button extends ActionWidget {

    private Utils.Align alignement;
    private StyledString string;

    public Button(Coords coords, StyledString string, Utils.Align alignement, Runnable action, boolean focusable, KeyStroke control) {

        this(coords, string, alignement, "Press " + control.toString().replace("pressed ", ""), action, focusable, control);
    }

    public Button(Coords coords, StyledString string, Utils.Align alignement, String description, Runnable action, boolean focusable, KeyStroke control) {

        super(coords);

        this.string = string;
        this.alignement = alignement;
        this.setCoords(coords);
        this.action = action;
        this.control = control;

        setVisible(true);
        setFocusable(focusable);

        setDescription(description);
    }


    public StyledString getText() {

        return this.string;
    }

    @Override
    public String paint(Terminal terminal) {

        return TerminalUtils.formattedLine(getCoords().getY(),
                                           getCoords().getX(),
                                           this.string,
                                           this.alignement,
                                           terminal.getWidth());
    }
}
