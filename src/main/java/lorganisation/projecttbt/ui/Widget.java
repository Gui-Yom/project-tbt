package lorganisation.projecttbt.ui;

import lorganisation.projecttbt.utils.Coords;
import org.jline.terminal.Terminal;

import java.util.Map;
import java.util.TreeMap;

public abstract class Widget {

    protected Coords coords;
    private boolean visibility = true;
    private boolean focusable = false;

    // int : keyCode, String : displayName  => if keyCode < 0, implicit controls like TYPE (TextField)
    private Map<Integer, String> controls = new TreeMap<>();

    public Coords getCoords() {

        return coords;
    }

    //TODO: implement this
    //public abstract void onFocus();

    public Map<Integer, String> getControls() {

        return this.controls;
    }

    public void setControls(Map<Integer, String> controls) {

        this.controls = controls;
    }

    public void addControl(int keyCode, String description) {

        this.controls.put(keyCode, description);
    }

    public void onFocused() {}

    public void onFocusLost() {}

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
