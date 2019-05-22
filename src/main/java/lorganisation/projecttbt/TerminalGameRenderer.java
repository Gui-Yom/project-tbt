package lorganisation.projecttbt;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.ui.InvisibleButton;
import lorganisation.projecttbt.ui.Screen;
import lorganisation.projecttbt.ui.Widget;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.TerminalUtils;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;

public class TerminalGameRenderer {

    private Terminal terminal;

    public TerminalGameRenderer(Terminal terminal) {

        super();
        this.terminal = terminal;
    }

    public Size getSize() {

        return terminal.getSize();
    }

    public void render(Game g) {

        render(g, 0, 0, Utils.Align.LEFT);
    }

    public void render(Game g, int x, int y, Utils.Align align) {

        TerminalUtils.clearTerm();

        Coords coords = TerminalUtils.coordinatesOfAlignedObject(y, x, g.getMap().getWidth(), align, terminal.getWidth());

        TerminalUtils.writeAt(coords.getX(), coords.getY(), g.getMap().visual().replaceAll("\n", "\n" + Anscapes.moveRight(coords.getX())));

        for (AbstractPlayer player : g.getPlayers())
            for (Character character : player.getCharacters())
                TerminalUtils.writeAt(coords.getX() + character.getX(), coords.getY() + character.getY(), player.getColor().fg() + character.getIcon());
    }

    public void render(Screen screen) {

        Size terminalSize = terminal.getSize();

        for (Widget component : screen.getComponents())
            if (component.isVisible()) renderComponent(component);

        StringBuilder line = new StringBuilder();
        for (int i = 0; i < terminalSize.getColumns(); i++)
            line.append(' ');


        StringBuilder controls = new StringBuilder("| TAB : Switch between menus | ");
        //if (screen.getFocusedWidget() != null && screen.getFocusedWidget().getControls() != null)
        for (Widget widget : screen.getComponents())
            if (((!widget.isFocusable() && widget.isEnabled()) || widget == screen.getFocusedWidget()) && widget.getControls() != null) {

                if ((widget instanceof InvisibleButton && !widget.isEnabled())) {continue;}

                if (!widget.getDescription().equals("")) {controls.append(widget.getDescription()).append(" | ");}
            }

        int lineCount = (int) Math.ceil((double) controls.length() / terminalSize.getColumns());

        // same col because cursorPos(y, x) starts at (1, 1) whereas formattedLine(y, x) starts at (0, 0)
        System.out.print(Anscapes.cursorPos(terminalSize.getRows() - lineCount, 0) + Anscapes.Colors.WHITE_BRIGHT.bg() + line.toString() + Anscapes.RESET);
        System.out.print(TerminalUtils.formattedLine(terminalSize.getRows() - lineCount, 0, new StyledString(controls.toString()), Utils.Align.LEFT, terminalSize.getColumns()));
    }

    public void renderComponent(Widget component) {

        System.out.print(component.paint(terminal));
    }

    public Terminal getTerminal() {

        return terminal;
    }

    public int getCols() {

        return terminal.getWidth();
    }

    public int getRows() {

        return terminal.getHeight();
    }

    public Coords getCenter() {

        Size termSize = getSize();
        return new Coords(termSize.getColumns() / 2, termSize.getRows() / 2);
    }
}
