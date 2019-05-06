package lorganisation.projecttbt.ui;

import org.jline.terminal.Terminal;

import java.util.regex.Pattern;

public class IntegerField extends ContainerWidget<Integer> {

    private String prompt;
    private StringBuilder builder;
    private int maxValue;
    private int value;

    public IntegerField(String prompt, int defaultValue, int maxValue) {

        this.prompt = prompt;
        this.maxValue = maxValue;
        this.builder = new StringBuilder();
        this.value = defaultValue;

        for (int i = 0; i < String.valueOf(this.maxValue).length(); i++)
            this.builder.append("_");
    }

    public boolean handleEvent(int key) {
        int typedLength = builder.indexOf("_");

        if (key == 8) { //backspace
            builder.deleteCharAt(typedLength - 1);
            this.value = Integer.parseInt(builder.toString());
        } else if (key == 0) { //uparrow
            this.value++;
            this.builder = new StringBuilder(value);
        } else if (key == 0) {//down arrow
            this.value--;
            this.builder = new StringBuilder(value);
        } else if (Pattern.compile("([0-9])").matcher(String.valueOf(key)).find()) {
            builder.replace(typedLength - 1, typedLength, String.valueOf((char) key));
            this.value = Integer.parseInt(builder.toString());
        } else
            return false;

        return true;
    }

    @Override
    public String render(Terminal term) {

        return prompt + builder.substring(0, maxSize);
    }

    @Override
    public String getValue() {

        return builder.substring(0 , builder.indexOf("_", maxSize));
    }
}
