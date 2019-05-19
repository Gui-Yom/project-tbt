package lorganisation.projecttbt.ui;

import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Terminal;

import javax.swing.KeyStroke;
import java.util.Arrays;

public class Button extends ActionWidget {

    private Utils.Align alignement;
    private StyledString string;

    public Button(Coords coords, StyledString string, Utils.Align alignement, Runnable action, boolean focusable, KeyStroke... controls) {

        super(coords);

        getControls().addAll(Arrays.asList(controls));

        this.string = string;
        this.alignement = alignement;
        this.setCoords(coords);
        this.action = action;

        setVisible(true);
    }

    public StyledString getText() {

        return this.string;
    }

    @Override
    public String paint(Terminal terminal) {

        return Utils.formattedLine(getCoords().getY(), getCoords().getX(), this.string, this.alignement, terminal.getWidth());
    }
}
