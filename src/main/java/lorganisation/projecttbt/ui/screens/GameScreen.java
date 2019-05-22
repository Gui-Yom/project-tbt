package lorganisation.projecttbt.ui.screens;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.Action;
import lorganisation.projecttbt.ui.InvisibleButton;
import lorganisation.projecttbt.ui.Label;
import lorganisation.projecttbt.ui.Screen;
import lorganisation.projecttbt.utils.*;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

public class GameScreen extends Screen {

    private boolean skip = false;
    private Label statusLabel;
    private Game game;

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

            AbstractPlayer player = game.getPlayers().next();

            statusLabel.setText("Tour de " + player.getColor().fg() + player.getName());

            KeyStroke key = input.readKey();

            //TODO: make the player play.
            Action action = player.play(game);
            if (action == Action.DO_NOTHING)
                keyPressed(key);
        }

    }
}
