package lorganisation.projecttbt.ui;

import org.jline.terminal.Terminal;

import javax.swing.KeyStroke;
import java.util.Arrays;

public class InvisibleButton extends ActionWidget {

    public InvisibleButton(Runnable action, KeyStroke... controls) {

        super(null);

        getControls().addAll(Arrays.asList(controls));
        this.action = action;

        setVisible(false);
        setFocusable(false);
    }

    @Override
    public String paint(Terminal terminal) {

        return null;
    }
}
