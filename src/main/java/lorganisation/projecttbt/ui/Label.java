package lorganisation.projecttbt.ui;

import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Terminal;

import javax.swing.KeyStroke;

public class Label extends Widget {

    protected StyledString stext;
    protected Utils.Align alignement;

    public Label(Coords coords, StyledString text, Utils.Align alignement) {

        super(coords);
        setCoords(coords);
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
    public String paint(Terminal terminal) {

        return Utils.formattedLine(coords.getY(), coords.getX(), stext, alignement, terminal.getSize().getColumns());
    }

    @Override
    public boolean handleInput(KeyStroke key) {

        return false;
    }
}
