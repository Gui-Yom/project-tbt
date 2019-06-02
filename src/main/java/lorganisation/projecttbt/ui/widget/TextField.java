package lorganisation.projecttbt.ui.widget;

import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.TerminalUtils;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Terminal;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * Un endroit où l'utilisateur peut entrer du texte
 */
public class TextField extends InputWidget<String> {

    private StyledString prompt;
    private StringBuilder builder;
    private Utils.Align alignement;
    private Map<Integer, String> modifiers;
    private int maxSize;

    public TextField(Coords coords, StyledString prompt, Utils.Align alignement, int maxSize) {

        super(coords);

        this.modifiers = new TreeMap<>();

        this.alignement = alignement;
        this.setCoords(coords);
        this.prompt = prompt;
        this.maxSize = maxSize;
        this.builder = new StringBuilder();

        setDescription("TextField: type");
    }

    @Override
    public boolean handleInput(KeyStroke key) {

        int typedLength = builder.length();

        if (key.getKeyCode() == KeyEvent.VK_BACK_SPACE) { //backspace
            if (typedLength > 0)
                builder.deleteCharAt(typedLength - 1);
        } else if (builder.length() < maxSize && Pattern.compile("^\\w{1}$").matcher(Character.toString(key.getKeyChar())).find()) {
            builder.append(key.getKeyChar());
        } else
            return false;

        return true;
    }

    @Override
    public String paint(Terminal term) {

        StringBuilder fill = new StringBuilder();
        for (int i = 0; i < maxSize - builder.length(); ++i)
            fill.append("_");

        return TerminalUtils.formattedLine(getCoords().getY(), getCoords().getX(),
                                           prompt + builder.toString() + fill,
                                           this.alignement,
                                           term.getWidth());
    }

    @Override
    public String getValue() {

        return builder.toString().trim();
    }

    public void setValue(String str) {

        if (str.length() <= maxSize)
            this.builder = new StringBuilder(str);
    }
}
