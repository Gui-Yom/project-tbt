package lorganisation.projecttbt.ui;

public class TextBox extends ContainerComponent<String> {

    private String prompt;

    public TextBox(String prompt) {

        this.prompt = prompt;
    }

    @Override
    public String render() {

        return prompt;
    }
}
