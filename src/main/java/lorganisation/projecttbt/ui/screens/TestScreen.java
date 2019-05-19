package lorganisation.projecttbt.ui.screens;

import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.ui.*;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

public class TestScreen extends Screen {

    private boolean skip = false;

    public TestScreen(Game game) {

        super();

        //addComponent(new InvisibleButton(() -> this.skip = true, Pair.of(13, "ENTER : SKIP this screen")));
        addComponent(new Label(new Coords(0, 5),
                               new StyledString("PRESS ENTER TO PASS"),
                               Utils.Align.CENTER));

        setFocused(addComponent(new TextField(
            new Coords(2, 8),
            new StyledString("PLEASE TYPE IN SOME SHIT: "),
            Utils.Align.LEFT,
            10)));

        addComponent(new IntegerField(new Coords(2, 9),
                                      new StyledString("PLEASE TYPE IN SOME SHIT: "),
                                      Utils.Align.LEFT,
                                      1,
                                      1,
                                      100));
        addComponent(new IntegerField(new Coords(2, 10),
                                      new StyledString("PLEASE TYPE IN SOME SECOND SHIT: "),
                                      Utils.Align.LEFT,
                                      1,
                                      1,
                                      10));

        addComponent(new ColorPicker(new Coords(7, 12),
                                     game.getAvailableColors(),
                                     Utils.Align.LEFT));

        addComponent(new Button(new Coords(2, 13),
                                new StyledString("ENTER TO VALIDATE"),
                                Utils.Align.LEFT,
                                () -> skip = true,
                                false, //FIXME
                                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0)));

        addComponent(new Button(new Coords(2, 14),
                                new StyledString("PRESS 'V' TO VALIDATE"),
                                Utils.Align.LEFT,
                                () -> System.exit(1), true, // FIXME
                                KeyStroke.getKeyStroke('v')));

    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        Utils.clearTerm();
        renderer.render(this);

        Utils.writeAt(0, this.getFocusedWidget().getCoords().getY(), ">");


        KeyStroke key = input.readKey();
        keyPressed(key);

        if (!skip)
            display(input, renderer);

    }
}
