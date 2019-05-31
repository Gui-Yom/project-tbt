package lorganisation.projecttbt.ui.screen;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.ui.widget.InvisibleButton;
import lorganisation.projecttbt.ui.widget.Label;
import lorganisation.projecttbt.ui.widget.PlayerListWidget;
import lorganisation.projecttbt.ui.widget.TextBoxWidget;
import lorganisation.projecttbt.utils.*;
import org.jline.terminal.Size;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class GameScreen extends Screen {

    private final TextBoxWidget statusUpdate;
    private final PlayerListWidget playerListW;
    private boolean skip = false;
    private Label statusLabel, subStatusLabel;
    private Game game;

    public GameScreen(Game game) {

        super(game.getInput().getTerminal());

        this.game = game;

        Size termSize = game.getInput().getTerminal().getSize();

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

        statusUpdate = new TextBoxWidget(new Coords(0, 0),
                                         new Size(termSize.getColumns() / 7, termSize.getRows() - 5),
                                         Utils.Align.RIGHT,
                                         Utils.Align.LEFT,
                                         new StyledString("STATUS UPDATE"),
                                         Anscapes.Colors.WHITE,
                                         Anscapes.Colors.BLACK);
        addComponent(statusUpdate);

        InvisibleButton quitBtn = new InvisibleButton(() -> {
            TerminalUtils.clearTerm();
            System.exit(0);
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        addComponent(quitBtn);

        playerListW = new PlayerListWidget(new Coords(0, 1),
                                           new Size(termSize.getColumns() / 7, termSize.getRows() - 2),
                                           Utils.Align.LEFT,
                                           Utils.Align.LEFT,
                                           new StyledString("Personnages"),
                                           Anscapes.Colors.WHITE,
                                           Anscapes.Colors.BLACK);
        addComponent(playerListW);
    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        AbstractPlayer currPlayer = game.getPlayers().current();

        int i = 0;
        for (Coords startPos : game.getMap().getStartPos())
            statusUpdate.addLine(new StyledString("startPos" + i++ + startPos.toString()));


        List<StyledString> updates = new ArrayList<>();
        for (AbstractPlayer player : game.getPlayers())
            for (Character character : player.getCharacters())
                updates.add(new StyledString(player.getName() + " " + character.getType() + " -> " + character.getPos()));
        statusUpdate.setText(updates);


        playerListW.updatePlayerList(game.getPlayers());
        playerListW.setSelected(game.getPlayers().indexOf(currPlayer));

        statusLabel.setText(new StyledString(" Tour de " + currPlayer.getName(), Pair.of(9, currPlayer.getColor().fg())));

        subStatusLabel.setText(new StyledString(currPlayer.getName() + " " + currPlayer.getStatus(), Pair.of(0, currPlayer.getColor().fg()), Pair.of(currPlayer.getName().length(), Anscapes.RESET)));

        renderer.render(game, 0, 10, Utils.Align.CENTER);
        renderer.render(this);
    }
}