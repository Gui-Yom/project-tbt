package lorganisation.projecttbt.ui;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.ansi.ANSITerminal;
import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.AnsiColor;
import lorganisation.projecttbt.utils.*;

import java.io.IOException;
import java.util.List;

public class ColorPicker extends ContainerWidget<AnsiColor> {

    private CyclicList<Anscapes.Colors> availableColors;
    private Utils.Align alignement;

    public ColorPicker(Coords coords, List<Anscapes.Colors> availableColors, Utils.Align alignement) {

        this.coords = coords;
        this.availableColors = new CyclicList<>(availableColors);
        this.alignement = alignement;


        addControl(new KeyStroke('d', false, false), "D : NEXT Color");
        addControl(new KeyStroke('q', false, false), "Q : PREVIOUS Color");
        setFocusable(true);
    }

    public void setAvailableColors(List<Anscapes.Colors> availableColors) {

        this.availableColors = new CyclicList<>(availableColors);
    }

    @Override
    public String render(ANSITerminal term) {

        StyledString string = new StyledString("  ", Pair.of(0, getValue().bg()));


        try {
            return Utils.formattedLine(coords.getY(), coords.getX(), string, this.alignement, term.getTerminalSize().getColumns()) + Anscapes.RESET;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean handleEvent(KeyStroke keyCode) {

        if (keyCode.getCharacter().equals('d'))
            return this.availableColors.next() != null;
        else if (keyCode.getCharacter().equals('q'))
            return this.availableColors.prev() != null;

        return false;
    }

    @Override
    public Anscapes.Colors getValue() {

        return this.availableColors.current();
    }
}
