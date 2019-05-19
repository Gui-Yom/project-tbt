package lorganisation.projecttbt.ui;

import lorganisation.projecttbt.utils.Coords;
import org.jline.terminal.Terminal;

import javax.swing.KeyStroke;
import java.util.ArrayList;
import java.util.List;

/**
 * Un composant de l'interface. (ex: un label ou un bouton)
 */
public abstract class Widget {

    // Coordonnées du composant
    protected Coords coords;
    protected boolean visible;
    protected boolean enabled;
    protected boolean focusable;

    // Mappe les entrées clavier à des fonctions
    protected List<KeyStroke> controls;

    // La description du composant qui sera affichée lorsque le composant aura le focus
    protected String description;

    public Widget(Coords coords) {

        this.coords = coords;
        this.description = "";
        this.focusable = true;
        this.enabled = true;
        this.visible = true;
        this.controls = new ArrayList<>();
    }

    public Coords getCoords() {

        return coords;
    }

    public void setCoords(Coords coords) {

        this.coords = coords;
    }

    public List<KeyStroke> getControls() {

        return this.controls;
    }

    public boolean isFocusable() {

        return focusable;
    }

    public void setFocusable(boolean focusable) {

        this.focusable = focusable;
    }

    public void onFocus() {

    }

    public void onFocusLost() {

    }

    public boolean isEnabled() {

        return this.enabled;
    }

    public void setEnabled(boolean enabled) {

        this.enabled = enabled;
    }

    public boolean isVisible() {

        return this.visible;
    }

    public void setVisible(boolean visible) {

        this.visible = visible;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String desc) {

        this.description = desc;
    }

    /**
     * Un composant est global si il n'est pas affiché
     *
     * @return
     */
    public boolean isGlobal() {

        return coords == null;
    }

    public abstract String paint(Terminal terminal);

    public boolean handleInput(KeyStroke key) {

        return false;
    }
}
