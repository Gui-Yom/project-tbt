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

        if (getControls().get(key) != null) {
            action.run();
            return true;
        }

        return false;
    }
}
