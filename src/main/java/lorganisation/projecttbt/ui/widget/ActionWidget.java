package lorganisation.projecttbt.ui.widget;


import lorganisation.projecttbt.utils.Coords;

import javax.swing.KeyStroke;

/**
 * Un composant capable d'effectuer une action lorsque activé même lorsqu'il n'est pas sélectionné.
 */
public abstract class ActionWidget extends Widget {

    protected Runnable action;

    public ActionWidget(Coords coords) {

        super(coords);
    }

    public Runnable getAction() {

        return action;
    }

    @Override
    public boolean handleInput(KeyStroke key) {

        if (control.equals(key)) {
            action.run();
            return true;
        }
        return false;
    }
}
