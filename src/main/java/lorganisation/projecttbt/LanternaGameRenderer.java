package lorganisation.projecttbt;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.ansi.ANSITerminal;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.ui.Label;
import lorganisation.projecttbt.ui.Widget;
import lorganisation.projecttbt.utils.Pair;
import lorganisation.projecttbt.utils.Utils;

import java.io.IOException;

public class LanternaGameRenderer {

    private Pair<lorganisation.projecttbt.ui.Screen, Screen> screens;
    private ANSITerminal lTerminal;

    public LanternaGameRenderer(ANSITerminal terminal) {

        super();
        this.lTerminal = terminal;
    }

    public void render(Game g) {

        Utils.clearTerm();

        Utils.writeAt(0, 0, g.getMap().visual());

        for (AbstractPlayer player : g.getPlayers())
            for (Character character : player.getCharacters())
                Utils.writeAt(character.getX(), character.getY(), player.getColor().fg() + character.getIcon());
    }

    public void render(lorganisation.projecttbt.ui.Screen screen) {

        Screen lanternaScreen = screens.getV();

        // Si l'écran on demande d'afficher un autre écran on met à jour la Pair, on close l'ancien et ouvre le nouvel écran
        if (this.screens.getU() != screen) {
            try {
                lanternaScreen.stopScreen();

                lanternaScreen = new TerminalScreen(lTerminal);

                this.screens = Pair.of(screen, lanternaScreen);

                lanternaScreen.startScreen();

                lanternaScreen.setCursorPosition(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        for (Widget component : screen.getComponents())
            if (component.isVisible()) renderComponent(component);


        // Build controls
        /*StringBuilder controls = new StringBuilder("| TAB : Switch between menus | ");
        for (Widget widget : screen.getComponents())
            if (((!widget.isFocusable() && widget.isActivated()) || widget == screen.getFocusedWidget()) && widget.getControls() != null) {
                if ((widget instanceof InvisibleButton && !widget.isActivated())) continue;
                for (String description : widget.getControls().values())
                    controls.append(description).append(" | ");
            }
        */

        //Display controls
    }

    public void renderComponent(Widget component) {

        Screen screen = screens.getV();

        if (component instanceof Label) {
            String text = ((Label) component).getText();
            TerminalPosition labelBoxTopLeft = new TerminalPosition(component.getCoords().getX(), component.getCoords().getY());
            TerminalSize labelBoxSize = new TerminalSize(text.length() + 2, 1);
            TerminalPosition labelBoxTopRightCorner = labelBoxTopLeft.withRelativeColumn(labelBoxSize.getColumns() - 1);
            TextGraphics textGraphics = screen.newTextGraphics();

            //This isn't really needed as we are overwriting everything below anyway, but just for demonstrative purpose
            textGraphics.fillRectangle(labelBoxTopLeft, labelBoxSize, ' ');
            textGraphics.putString(labelBoxTopLeft.withRelative(1, 1), text);
        }
    }
}
