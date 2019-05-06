package lorganisation.projecttbt.ui.screens;

import lorganisation.projecttbt.AssetsManager;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.map.LevelMap;
import lorganisation.projecttbt.ui.Button;
import lorganisation.projecttbt.ui.Label;
import lorganisation.projecttbt.ui.Screen;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;

import java.util.Map;

public class MapSelectionScreen extends Screen {

    public MapSelectionScreen(Game game) {

        super();
        addComponent(new Label(new Coords(0, 2), new StyledString("Available maps"), Utils.Align.CENTER));

        int i = 0;
        for (Map.Entry<String, String> e : AssetsManager.gameMaps().entrySet()) { //max 10 maps pr l'instant
            addComponent(new Label(new Coords(0, 5 + i), new StyledString("[" + i + "] " + e.getKey()), Utils.Align.CENTER));
            addComponent(new Button(String.valueOf(i).charAt(0),
                                    () -> {
                                        game.setMap(LevelMap.load(e.getValue()));
                                    }
            ));
            i++;
        }
    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        renderer.render(this);

        int inputValue;
        int mapIndex = 0;
        do {
            inputValue = input.getInput();
            try {
                mapIndex = Integer.parseInt(String.valueOf((char) inputValue));
            } catch (NumberFormatException ex) {/**/}
        } while (!(mapIndex >= 0 && mapIndex < AssetsManager.gameMapNames().size()));

        keyPressed((char) inputValue);
    }
}
