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

public class IntegerField extends InputWidget<Integer> {

    private StyledString prompt;
    private Map<Integer, String> modifiers;
    private Utils.Align alignement;
    private int maxValue;
    private int minValue;
    private int value;

    public IntegerField(Coords coords, StyledString prompt, Utils.Align alignement, int defaultValue, int minValue, int maxValue, Pair<Integer, String>... modifiers) {

        super(coords);

        this.modifiers = new TreeMap<>();

        if (prompt.modifiers() != null)
            for (Integer index : prompt.modifiers().keySet())
                this.modifiers.put(index, prompt.modifiers().get(index));

        if (modifiers != null)
            for (Pair<Integer, String> modifier : modifiers)
                this.modifiers.put(modifier.getU() + prompt.text().length(), modifier.getV());


        this.alignement = alignement;
        this.coords = coords;
        this.prompt = prompt;
        this.value = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;

        // FIXME
        /*
        addControl(null, "USE THE FCKING KEYBOARD (numbers only)");
        addControl(new KeyStroke('+', false, false), "+ -> Increment by one");
        addControl(new KeyStroke('-', false, false), "- -> Decrement by one");

         */
        setFocusable(true);
    }

    @Override
    public boolean handleInput(KeyStroke key) {

        if (key.getKeyCode() == KeyEvent.VK_BACKSPACE) { //backspace
            removeChar();
        } else if (key.getKeyChar() == '+')
            increment(+1);
        else if (key.getKeyChar() == '-')
            increment(-1);
        else if (Pattern.compile("([0-9])").matcher(Character.toString(key.getKeyChar())).find()) {
            appendChar(key.getKeyChar());
        } else {
            return false;
        }

        return true;
    }

    private boolean removeChar() {

        if (this.value / 10 < minValue) { //test without last value
            this.value = minValue;
        } else
            this.value = this.value / 10;
        return true;
    }

    private boolean appendChar(char key) {

        try {
            int temp = this.value * 10 + Integer.parseInt(String.valueOf(key));
            if (temp <= this.maxValue && temp >= minValue) {
                this.value = temp;
                return true;
            }
            return false;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean increment(int howMuch) {

        if (this.value + howMuch > maxValue)
            this.value = maxValue;
        else if (this.value + howMuch < minValue)
            this.value = minValue;
        else {
            this.value += howMuch;
        }
        return true;
    }

    @Override
    public String paint(Terminal term) {

        /*StringBuilder fill = new StringBuilder();
        for (int i = 0; i < maxValue / 10 - builder.length(); ++i)
            fill.append("_");*/
        StyledString string = new StyledString(prompt.text() + this.value + /*fill.toString() +*/ " +/- ", this.modifiers);

        return Utils.formattedLine(coords.getY(), coords.getX(), string, this.alignement, term.getSize().getColumns()) + Anscapes.RESET;
    }

    @Override
    public Integer getValue() {

        return this.value;
    }
}
