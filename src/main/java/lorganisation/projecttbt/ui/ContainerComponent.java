package lorganisation.projecttbt.ui;

public abstract class ContainerComponent<E> extends MenuComponent {

    protected E selectedItem;

    public abstract E getValue();
}
