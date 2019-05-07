package lorganisation.projecttbt.ui;

import org.jline.terminal.Terminal;

public class Button extends ActionWidget {

    public Button(char key, Runnable action) {

        this.key = key;
        this.action = action;

        setVisible(false);
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public String render(Terminal terminal) {

        return null;
    }

    @Override
    public boolean handleEvent(int key) {

        return false;
    }
}
