package lorganisation.projecttbt.ui;

import lorganisation.projecttbt.utils.Coords;

public class Text extends MenuComponent {

    protected String text;

    public Text(String id, Coords coords, String text) {

        this.id = id;
        this.text = text;
        this.coords = coords;
    }

    @Override
    public String render() {

        return text;
    }
}
