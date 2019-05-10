package lorganisation.projecttbt.ui.screens;

import lorganisation.projecttbt.AssetsManager;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.map.LevelMap;
import lorganisation.projecttbt.ui.Button;
import lorganisation.projecttbt.ui.InvisibleButton;
import lorganisation.projecttbt.ui.Label;
import lorganisation.projecttbt.ui.Screen;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.Pair;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;

public class MapSelectionScreen extends Screen {

    private final Game associatedGame;

    public MapSelectionScreen(Game game) {

        super();

        this.associatedGame = game;

        addComponent(new Label(new Coords(0, 2), new StyledString("Available maps"), Utils.Align.CENTER));


        int i = 0;
        for (String e : AssetsManager.gameMapNames()) { //max 10 maps pr l'instant
            addComponent(new Button(new Coords(0, 5 + i), new StyledString(e), Utils.Align.CENTER,
                                    () -> {
                game.setMap(LevelMap.load(e + ".map"));
            }
                , true, Pair.of(13, "ENTER : Select map")));
            i++;
        }

        setFocused(1);
    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        renderer.render(this);

        Coords focusedCoords = getFocusedWidget().getCoords();
         Utils.writeAt(focusedCoords.getY(), focusedCoords.getX() - 2, ">");
         Utils.writeAt(focusedCoords.getY(), focusedCoords.getX() + ((Button)getFocusedWidget()).getText().text().length() + 2, "<");

        keyPressed((char) input.getInput());

        if (associatedGame.getMap() != null) // skip screen when a map has been successfully loaded
            display(input, renderer);
    }
}
