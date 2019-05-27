package lorganisation.projecttbt;

import com.limelion.anscapes.ColorMode;
import lorganisation.projecttbt.map.LevelMap;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.ui.screen.*;
import lorganisation.projecttbt.utils.CyclicList;
import lorganisation.projecttbt.utils.TerminalUtils;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.impl.DumbTerminal;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.limelion.anscapes.Anscapes.Colors;

/**
 * La classe principale du jeu.
 *
 * @see Game#main(String[])
 */
public class Game {

    /**
     * La liste des joueurs.
     */
    private CyclicList<AbstractPlayer> players;

    /**
     * La map du jeu
     */
    private LevelMap map;

    /**
     * Gère le rendu à l'écran
     */
    private TerminalGameRenderer renderer;

    /**
     * Gère les entrées utilisateurs
     */
    private TerminalGameInput input;

    private List<Colors> availableColors;

    public Game(TerminalGameInput input, TerminalGameRenderer renderer) {

        this.renderer = renderer;
        this.input = input;
        players = new CyclicList<>();

        availableColors = Utils.arrayToList(Colors.values());
        // on enleve les couleurs génantes
        availableColors.remove(Colors.BLACK);
        availableColors.remove(Colors.BLACK_BRIGHT);
        availableColors.remove(Colors.WHITE_BRIGHT);
        availableColors.remove(Colors.WHITE);
    }


    /**
     * Méthode exécutée au lancement, met en place un Terminal adapté, puis gère le lancement de chaque écran. Structure
     * principale du fonctionnement du jeu.
     *
     * @param args arguments de lancement
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        // Arguments de ligne de commande
        // On les parse à la main
        if (args != null && args.length == 1 && args[0].equals("devenv")) {

            IntegratedDevenv.devenv();
            System.exit(0);
        }

        if (args != null && args.length == 4 && args[0].equals("convert")) {

            IntegratedDevenv.convert(ColorMode.valueOf(args[1]), Integer.parseInt(args[2]), new File(args[3]));
            System.exit(0);
        }

        // L'objet Terminal qui nous permet de faire à peu près tout
        Terminal term = TerminalBuilder.builder()
                                       .encoding(StandardCharsets.UTF_8)
                                       .jansi(true)
                                       .system(true)
                                       .name("Project: TBT")
                                       .nativeSignals(true)
                                       //.type("windows-vtp")
                                       .signalHandler(Game::handleSignal)
                                       .build();

        // On vérifie que le terminal a correctement été instancié
        // ex: problème de compatibilité de l'OS
        if (term instanceof DumbTerminal) {
            System.err.println("This terminal isn't supported ! Aborting.");
            System.exit(-1);
        }

        term.enterRawMode();

        // On passe en mode privé
        TerminalUtils.enterPrivateMode();

        // L'objet jeu qui fait tout tenir en place
        Game game = new Game(new TerminalGameInput(term), new TerminalGameRenderer(term));

        // On demande à l'utilisateur de redimensionner sa fenêtre
        // Sur windows, on ne peut pas la redimensionner depuis le programme
        TerminalUtils.askForResize(term, new Size(130, 40));

        new TestScreen(game).display(game.input, game.renderer);

        // Menu principal du jeu
        // L'utilisateur peut choisir de démarrer une partie ou d'utiliser les outils de développement intégrés
        game.mainMenu();

        // L'utilisateur peut choisir la map
        game.mapSelection();

        // Les joueurs choisissent leurs personnages et leurs couleurs
        game.lobby();

        // La partie commence !
        // Elle continue jusqua ce que mort s'ensuive
        game.start();

        // END / Cleanup
        TerminalUtils.clearTerm();
        TerminalUtils.exitPrivateMode();

        term.close();
    }

    /**
     * Cette méthode gère les signaux.
     *
     * @param sig le signal à gérer
     *
     * @see <a href="https://en.wikipedia.org/wiki/Signal_(IPC)">Signaux, systèmes POSIX</a>
     **/
    public static void handleSignal(Terminal.Signal sig) {

        switch (sig) {
            case INT: // Interrupt, usually ctrl+c
                System.out.println("Received SIGINT !");
                TerminalUtils.clearTerm();
                TerminalUtils.exitPrivateMode();
                System.exit(0);
                break;
            case QUIT:
                break;
            case TSTP:
                break;
            case CONT:
                break;
            case INFO:
                break;
            case WINCH: // Window resize
                // System.out.println("Received SIGWINCH !");
                break;
        }
    }

    /**
     * Ajoute un joueur dans le jeu.
     *
     * @param player le joueur à ajouter
     */
    public void addPlayer(AbstractPlayer player) {

        players.add(player);

        //noinspection SuspiciousMethodCalls
        availableColors.remove(player.getColor());
    }

    /**
     * Liste cyclique des joueurs de la partie
     *
     * @return la liste des joueurs
     */
    public CyclicList<AbstractPlayer> getPlayers() {

        return players;
    }

    /**
     * Renvoie la map actuelle
     *
     * @return la map du jeu
     */
    public LevelMap getMap() {

        return map;
    }

    /**
     * Définit la map du jeu
     *
     * @param map la map à utiliser
     */
    public void setMap(LevelMap map) {

        this.map = map;
    }


    /**
     * Renvoie l'InputManager utilisé par le jeu.
     *
     * @return
     */
    public TerminalGameInput getInput() {

        return input;
    }

    /**
     * Renvoie le renderer utilisé par le jeu.
     *
     * @return
     */
    public TerminalGameRenderer getRenderer() {

        return renderer;
    }

    /**
     * Menu principal du jeu
     * <p>
     * L'utilisateur peut choisir de lancer une partie, modifier les paramètres ou utiliser les outils de développment
     * intégrés.
     */
    public void mainMenu() {

        new MainScreen(input.getTerminal()).display(input, renderer);
    }

    /**
     * Menu de sélection de map
     */
    public void mapSelection() {

        new MapSelectionScreen(this).display(input, renderer);
    }

    /**
     * Les joueurs peuvent choisir leur personnages et ajouter des IA à la partie.
     */
    public void lobby() {

        LobbyScreen lobbyScreen = new LobbyScreen(this);
        lobbyScreen.display(input, renderer);


        CharacterSelectionScreen characterSelectionScreen = new CharacterSelectionScreen(this, lobbyScreen.getMaxCharacterCount());
        characterSelectionScreen.display(input, renderer);
    }

    /**
     * Renvoie la liste des couleurs disponibles pour les joueurs
     */
    public List<Colors> getAvailableColors() {

        return this.availableColors;
    }

    /**
     * Donne le nombre de joueurs non-bot dans la partie
     */
    public int getRealPlayerCount() {

        int count = 0;

        for (AbstractPlayer player : getPlayers())
            if (!player.isBot())
                count++;

        return count;
    }

    // Keep it until as model until CharacterSelectionScreen is made

    /**
     * Donne le nombre de BOTs non-bot dans la partie
     */
    public int getBotCount() {

        return getPlayers().size() - getRealPlayerCount();
    }

    /**
     * Lance la partie. La partie continue à l'infinie pour l'instant.
     */
    public void start() {

        /*
        GameScreen gameScreen = new GameScreen(this);
        gameScreen.display(input, renderer);


         */

    }
}
