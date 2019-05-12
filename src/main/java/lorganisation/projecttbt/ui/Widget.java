package lorganisation.projecttbt.ui;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.ansi.ANSITerminal;
import lorganisation.projecttbt.utils.Coords;

import java.util.HashMap;
import java.util.Map;

/**
 * Un composant de l'interface. (ex: un label ou un bouton)
 */
public abstract class Widget {

    protected Coords coords;
    protected boolean visible = true;
    protected boolean focusable = false;
    protected boolean activated = true;

    // int : keyCode, String : displayName  => if keyCode < 0, implicit controls like TYPE (TextField)
    private Map<KeyStroke, String> controls = new HashMap<>();

    public Coords getCoords() {

        return coords;
    }

    // TODO Event system
    // TODO implement onFocus
    //public abstract void onFocus();
    //public abstract void onFocusLost();

    public Map<KeyStroke, String> getControls() {

        return this.controls;
    }

    public void addControl(KeyStroke keyCode, String description) {

        this.controls.put(keyCode, description);
    }

    public boolean isFocusable() {

        return this.focusable;
    }

    public void setFocusable(boolean focus) {

        this.focusable = focus;
    }

    public boolean isActivated() {

        return this.activated;
    }

    public void setActivated(boolean activated) {

        this.activated = activated;
        setFocusable(activated);
    }

    public boolean isVisible() {

        return this.visible;
    }

    public void setVisible(boolean visible) {

        this.visible = visible;
    }

    public abstract String render(ANSITerminal terminal);

    public abstract boolean handleEvent(KeyStroke key);
}
