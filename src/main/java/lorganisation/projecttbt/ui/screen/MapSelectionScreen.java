package lorganisation.projecttbt.ui.screen;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.AssetsManager;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.map.LevelMap;
import lorganisation.projecttbt.ui.widget.Button;
import lorganisation.projecttbt.ui.widget.Label;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.TerminalUtils;
import lorganisation.projecttbt.utils.Utils;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

public class MapSelectionScreen extends Screen {

    private boolean skip = false;
    private Game game;

    public MapSelectionScreen(Game game) {

        super(game.getInput().getTerminal());
        this.game = game;

        addComponent(new Label(new Coords(0, 2), new StyledString("Available maps"), Utils.Align.CENTER));

        int i = 0;
        for (String e : AssetsManager.gameMapNames()) { //max 10 maps pr l'instant
            Button btn = new Button(new Coords(0, 5 + i), new StyledString(e.toUpperCase()), Utils.Align.CENTER,
                                    () -> {
                                        game.setMap(LevelMap.load(e + ".map"));
                                        skip = true;
                                    }
                , true, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
            addComponent(btn);
            i++;
        }

        setFocused(1);
    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        while (!skip) {

            TerminalUtils.clearTerm();

            Button mapButton = (Button) getFocusedWidget();

            //Adding Focused Format
            mapButton.getText().modifiers().put(0, "> " + Anscapes.Colors.RED.fg());
            mapButton.getText().modifiers().put(mapButton.getText().length(), Anscapes.RESET + " <");
            mapButton.getCoords().setX(mapButton.getCoords().getX() - 2); // must change Coords or else doesn't print at right place (use 2 bc "> ".length() == 2)

            game.getRenderer().render(this);

            //Removing Focused Format
            mapButton.getCoords().setX(mapButton.getCoords().getX() + 2);
            mapButton.getText().modifiers().remove(0, "> " + Anscapes.Colors.RED.fg());
            mapButton.getText().modifiers().remove(mapButton.getText().length(), Anscapes.RESET + " <");

            keyPressed(game.getInput().readKey());
        }
    }
}
