package lorganisation.projecttbt.ui;

import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.AnsiColor;
import lorganisation.projecttbt.utils.*;
import org.jline.terminal.Terminal;

import javax.swing.KeyStroke;
import java.util.List;

public class ColorPicker extends InputWidget<AnsiColor> {

    private CyclicList<Anscapes.Colors> availableColors;
    private Utils.Align alignement;

    public ColorPicker(Coords coords, List<Anscapes.Colors> availableColors, Utils.Align alignement) {

        super(coords);


        this.coords = coords;
        this.availableColors = new CyclicList<>(availableColors);
        this.alignement = alignement;

        setFocusable(true);
        setDescription("Color picker Q/D to cycle through colors");
    }

    public void setAvailableColors(List<Anscapes.Colors> availableColors) {

        this.availableColors = new CyclicList<>(availableColors);
    }

    @Override
    public String paint(Terminal term) {

        StyledString string = new StyledString("  ", Pair.of(0, getValue().bg()));

        return TerminalUtils.formattedLine(coords.getY(), coords.getX(), string, this.alignement, term.getSize().getColumns()) + Anscapes.RESET;
    }

    @Override
    public boolean handleInput(KeyStroke key) {

        if (key.getKeyChar() == 'd')
            return this.availableColors.next() != null;
        else if (key.getKeyChar() == 'q')
            return this.availableColors.prev() != null;

        return false;
    }

    @Override
    public Anscapes.Colors getValue() {

        return this.availableColors.current();
    }
}
