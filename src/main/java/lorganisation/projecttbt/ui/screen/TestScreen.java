package lorganisation.projecttbt.ui.screen;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.ui.widget.*;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.TerminalUtils;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Size;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

public class TestScreen extends Screen {

    private boolean skip = false;
    private int i = 0;

    public TestScreen(Game game) {

        super(game.getInput().getTerminal());

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
                                false,
                                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0)));

        addComponent(new Button(new Coords(2, 14),
                                new StyledString("PRESS 'V' TO VALIDATE"),
                                Utils.Align.LEFT,
                                () -> System.exit(1),
                                true,
                                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0)));

        //public TextBoxWidget(Coords coords, Size size, Utils.Align align, Utils.Align textAlign, StyledString title, Anscapes.Colors borderColor, Anscapes.Colors backgroundColor, StyledString... lines) {
        StyledString[] lines = new StyledString[game.getPlayers().size()];
/*        int j = 0;
        for (AbstractPlayer p : game.getPlayers())
            lines[j++] = (new StyledString(p.getName(), Pair.of(0, p.getColor().fg())));*/

        addComponent(new TextBoxWidget(new Coords(3, 10), new Size(40, 10), Utils.Align.RIGHT, Utils.Align.LEFT, new StyledString("Titre boite"), Anscapes.Colors.BLUE, Anscapes.Colors.YELLOW_BRIGHT, new StyledString("Salut mon giga pote"), new StyledString("Ca va ou quoi")));

        addComponent(new ImageButtonWidget(new Coords(0, 8),
                                           new Size(16,16),
                                           Utils.Align.CENTER, "assets/characters/sprites/mage.png",
                                           true,
                                           new StyledString("MAGE"),
                                           () -> TerminalUtils.writeAt(2, 2, Anscapes.Colors.RED.bg()+"image"),
                                           KeyStroke.getKeyStroke('a', 0)));
    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        while (!skip) {

            renderer.render(this);


            TerminalUtils.writeAt(0, this.getFocusedWidget().getCoords().getY(), ">");
            TerminalUtils.writeAt(0, 0, (++i) + " focusedIndex -> " + this.components.getIndex());

            KeyStroke key = input.readKey();

            TerminalUtils.clearTerm();

            keyPressed(key);

            TerminalUtils.writeAt(0, 2, Anscapes.Colors.RED_BRIGHT.fg() + input.getLastKey());
        }
    }
}
