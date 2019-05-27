package lorganisation.projecttbt.ui.screen;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.Action;
import lorganisation.projecttbt.ui.widget.InvisibleButton;
import lorganisation.projecttbt.ui.widget.Label;
import lorganisation.projecttbt.utils.*;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

public class GameScreen extends Screen {

    private boolean skip = false;
    private Label statusLabel, subStatusLabel;
    private Game game;

    private AbstractPlayer current;

    public GameScreen(Game game) {

        super(game.getInput().getTerminal());

        this.game = game;

        Label lbl = new Label(new Coords(0, 2),
                              new StyledString("Project: TBT",
                                               Pair.of(0, Anscapes.Colors.RED_BRIGHT.fg()),
                                               Pair.of(9, Anscapes.Colors.YELLOW.fg()),
                                               Pair.of(10, Anscapes.Colors.BLUE.fg()),
                                               Pair.of(11, Anscapes.Colors.GREEN.fg())),
                              Utils.Align.CENTER);
        addComponent(lbl);

        statusLabel = new Label(new Coords(0, 4),
                                new StyledString(""),
                                Utils.Align.CENTER);
        addComponent(statusLabel);

        subStatusLabel = new Label(new Coords(0, 5),
                                   new StyledString(""),
                                   Utils.Align.CENTER);
        addComponent(subStatusLabel);

        /*
        InvisibleButton moveLeftButton = new InvisibleButton(() -> (current.play(game, Action.MOVE_LEFT)), KeyStroke.getKeyStroke('q', 0));
        InvisibleButton moveRightButton = new InvisibleButton(() -> (current.play(game, Action.MOVE_RIGHT)), KeyStroke.getKeyStroke('d', 0));
        InvisibleButton moveUpButton = new InvisibleButton(() -> (current.play(game, Action.MOVE_UP)), KeyStroke.getKeyStroke('z', 0));
        InvisibleButton moveDownButton = new InvisibleButton(() -> (current.play(game, Action.MOVE_DOWN)), KeyStroke.getKeyStroke('s', 0));


         */
        InvisibleButton actionButton = new InvisibleButton(() -> {}, KeyUtils.KEY_SPACE_BAR);

        InvisibleButton iBtn = new InvisibleButton(() -> {
            TerminalUtils.clearTerm();
            System.exit(0);
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        addComponent(iBtn);
    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        while (!skip) {

            renderer.render(game, 0, 10, Utils.Align.CENTER);
            renderer.render(this);

            current = game.getPlayers().next();

            statusLabel.setText("Tour de " + current.getName());
            statusLabel.getStyledText().modifiers().remove(7);
            statusLabel.getStyledText().modifiers().put(7, current.getColor().fg());

            KeyStroke key = input.readKey();


            //TODO: make the player play.
            Action action = current.play(game);
            if (action == Action.DO_NOTHING)
                keyPressed(key);
        }

    }
}
