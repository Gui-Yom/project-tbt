package lorganisation.projecttbt.ui;

import lorganisation.projecttbt.utils.Pair;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Terminal;

public class InvisibleButton extends ActionWidget {

    public InvisibleButton(Runnable action, Pair<Integer, String>... controls) {

        setControls(Utils.pairArrayToMap(controls));
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
}
