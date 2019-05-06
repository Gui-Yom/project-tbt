package lorganisation.projecttbt.ui;

import lorganisation.projecttbt.utils.Coords;
import org.jline.terminal.Terminal;

public abstract class Widget {

    protected Coords coords;

    public Coords getCoords() {

        return coords;
    }

    public abstract String render(Terminal term);

    public abstract boolean handleEvent(int key);
}
