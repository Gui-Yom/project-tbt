package lorganisation.projecttbt.ui.widget;

import org.jline.terminal.Terminal;

import javax.swing.KeyStroke;

public class InvisibleButton extends ActionWidget {

    public InvisibleButton(Runnable action, KeyStroke control) {

        super(null);

        this.action = action;
        this.control = control;

        setVisible(false);
        setFocusable(false);
    }

    @Override
    public String paint(Terminal terminal) {

        return null;
    }
}
