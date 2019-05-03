package lorganisation.projecttbt.ui;

public class TextBox extends ContainerComponent<String> {

    private String prompt;
    private StringBuilder builder;

    public TextBox(String prompt) {

        this.prompt = prompt;
        this.builder = new StringBuilder();
    }

    public void sendEvent(int key) {
        if(key == 8) //backspace
            builder.deleteCharAt(builder.length() - 1);
        else
            builder.append((char)key);
    }

    @Override
    public String render() {

        return prompt;
    }

    @Override
    public String getSelectedItem() {

        return null;
    }
}
