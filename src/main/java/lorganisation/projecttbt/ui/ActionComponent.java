package lorganisation.projecttbt.ui;

/**
 * Un composant capable d'effectuer une action lorsque activé même lorsqu'il n'est pas sélectionné.
 */
public abstract class ActionComponent extends Widget {

    protected Runnable action;
    protected char key;

    public char getKey() {

        return key;
    }

    public Runnable getAction() {

        return action;
    }
}
