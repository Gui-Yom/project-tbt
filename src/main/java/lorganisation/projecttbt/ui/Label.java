package lorganisation.projecttbt.ui;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.ansi.ANSITerminal;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;

import java.io.IOException;

public class Label extends Widget {

    protected StyledString stext;
    protected Utils.Align alignement;

    public Label(Coords coords, StyledString text, Utils.Align alignement) {

        this.coords = coords;
        this.stext = text;
        this.alignement = alignement;

        setFocusable(false);
    }

    public StyledString getStyledText() {

        return stext;
    }

    public String getText() {

        return stext.text();
    }

    public void setText(String string) {

        stext.setText(string);
    }

    public Utils.Align getAlignement() {

        return this.alignement;
    }

    @Override
    public String render(ANSITerminal terminal) {

        try {
            return Utils.formattedLine(coords.getY(), coords.getX(), stext, alignement, terminal.getTerminalSize().getColumns());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean handleEvent(KeyStroke key) {

        return false;
    }
}
