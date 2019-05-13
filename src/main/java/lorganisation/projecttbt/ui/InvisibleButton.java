package lorganisation.projecttbt.ui;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.ansi.ANSITerminal;
import lorganisation.projecttbt.utils.Pair;

public class InvisibleButton extends ActionWidget {

    public InvisibleButton(Runnable action, Pair<KeyStroke, String>... controls) {


        for (Pair<KeyStroke, String> control : controls) {
            addControl(control.getU(), control.getV());
        }
        this.action = action;

        setVisible(false);
    }

    @Override
    public void setActivated(boolean activated) {

        this.activated = activated;
    }

    @Override
    public boolean isFocusable() {

        return false;
    }

    @Override
    public boolean isVisible() {

        return false;
    }

    @Override
    public String render(ANSITerminal terminal) {

        return null;
    }
}
