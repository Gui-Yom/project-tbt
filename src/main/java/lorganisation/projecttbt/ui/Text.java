package lorganisation.projecttbt.ui;

public class Text extends MenuComponent {

    protected String text;

    public Text(String text) {

        this.text = text;
    }

    @Override
    public String render() {

        return text;
    }
}
