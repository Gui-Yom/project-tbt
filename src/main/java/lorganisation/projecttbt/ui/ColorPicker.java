package lorganisation.projecttbt.ui;

import com.limelion.anscapes.AnsiColor;
import org.jline.terminal.Terminal;

import java.util.List;

public class ColorPicker extends ContainerWidget<AnsiColor> {

    private List<AnsiColor> availableColors;

    public ColorPicker(List<AnsiColor> availableColors) {

        this.availableColors = availableColors;
    }

    @Override
    public String render(Terminal term) {

        return null;
    }

    @Override
    public boolean handleEvent(int key) {

        return false;
    }

    @Override
    public AnsiColor getValue() {

        return null;
    }
}
