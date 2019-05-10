package lorganisation.projecttbt.ui.screens;

import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.ui.*;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.Pair;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;

public class TestScreen extends Screen {

    private boolean skip = false;

    public TestScreen(Game game) {

        super();

        //addComponent(new InvisibleButton(() -> this.skip = true, Pair.of(13, "ENTER : SKIP this screen")));
        addComponent(new Label(new Coords(0, 5), new StyledString("PRESS ENTER TO PASS"), Utils.Align.CENTER));
        setFocused(addComponent(new TextField(new Coords(2, 8), new StyledString("PLEASE TYPE IN SOME SHIT: "), Utils.Align.LEFT, 10)));
        addComponent(new IntegerField(new Coords(2, 9), new StyledString("PLEASE TYPE IN SOME SHIT: "), Utils.Align.LEFT, 1, 1, 100));
        addComponent(new IntegerField(new Coords(2, 10), new StyledString("PLEASE TYPE IN SOME SECOND SHIT: "), Utils.Align.LEFT, 1, 1, 10));

        addComponent(new ColorPicker(new Coords(7, 12), game.getAvailableColors(), Utils.Align.LEFT));

        addComponent(new Button(new Coords(2, 13), new StyledString("ENTER TO VALIDATE"), Utils.Align.LEFT, () -> skip = true, false, Pair.of(13, "ENTER : VALIDATE")));
        addComponent(new Button(new Coords(2, 14), new StyledString("PRESS 'V' TO VALIDATE"), Utils.Align.LEFT, () -> System.exit(1), true, Pair.of((int) 'v', "V : VALIDATE")));

    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        Utils.clearTerm();
        renderer.render(this);

        Utils.writeAt(0, this.getFocusedWidget().getCoords().getY(), ">");

        while (!skip) {
            char key = (char) input.getInput();

            keyPressed(key);
            display(input, renderer);
        }

    }
}
