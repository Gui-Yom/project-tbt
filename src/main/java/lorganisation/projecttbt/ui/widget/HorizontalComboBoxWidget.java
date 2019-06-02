package lorganisation.projecttbt.ui.widget;

import lorganisation.projecttbt.utils.*;
import org.jline.terminal.Terminal;

import javax.swing.KeyStroke;

public class HorizontalComboBoxWidget extends InputWidget<StyledString> {


    protected CyclicList<StyledString> options;
    protected Utils.Align alignement;

    public HorizontalComboBoxWidget(Coords coords, Utils.Align alignement, StyledString... options) {

        super(coords);

        this.alignement = alignement;
        this.options = new CyclicList<>(Utils.arrayToList(options));

        setFocusable(true);

        //TODO make these keys customizable, now not needed as this Widget is only used in GameScreen where Q / D are already taken
        setDescription("H. Combo box: J/K to cycle through options");
    }

    public void addOption(StyledString str) {

        options.add(str);
    }

    public CyclicList<StyledString> getOptions() {

        return this.options;
    }

    public void setOptions(StyledString... opt) {

        options = new CyclicList<>(opt);
    }

    public void resetOptions() {

        this.options = new CyclicList<>();
    }

    @Override
    public boolean handleInput(KeyStroke key) {

        if (key.getKeyChar() == 'k')
            return this.options.next() != null;
        else if (key.getKeyChar() == 'j')
            return this.options.prev() != null;

        return false;
    }

    @Override
    public StyledString getValue() {

        return options.current();
    }

    @Override
    public String paint(Terminal terminal) {


        StringBuilder visual = new StringBuilder("> ");
        visual.append(getValue()).append(" <");

        return TerminalUtils.formattedLine(coords.getY(), coords.getY(), visual.toString(), alignement, terminal.getWidth());
    }
}
