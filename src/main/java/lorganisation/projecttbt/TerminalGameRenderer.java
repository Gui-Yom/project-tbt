package lorganisation.projecttbt;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.map.Tile;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.player.attack.Attack;
import lorganisation.projecttbt.ui.screen.Screen;
import lorganisation.projecttbt.ui.widget.InvisibleButton;
import lorganisation.projecttbt.ui.widget.Widget;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.TerminalUtils;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;

import java.util.List;

public class TerminalGameRenderer {

    /**
     * L'objet terminal utilisé pour écrire
     */
    private Terminal terminal;

    public TerminalGameRenderer(Terminal terminal) {

        super();
        this.terminal = terminal;
    }

    /**
     * Retourne la taille du terminal
     *
     * @return Size of the terminal
     */
    public Size getSize() {

        return terminal.getSize();
    }

    /**
     * Affiche la map du jeu à l'emplacement spécifié
     *
     * @param g     l'instance du jeu à afficher
     * @param x     offset selon x
     * @param y     offset selon
     * @param align alignement du composant
     */
    public void render(Game g, int x, int y, Utils.Align align) {

        Coords coords = TerminalUtils.coordinatesOfAlignedObject(y, x, g.getMap().getWidth(), align, terminal.getWidth());

        // On affiche la map
        TerminalUtils.writeAt(coords.getX(), coords.getY(), g.getMap().visual().replaceAll("\n", "\n" + Anscapes.moveRight(coords.getX())));

        // On affiche la distance d'attaque du personnage
        // Lorsque le personnage est en phase d'attaque
        AbstractPlayer currPlayingPlayer = g.getPlayers().current();
        if (currPlayingPlayer != null && currPlayingPlayer.getStatus() == AbstractPlayer.Status.CASTING_ATTACK) {

            Character currPlayingCharacter = g.getPlayers().current().getCharacters().current();

            Attack atk = currPlayingCharacter.getAttacks().current();
            List<Coords> toHighlight = atk.getReachableTiles(currPlayingCharacter);
            for (Coords tile : toHighlight)
                if (g.getMap().isInBounds(tile.getX(), tile.getY()))
                    TerminalUtils.writeAt(coords.getX() + tile.getX(), coords.getY() + tile.getY(), Anscapes.Colors.BLUE_BRIGHT.bg() + " ");

            for(Coords impact : atk.getHitTiles(currPlayingCharacter.getAimingAt())) {
                TerminalUtils.writeAt(coords.getX() + impact.getX(), coords.getY() + impact.getY(), Anscapes.Colors.BLUE.bg() + " ");
            }
        }

        for (AbstractPlayer player : g.getPlayers()) {

            // display characters (top layer)
            for (Character character : player.getCharacters())
                TerminalUtils.writeAt(coords.getX() + character.getPos().getX(),
                                      coords.getY() + character.getPos().getY(),
                                      player.getColor().fg() + character.getIcon());
        }

        // Une petite cible pour montrer où le joueur vise
        // FIXME make that if a character is on this tile we must just invert the color and not print an 'x'
        if (currPlayingPlayer != null && currPlayingPlayer.getStatus() == AbstractPlayer.Status.CASTING_ATTACK) {

            // character that player is currently playing with (if playing)
            Character currPlayingCharacter = currPlayingPlayer.getCharacters().current();

            Coords aim = currPlayingCharacter.getAimingAt();
            Tile aimedTile = g.getMap().getTileAt(aim.getX(), aim.getY());

            TerminalUtils.writeAt(coords.getX() + aim.getX(), coords.getY() + aim.getY(), Anscapes.Colors.RED_BRIGHT.fg() + Anscapes.Colors.BLUE.bg()  + "×");
        }
    }


    /**
     * Affiche un objet Screen
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
            if (((!widget.isFocusable() && widget.isEnabled()) || widget == screen.getFocusedWidget())) { //FIXME removed && widget.getControls() != null at the end

                if ((widget instanceof InvisibleButton && !widget.isEnabled())) {
                    continue;
                }

                if (!widget.getDescription().equals("")) {
                    controls.append(" | ").append(widget.getDescription());
                }
            }

        int yPos = terminalSize.getRows() - (int) Math.ceil((double) controls.length() / terminalSize.getColumns());

        // same col because cursorPos(y, x) starts at (1, 1) whereas formattedLine(y, x) starts at (0, 0)
        System.out.print(TerminalUtils.makeLine(new Coords(0, yPos),
                                                new Coords(terminalSize.getColumns(), yPos),
                                                ' ',
                                                Anscapes.Colors.WHITE.bg()));
        System.out.print(TerminalUtils.formattedLine(yPos, 0, new StyledString(controls.toString()), Utils.Align.LEFT, terminalSize.getColumns()));
    }

    /**
     * Dessine un composant
     *
     * @param component le composant à dessiner
     */
    private void renderComponent(Widget component) {

        System.out.print(component.paint(terminal));
    }

    /**
     * @return l'objet terminal associé
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
