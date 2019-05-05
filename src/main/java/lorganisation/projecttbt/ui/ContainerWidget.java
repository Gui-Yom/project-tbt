package lorganisation.projecttbt.ui;

public abstract class ContainerWidget<E> extends Widget {

    protected E selectedItem;

    public abstract E getValue();
}
