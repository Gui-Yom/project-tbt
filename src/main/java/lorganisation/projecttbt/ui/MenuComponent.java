package lorganisation.projecttbt.ui;

import lorganisation.projecttbt.utils.Coords;
import org.jline.terminal.Terminal;

public abstract class MenuComponent {

    protected Coords coords;

    public Coords getCoords() {

        return coords;
    }

    public abstract String render(Terminal term);
}
