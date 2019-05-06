package lorganisation.projecttbt.ui;

import org.jline.terminal.Terminal;

import java.util.regex.Pattern;

public class TextField extends ContainerWidget<String> {

    private String prompt;
    private StringBuilder builder;
    private int maxSize;
    private int typedLength;

    public TextField(String prompt, int maxSize) {

        this.prompt = prompt;
        this.maxSize = maxSize;
        this.builder = new StringBuilder();
        this.typedLength = 0;

        for (int i = 0; i < this.maxSize; i++)
            this.builder.append("_");
    }

    public boolean handleEvent(int key) {

        if (key == 8) //backspace
            builder.deleteCharAt(typedLength - 1);
        else if (Pattern.compile("([a-zA-Z_ @.\\-0-9])").matcher(String.valueOf(key)).find()) {
            builder.replace(typedLength - 1, typedLength, String.valueOf((char) key));
            typedLength = builder.indexOf("_");
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
