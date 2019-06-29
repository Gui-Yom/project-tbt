package lorganisation.projecttbt.ui.screen;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.player.CharacterTemplate;
import lorganisation.projecttbt.player.Player;
import lorganisation.projecttbt.ui.widget.*;
import lorganisation.projecttbt.utils.*;
import org.jline.terminal.Size;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.util.List;

public class TestScreen extends Screen {

    private boolean skip = false;
    private int i = 0;
    private LoadingBar bar;

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

        /*addComponent(new Button(new Coords(2, 13),
                                new StyledString("ENTER TO VALIDATE"),
                                Utils.Align.LEFT,
                                () -> skip = true,
                                false,
                                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0)));*/

        addComponent(new InvisibleButton(() -> skip = true,
                                         KeyUtils.KEY_ENTER, "SKIP TEST MENU"));

        addComponent(new Button(new Coords(2, 14),
                                new StyledString("PRESS 'V' TO VALIDATE"),
                                Utils.Align.LEFT,
                                Game::shutdownGracefully,
                                true,
                                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0)));

        //public TextBoxWidget(Coords coords, Size size, Utils.Align align, Utils.Align textAlign, StyledString title, Anscapes.Colors borderColor, Anscapes.Colors backgroundColor, StyledString... lines) {
        StyledString[] lines = new StyledString[game.getPlayers().size()];
/*        int j = 0;
        for (AbstractPlayer p : game.getPlayers())
            lines[j++] = (new StyledString(p.getName(), Pair.of(0, p.getColor().fg())));*/

        addComponent(new TextBoxWidget(new Coords(3, 10),
                                       new Size(40, 10),
                                       Utils.Align.RIGHT,
                                       Utils.Align.LEFT,
                                       new StyledString("Titre boite"),
                                       Anscapes.Colors.BLUE,
                                       Anscapes.Colors.YELLOW_BRIGHT,
                                       new StyledString("Salut mon giga pote"),
                                       new StyledString("Ca va ou quoi")));

        addComponent(new ImageButtonWidget(new Coords(0, 8),
                                           new Size(16, 16),
                                           Utils.Align.CENTER, "assets/images/characters/mage.png",
                                           true,
                                           new StyledString("MAGE"),
                                           () -> TerminalUtils.writeAt(2, 2, Anscapes.Colors.RED.bg() + "image"),
                                           KeyStroke.getKeyStroke('a', 0)));

        addComponent(new HorizontalComboBoxWidget(new Coords(3, 18),
                                                  Utils.Align.LEFT,
                                                  new StyledString("first"),
                                                  new StyledString("second"),
                                                  new StyledString("last option", Pair.of(0, Anscapes.Colors.RED.fg()))
        ));

        bar = (LoadingBar) addComponent(new LoadingBar(new Coords(3, 19),
                                                       12, ' ', Anscapes.Colors.GREEN_BRIGHT.bg()));

        bar.setPercent(.01f);
    }

    @Override
    public void display(Game game) {

        while (!skip) {

            AbstractPlayer dummyPlayer = new Player("Dummy", Anscapes.Colors.BLUE);
            Character archer = new Character(CharacterTemplate.getCharacterTemplate("archer"), dummyPlayer);

            game.getRenderer().render(this);

            archer.setPos(new Coords(10, 25));
            List<Coords> reach = archer.getAttacks().current().getReachableTiles(archer);
            TerminalUtils.writeAt(10, 20, archer.getType() + ": " + reach.size());

            for (Coords tile : reach) {
                if (tile.getX() < 7)
                    TerminalUtils.writeAt(tile.getX(), tile.getY(), Anscapes.Colors.RED_BRIGHT.bg() + " " + Anscapes.RESET);
                else
                    TerminalUtils.writeAt(tile.getX(), tile.getY(), Anscapes.Colors.GREEN_BRIGHT.bg() + " " + Anscapes.RESET);
            }

            archer.getAttacks().current().areaRadius = 3;
            for (Coords hit : archer.getAttacks().current().getHitTiles(new Coords(12, 25))) {
                TerminalUtils.writeAt(hit.getX(), hit.getY(), Anscapes.Colors.YELLOW_BRIGHT.bg() + " " + Anscapes.RESET);
            }

            TerminalUtils.writeAt(archer.getPos().getX(), archer.getPos().getY(), archer.getIcon());


            TerminalUtils.writeAt(0, this.getFocusedWidget().getCoords().getY(), ">");
            TerminalUtils.writeAt(0, 0, (++i) + " focusedIndex -> " + this.components.getIndex());

            KeyStroke key = game.getInput().readKey();


            TerminalUtils.clearTerm();
            bar.setPercent(bar.getPercent() * -2);
            keyPressed(key);

            for (Widget w : getComponents())
                System.out.println((((!w.isFocusable() && w.isEnabled()) || w == getFocusedWidget()) && w.getControls() != null));

            TerminalUtils.writeAt(0, 2, Anscapes.Colors.RED_BRIGHT.fg() + game.getInput().getLastKey());
        }
    }
}
