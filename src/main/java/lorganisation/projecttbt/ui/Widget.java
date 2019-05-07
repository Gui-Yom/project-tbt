package lorganisation.projecttbt.ui;

import lorganisation.projecttbt.utils.Coords;
import org.jline.terminal.Terminal;

public abstract class Widget {

    protected Coords coords;
    private boolean visibility = true;
    private boolean focusable = false;

    public Coords getCoords() {

        return coords;
    }

    //TODO: implement this
    //public abstract void onFocus();

    public boolean isFocusable() {
        return this.focusable;
    }

    public void setFocusable(boolean focus) {
        this.focusable = focus;
    }

    public boolean isVisible() {
        return this.visibility;
    }

    public void setVisible(boolean visible) {
        this.visibility = visible;
    }

    public abstract String render(Terminal term);

    public abstract boolean handleEvent(int key);
}
