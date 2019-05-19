package lorganisation.projecttbt.ui;

import com.limelion.anscapes.Anscapes;
import com.sun.glass.events.KeyEvent;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.Pair;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Terminal;

import javax.swing.KeyStroke;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class TextField extends InputWidget<String> {

    private StyledString prompt;
    private StringBuilder builder;
    private Utils.Align alignement;
    private Map<Integer, String> modifiers;
    private int maxSize;

    public TextField(Coords coords, StyledString prompt, Utils.Align alignement, int maxSize, Pair<Integer, String>... modifiers) {

        super(coords);

        this.modifiers = new TreeMap<>();

        if (prompt.modifiers() != null) {
            for (Integer index : prompt.modifiers().keySet())
                this.modifiers.put(index, prompt.modifiers().get(index));
        }

        if (modifiers != null) {
            for (Pair<Integer, String> modifier : modifiers)
                this.modifiers.put(modifier.getU() + prompt.text().length(), modifier.getV());
        }


        this.alignement = alignement;
        this.setCoords(coords);
        this.prompt = prompt;
        this.maxSize = maxSize;
        this.builder = new StringBuilder();

        getControls().add(null);
    }

    public boolean handleEvent(KeyStroke key) {

        int typedLength = builder.length();

        if (key.getKeyCode() == KeyEvent.VK_BACKSPACE) { //backspace
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
        StyledString string = new StyledString(prompt.text() + builder.toString() + fill.toString(), this.modifiers);

        return Utils.formattedLine(getCoords().getY(), getCoords().getX(), string, this.alignement, term.getWidth()) + Anscapes.RESET;
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
