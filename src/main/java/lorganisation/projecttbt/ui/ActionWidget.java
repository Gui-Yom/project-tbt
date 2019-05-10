package lorganisation.projecttbt.ui;

/**
 * Un composant capable d'effectuer une action lorsque activé même lorsqu'il n'est pas sélectionné.
 */
public abstract class ActionWidget extends Widget {

    protected Runnable action;

    public Runnable getAction() {

        return action;
    }

    @Override
    public boolean handleEvent(int key) {

        if (getControls().get(key) != null) {
            action.run();
            return true;
        }

        return false;
    }
}
