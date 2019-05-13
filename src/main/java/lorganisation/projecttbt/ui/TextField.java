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

public class TextField extends ContainerWidget<String> {

    private StyledString prompt;
    private StringBuilder builder;
    private Utils.Align alignement;
    private Map<Integer, String> modifiers;
    private int maxSize;

    public TextField(Coords coords, StyledString prompt, Utils.Align alignement, int maxSize, Pair<Integer, String>... modifiers) {


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
        this.coords = coords;
        this.prompt = prompt;
        this.maxSize = maxSize;
        this.builder = new StringBuilder();

        addControl(null, "TYPE (use keyboard idiot)");
        setFocusable(true);
    }

    public boolean handleEvent(KeyStroke key) {

        int typedLength = builder.length();

        if (key.getKeyType().equals(KeyType.Backspace)) {//backspace
            if (typedLength > 0)
                builder.deleteCharAt(typedLength - 1);
        } else if (builder.length() < maxSize && key.getCharacter() != null && Pattern.compile("^\\w{1}$").matcher(key.getCharacter().toString()).find()) {
            builder.append(key.getCharacter());
        } else
            return false;

        return true;
    }

    @Override
    public String render(ANSITerminal term) {

        StringBuilder fill = new StringBuilder();
        for (int i = 0; i < maxSize - builder.length(); ++i)
            fill.append("_");
        StyledString string = new StyledString(prompt.text() + builder.toString() + fill.toString(), this.modifiers);


        try {
            return Utils.formattedLine(coords.getY(), coords.getX(), string, this.alignement, term.getTerminalSize().getColumns()) + Anscapes.RESET;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
