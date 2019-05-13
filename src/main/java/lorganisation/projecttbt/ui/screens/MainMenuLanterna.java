package lorganisation.projecttbt.ui.screens;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.AbsoluteLayout;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.screen.Screen;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.utils.MenuUtils;

import java.io.IOException;
import java.util.Arrays;

public class MainMenuLanterna extends BasicWindow {

    private String returnStatus = "none";
    private Game associatedGame;

    public MainMenuLanterna(Screen screen, Game game) {

        super("TBT - Welcome");

        this.associatedGame = game;

        TerminalSize size = screen.getTerminalSize();

        setHints(Arrays.asList(Hint.NO_POST_RENDERING, Hint.NO_DECORATIONS, Hint.CENTERED, Hint.FULL_SCREEN));
        Panel panel = new Panel();
        panel.setLayoutManager(new AbsoluteLayout());

        panel.addComponent(MenuUtils.getMainTitle(size, 10));

        Button play = new Button("Jouer", () -> {
            returnStatus = "play";
            close();
        });
        play.setPosition(new TerminalPosition((size.getColumns() - play.getLabel().length() - 2) / 2, 12));
        play.setSize(new TerminalSize(play.getLabel().length() + 2, 1));
        panel.addComponent(play);

        Button quit = new Button("Quitter", () -> {
            close();
            try {
                screen.stopScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });
        quit.setPosition(new TerminalPosition((size.getColumns() - quit.getLabel().length() - 2) / 2, 13));
        quit.setSize(new TerminalSize(quit.getLabel().length() + 2, 1));
        panel.addComponent(quit);

        Button devenv = new Button("Espace créateur", () -> {
            returnStatus = "devenv";
            close();
        });
        devenv.setPosition(new TerminalPosition((size.getColumns() - devenv.getLabel().length() - 2) / 2, 14));
        devenv.setSize(new TerminalSize(devenv.getLabel().length() + 2, 1));
        panel.addComponent(devenv);

        setComponent(panel);
    }

    public String getReturnStatus() {

        return returnStatus;
    }
}