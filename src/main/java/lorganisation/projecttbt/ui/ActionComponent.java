package lorganisation.projecttbt.ui;

public abstract class ActionComponent extends MenuComponent {

    protected Runnable action;
    protected char key;

    public char getKey() {

        return key;
    }

    public Runnable getAction() {

        return action;
    }
}
