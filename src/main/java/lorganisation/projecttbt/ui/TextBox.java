package lorganisation.projecttbt.ui;

import org.jline.terminal.Terminal;

public class TextBox extends ContainerComponent<String> {

    private String prompt;
    private StringBuilder builder;

    public TextBox(String prompt) {

        this.prompt = prompt;
        this.builder = new StringBuilder();
    }

    public void sendEvent(int key) {

        if (key == 8) //backspace
            builder.deleteCharAt(builder.length() - 1);
        else
            builder.append((char) key);
    }

    @Override
    public String render(Terminal term) {

        return prompt;
    }

    @Override
    public String getValue() {

        return null;
    }
}
