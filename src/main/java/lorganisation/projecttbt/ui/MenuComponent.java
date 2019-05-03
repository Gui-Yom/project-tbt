package lorganisation.projecttbt.ui;

import lorganisation.projecttbt.utils.Coords;

public abstract class MenuComponent {

    protected Coords coords;
    protected String id;

    public abstract String render();

    public Coords getCoords() {

        return coords;
    }
}
