package lorganisation.projecttbt;

import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.ui.Menu;
import lorganisation.projecttbt.ui.MenuComponent;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.Utils;
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

    public void render(Menu menu) {

        for (MenuComponent component : menu.getComponents()) {

            Coords coords = component.getCoords();

            if (coords != null)
                System.out.print(component.render(terminal));
        }
    }
}
