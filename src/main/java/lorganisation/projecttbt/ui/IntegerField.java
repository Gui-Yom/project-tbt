package lorganisation.projecttbt.ui;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.ansi.ANSITerminal;
import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.Pair;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class IntegerField extends ContainerWidget<Integer> {

    private StyledString prompt;
    private Map<Integer, String> modifiers;
    private Utils.Align alignement;
    private int maxValue;
    private int minValue;
    private int value;

    public IntegerField(Coords coords, StyledString prompt, Utils.Align alignement, int defaultValue, int minValue, int maxValue, Pair<Integer, String>... modifiers) {


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


        addControl(null, "USE THE FCKING KEYBOARD (numbers only)");
        addControl(new KeyStroke('+', false, false), "+ -> Increment by one");
        addControl(new KeyStroke('-', false, false), "- -> Decrement by one");
        setFocusable(true);
    }

    public boolean handleEvent(KeyStroke key) {

        if (key.getKeyType().equals(KeyType.Backspace)) { //backspace
            removeChar();
        } else if (key.getCharacter().equals('+')) //uparrow c galere
            increment(+1);
        else if (key.getCharacter().equals('-'))//down arrow c galere
            increment(-1);
        else if (Pattern.compile("([0-9])").matcher(key.getCharacter().toString()).find()) {
            appendChar(key.getCharacter());
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
    public String render(ANSITerminal term) {

        /*StringBuilder fill = new StringBuilder();
        for (int i = 0; i < maxValue / 10 - builder.length(); ++i)
            fill.append("_");*/
        StyledString string = new StyledString(prompt.text() + this.value + /*fill.toString() +*/ " +/- ", this.modifiers);

        try {
            return Utils.formattedLine(coords.getY(), coords.getX(), string, this.alignement, term.getTerminalSize().getColumns()) + Anscapes.RESET;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer getValue() {

        return this.value;
    }
}
