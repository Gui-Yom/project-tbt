package lorganisation.projecttbt.ui.screens;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import lorganisation.projecttbt.AssetsManager;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.map.LevelMap;
import lorganisation.projecttbt.utils.*;

import java.util.Arrays;

public class MapSelectionMenuLanterna extends BasicWindow {

    private String returnStatus = "none";
    private Game associatedGame;

    public MapSelectionMenuLanterna(Screen screen, Game game) {

        super("TBT - Map Selection");

        this.associatedGame = game;

        TerminalSize size = screen.getTerminalSize();

        setHints(Arrays.asList(Hint.FULL_SCREEN, Hint.NO_POST_RENDERING, Hint.NO_DECORATIONS, Hint.CENTERED));
        Panel panel = new Panel();
        panel.setLayoutManager(new AbsoluteLayout());

        panel.addComponent(MenuUtils.getMainTitle(size, 1));

        Label subTitle = MenuUtils.makeSubtitle(size, 6, "Available Maps");
        panel.addComponent(subTitle);

        int i=0;
        for (String e : AssetsManager.gameMapNames()) { //max 10 maps pr l'instant

            Button mapButton = MenuUtils.makeButton(size, 0,10 + i, e.toUpperCase(), Utils.Align.CENTER, () -> game.setMap(LevelMap.load(e + ".map")));

            panel.addComponent(mapButton);

            i++;
        }

        setComponent(panel);
    }
}
