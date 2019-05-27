package lorganisation.projecttbt.ui.widget;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.utils.*;
import org.jline.terminal.Terminal;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
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
                this.modifiers.put(modifier.getU() + prompt.rawText().length(), modifier.getV());


        this.alignement = alignement;
        this.coords = coords;
        this.prompt = prompt;
        this.value = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;

        setFocusable(true);
        setDescription("Number field +/-");
    }

    @Override
    public boolean handleInput(KeyStroke key) {

        if (key.getKeyCode() == KeyEvent.VK_BACK_SPACE) { //backspace
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

    public boolean increment(int howMuch) {

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
        StyledString string = new StyledString(prompt.rawText() + this.value + /*fill.toString() +*/ " +/- ", this.modifiers);

        return TerminalUtils.formattedLine(coords.getY(), coords.getX(), string, this.alignement, term.getSize().getColumns()) + Anscapes.RESET;
    }

    @Override
    public Integer getValue() {

        return this.value;
    }
}
