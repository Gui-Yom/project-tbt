package lorganisation.projecttbt.ui.widget;

import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.TerminalUtils;
import org.jline.terminal.Terminal;

public class LoadingBar extends Widget {

    private float percent = 0;
    private int barLength;
    private char fillChar;
    private String modifiers;

    public LoadingBar(Coords coords, int barLength, char fillChar, String modifiers) {

        super(coords);

        this.barLength = barLength;
        this.fillChar = fillChar;
        this.modifiers = modifiers;
    }

    public float getPercent() {

        return percent;
    }

    public void setPercent(float percent) {

        this.percent = percent;
    }

    @Override
    public String paint(Terminal terminal) {

        return TerminalUtils.makeLine(coords,
                                      new Coords(coords.getX() + Math.round(barLength * percent), coords.getY()),
                                      fillChar,
                                      modifiers);
    }
}
