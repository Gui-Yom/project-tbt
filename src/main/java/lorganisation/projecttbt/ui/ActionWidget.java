package lorganisation.projecttbt.ui;

import com.googlecode.lanterna.input.KeyStroke;

/**
 * Un composant capable d'effectuer une action lorsque activé même lorsqu'il n'est pas sélectionné.
 */
public abstract class ActionWidget extends Widget {

    protected Runnable action;

    public Runnable getAction() {

        return action;
    }

    @Override
    public boolean handleEvent(KeyStroke key) {


        this.visible = false;
        for (KeyStroke keyStroke : getControls().keySet())
            if (keyStroke.equals(key)) {
                action.run();
                return true;
            }

        return false;
    }
}
