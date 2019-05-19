package lorganisation.projecttbt;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.ui.InvisibleButton;
import lorganisation.projecttbt.ui.Screen;
import lorganisation.projecttbt.ui.Widget;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;

public class TerminalGameRenderer {

    private Terminal terminal;

    public TerminalGameRenderer(Terminal terminal) {

        super();
        this.terminal = terminal;
    }

    public void render(Game g) {

        Utils.clearTerm();

        Utils.writeAt(0, 0, g.getMap().visual());

        for (AbstractPlayer player : g.getPlayers())
            for (Character character : player.getCharacters())
                Utils.writeAt(character.getX(), character.getY(), player.getColor().fg() + character.getIcon());
    }

    public void render(Screen screen) {

        Size terminalSize = terminal.getSize();

        for (Widget component : screen.getComponents())
            if (component.isVisible()) renderComponent(component);

        StringBuilder line = new StringBuilder();
        for (int i = 0; i < terminalSize.getColumns(); i++)
            line.append(' ');
        System.out.print(Anscapes.cursorPos(terminalSize.getRows() - 2, 0) + Anscapes.Colors.WHITE_BRIGHT.bg() + line.toString() + Anscapes.RESET);

        StringBuilder controls = new StringBuilder("| TAB : Switch between menus | ");
        //if (screen.getFocusedWidget() != null && screen.getFocusedWidget().getControls() != null)
        for (Widget widget : screen.getComponents())
            if (((!widget.isFocusable() && widget.isEnabled()) || widget == screen.getFocusedWidget()) && widget.getControls() != null) {
                if ((widget instanceof InvisibleButton && !widget.isEnabled()))
                    continue;
                // FIXME
                controls.append(widget.getDescription()).append(" | ");
            }

        System.out.print(Utils.formattedLine(terminalSize.getRows() - 2, 0, new StyledString(controls.toString()), Utils.Align.LEFT, terminalSize.getColumns()));
    }

    public void renderComponent(Widget component) {

        System.out.print(component.paint(terminal));
    }
}
