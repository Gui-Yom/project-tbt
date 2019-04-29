package lorganisation.projectrpg;

import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.AnsiColors;
import lorganisation.projectrpg.player.Character;
import org.jline.terminal.Terminal;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class TerminalGameRenderer extends GameRenderer {

    private Terminal terminal;

    public TerminalGameRenderer(Terminal terminal) {

        super();
        this.terminal = terminal;
    }

    @Override
    public void render(Game g) {

        Character character = g.getPlayers().get(0).getCharacters().get(0);

        Game.clearTerm();

        write(0, 0, g.getMap().visual());
        write(0, g.getMap().getHeight()+1, "x: "+character.getX()+", y: "+character.getY());

        write(character.getX(), character.getY(), g.getPlayers().get(0).getColor().code() + character.getIcon());
    }

    private void write(int x, int y, String s) {
        // La première case sur un terminal est (1,1)
        // On décale pour pouvoir mettre (0,0)
        // On inverse x et y car le terminal fait ch*** (il inverse les 2)
        System.out.print(Anscapes.cursorPos(y + 1, x + 1));
        System.out.print(s + Anscapes.RESET);
        System.out.print(Anscapes.cursorPos(y + 1, x + 1)); // On refout le curseur là où on a écrit pour avoir le blinking sur cette case et pas celle d'après
    }
}
