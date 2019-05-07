package lorganisation.projecttbt.ui.screens;

import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.ui.Button;
import lorganisation.projecttbt.ui.IntegerField;
import lorganisation.projecttbt.ui.Screen;
import lorganisation.projecttbt.ui.Label;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;

public class TestScreen extends Screen {

    private boolean pass = false;

    public TestScreen() {

        super();


        addComponent(new Button((char) 13, () -> this.pass = true));
        addComponent(new Label(new Coords(0, 5), new StyledString("PRESS ENTER TO PASS"), Utils.Align.CENTER));
        //addComponent(new TextField(new Coords(2, 8), new StyledString("PLEASE TYPE IN SOME SHIT: "), Utils.Align.LEFT, 10));
        setFocused(addComponent(new IntegerField(new Coords(2, 8), new StyledString("PLEASE TYPE IN SOME SHIT: "), Utils.Align.LEFT, 1, 1, 100)));
        addComponent(new IntegerField(new Coords(2, 9), new StyledString("PLEASE TYPE IN SOME SECOND SHIT: "), Utils.Align.LEFT, 1, 1, 10));
    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        Utils.clearTerm();
        renderer.render(this);

        Utils.writeAt(0,this.getFocusedWidget().getCoords().getY(), "> ");

        while (!pass) {
            char key = (char) input.getInput();

            keyPressed(key);
                display(input, renderer);
        }

    }
}
