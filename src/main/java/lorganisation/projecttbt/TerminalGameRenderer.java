package lorganisation.projecttbt;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.ui.screen.Screen;
import lorganisation.projecttbt.ui.widget.InvisibleButton;
import lorganisation.projecttbt.ui.widget.Widget;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.TerminalUtils;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;

public class TerminalGameRenderer {

    // Terminal utilisé pour afficher les écrans et composants
    private Terminal terminal;

    public TerminalGameRenderer(Terminal terminal) {

        super();
        this.terminal = terminal;
    }

    /**
     * Get terminal size
     *
     * @return Size of the terminal
     */
    public Size getSize() {

        return terminal.getSize();
    }

    /**
     * Render and display a Game at (0,0) aligned to the left
     *
     * @param g game to render
     */
    public void render(Game g) {

        render(g, 0, 0, Utils.Align.LEFT);
    }


    /**
     * Render and display a Game on the terminal
     *
     * @param g     game to display
     * @param x     offset from LEFT / RIGHT / CENTER
     * @param y     distance from top
     * @param align alignment
     */
    public void render(Game g, int x, int y, Utils.Align align) {

        TerminalUtils.clearTerm();

        Coords coords = TerminalUtils.coordinatesOfAlignedObject(y, x, g.getMap().getWidth(), align, terminal.getWidth());

        TerminalUtils.writeAt(coords.getX(), coords.getY(), g.getMap().visual().replaceAll("\n", "\n" + Anscapes.moveRight(coords.getX())));

        for (AbstractPlayer player : g.getPlayers())
            for (Character character : player.getCharacters())
                TerminalUtils.writeAt(coords.getX() + character.getPos().getX(),
                                      coords.getY() + character.getPos().getY(),
                                      player.getColor().fg() + character.getIcon());
    }


    /**
     * Render and display a screen on the terminal
     *
     * @param screen to render and display
     */
    public void render(Screen screen) {

        Size terminalSize = terminal.getSize();

        for (Widget component : screen.getComponents())
            if (component.isVisible())
                renderComponent(component);

        StringBuilder controls = new StringBuilder("Change focus: TAB");
        //if (screen.getFocusedWidget() != null && screen.getFocusedWidget().getControls() != null)
        for (Widget widget : screen.getComponents())
            if (((!widget.isFocusable() && widget.isEnabled()) || widget == screen.getFocusedWidget()) && widget.getControls() != null) {

                if ((widget instanceof InvisibleButton && !widget.isEnabled())) {
                    continue;
                }

                if (!widget.getDescription().equals("")) {
                    controls.append(" | ").append(widget.getDescription());
                }
            }

        int ypos = terminalSize.getRows() - (int) Math.ceil((double) controls.length() / terminalSize.getColumns());

        // same col because cursorPos(y, x) starts at (1, 1) whereas formattedLine(y, x) starts at (0, 0)
        System.out.print(TerminalUtils.makeLine(new Coords(0, ypos),
                                                new Coords(terminalSize.getColumns(), ypos),
                                                ' ',
                                                Anscapes.Colors.WHITE.bg()));
        System.out.print(TerminalUtils.formattedLine(ypos, 0, new StyledString(controls.toString()), Utils.Align.LEFT, terminalSize.getColumns()));
    }

    /**
     * Uses component's paint method to render it
     *
     * @param component to render
     */
    private void renderComponent(Widget component) {

        System.out.print(component.paint(terminal));
    }

    /**
     * Get terminal
     *
     * @return the current terminal
     */
    public Terminal getTerminal() {

        return terminal;
    }

    /**
     * Get terminal width
     *
     * @return terminal width
     */
    public int getCols() {

        return terminal.getWidth();
    }

    /**
     * Get terminal height
     *
     * @return terminal height
     */
    public int getRows() {

        return terminal.getHeight();
    }

    /**
     * Get coordinates of center of the screen
     *
     * @return coods of center of the screen
     */
    public Coords getCenter() {

        Size termSize = getSize();
        return new Coords(termSize.getColumns() / 2, termSize.getRows() / 2);
    }
}
