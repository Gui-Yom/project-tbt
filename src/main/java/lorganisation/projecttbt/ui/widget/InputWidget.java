package lorganisation.projecttbt.ui.widget;

import lorganisation.projecttbt.utils.Coords;

public abstract class InputWidget<E> extends Widget {

    protected E selectedItem;

    public InputWidget(Coords coords) {

        super(coords);
    }

    public abstract E getValue();
}
