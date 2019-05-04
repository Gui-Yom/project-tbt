package lorganisation.projecttbt.ui;

import org.jline.terminal.Terminal;

public class Button extends ActionComponent {

    public Button(char key, Runnable action) {

        this.key = key;
        this.action = action;
    }

    @Override
    public String render(Terminal terminal) {

        return null;
    }
}
