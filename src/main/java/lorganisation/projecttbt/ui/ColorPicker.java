package lorganisation.projecttbt.ui;

import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.AnsiColor;
import lorganisation.projecttbt.utils.*;
import org.jline.terminal.Terminal;

import java.util.List;

public class ColorPicker extends ContainerWidget<AnsiColor> {

    private CyclicList<Anscapes.Colors> availableColors;
    private Utils.Align alignement;

    public ColorPicker(Coords coords, List<Anscapes.Colors> availableColors, Utils.Align alignement) {

        this.coords = coords;
        this.availableColors = new CyclicList<>(availableColors);
        this.alignement = alignement;


        addControl('d', "D : NEXT Color");
        addControl('q', "Q : PREVIOUS Color");
        setFocusable(true);
    }

    public void setAvailableColors(List<Anscapes.Colors> availableColors) {

        this.availableColors = new CyclicList<>(availableColors);
    }

    @Override
    public String render(Terminal term) {

        StyledString string = new StyledString("  ", Pair.of(0, getValue().bg()));


        return Utils.formattedLine(coords.getY(), coords.getX(), string, this.alignement, term.getWidth()) + Anscapes.RESET;
    }

    @Override
    public boolean handleEvent(int keyCode) {

        char key = (char) keyCode;

        if (key == 'd')
            return this.availableColors.next() != null;
        else if (key == 'q')
            return this.availableColors.prev() != null;

        return false;
    }

    @Override
    public Anscapes.Colors getValue() {

        return this.availableColors.current();
    }
}
