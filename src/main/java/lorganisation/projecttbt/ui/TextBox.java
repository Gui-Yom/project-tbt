package lorganisation.projecttbt.ui;

import org.jline.terminal.Terminal;

public class TextBox extends ContainerWidget<String> {

    private String prompt;
    private StringBuilder builder;
    private int maxSize;
    private int typedLength;

    public TextBox(String prompt, int maxSize) {

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
        else
            builder.replace(typedLength - 1, typedLength, String.valueOf((char) key));

        return true;
    }

    @Override
    public String render(Terminal term) {

        return prompt + builder.substring(0, maxSize);
    }

    @Override
    public String getValue() {

        return builder.substring(0 , maxSize);
    }
}
