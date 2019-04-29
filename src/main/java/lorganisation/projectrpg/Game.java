package lorganisation.projectrpg;

import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.AnsiColors;
import com.limelion.anscapes.ImgConverter;
import lorganisation.projectrpg.map.LevelMap;
import lorganisation.projectrpg.player.AbstractPlayer;
import lorganisation.projectrpg.player.Character;
import lorganisation.projectrpg.player.Player;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.impl.DumbTerminal;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * La classe principale du jeu.
 *
 * @see Game#main(String[])
 */
public class Game {

    /**
     * La liste des joueurs.
     */
    private List<AbstractPlayer> players;
    /**
     * La map du jeu
     */
    private LevelMap map;
    /**
     * Gère le rendu à l'écran
     */
    private GameRenderer renderer;
    /**
     * Gère les entrées utilisateurs
     */
    private GameInput input;

    /**
     * On crée un Game à partir d'une entrée et d'une sortie.
     *
     * @param input
     *     l'entrée
     * @param renderer
     *     la sortie
     */
    public Game(GameInput input, GameRenderer renderer) {

        this.renderer = renderer;
        this.input = input;
        players = new ArrayList<>();
    }

    public static void main(String[] args) throws IOException {

        // Aucune vérification, balec
        if (args != null && args.length == 1) {

            System.out.println("Mise en place de l'environnement de développement :");
            System.out.println("    Extraction des ressources ...");

            AssetsManager.extractAll();

            System.out.println("Mise en place de l'environnement de développement :");
            System.out.println("    Extraction des ressources ... OK");
            System.exit(0);
        }

        if (args != null && args.length == 4) {

            ImgConverter converter = ImgConverter.builder()
                                                 .mode(ImgConverter.Mode.valueOf(args[1]))
                                                 .smoothing(true)
                                                 .reductionScale(Integer.parseInt(args[2]))
                                                 .build();

            File f = new File(args[3]);
            if (!f.exists()) {
                System.err.println("Given file doesn't exist. ('" + f.getAbsolutePath() + "')");
                System.exit(-1);
            }

            BufferedImage image = ImageIO.read(f);

            System.out.print(converter.convert(image));
            System.exit(0);
        }

        Terminal terminal = TerminalBuilder.builder()
                                           .encoding(StandardCharsets.UTF_8)
                                           .jansi(true)
                                           .system(true)
                                           .name("Game")
                                           .nativeSignals(true)
                                           //.type("windows-vtp")
                                           .signalHandler(Game::handleSignal)
                                           .build();

        if (terminal instanceof DumbTerminal) {
            System.err.println("This terminal isn't supported ! Aborting.");
            System.exit(-1);
        }

        Attributes attr = terminal.enterRawMode();

        // TODO main menu

        Game game = new Game(new TerminalGameInput(terminal), new TerminalGameRenderer(terminal));

        // On affiche les maps disponibles et on demande au joueur de choisir
        System.out.println("Available maps :");
        for (Map.Entry<String, String> e : AssetsManager.listMaps().entrySet())
            System.out.println(" - " + e.getKey() + " (" + e.getValue() + ")");

        LineReader reader = LineReaderBuilder.builder()
                                             .terminal(terminal)
                                             .completer(new StringsCompleter(AssetsManager.mapNames()))
                                             .build();

        String map = reader.readLine("Choose a map : ").trim();

        LevelMap level = LevelMap.load(AssetsManager.listMaps().get(map));
        game.setMap(level);
        // TODO resize terminal on map loading

        // Pour l'instant il s'agit d'une partie toute bête

        AbstractPlayer player = new Player(AnsiColors.ColorFG.FG_YELLOW_BRIGHT);
        Coords startPos = level.getStartPos().get(0);
        player.addCharacter(new Character(startPos.getX(), startPos.getY(), "Mage", 'M'));

        game.addPlayer(player);

        game.start();

        clearTerm();
        terminal.close();
    }

    public static void clearTerm() {

        System.out.print(Anscapes.CLEAR);
    }

    /**
     * Cette méthode gère les signaux.
     *
     * @param sig
     *     le signal à gérer
     *
     * @see <a href="https://en.wikipedia.org/wiki/Signal_(IPC)">Signaux, systèmes POSIX</a>
     */
    private static void handleSignal(Terminal.Signal sig) {

        switch (sig) {
            case INT:
                System.out.println("Received SIGINT !");
                System.exit(0);
                break;
            case QUIT:
                System.out.println("Received SIGQUIT !");
                break;
            case TSTP:
                System.out.println("Received SIGTSTP !");
                break;
            case CONT:
                System.out.println("Received SIGCONT !");
                break;
            case INFO:
                System.out.println("Received SIGINFO !");
                break;
            case WINCH:
                // On window resize
                System.out.println("Received SIGWINCH !");
                break;
        }
    }

    /**
     * Ajoute un joueur dans le jeu.
     *
     * @param player
     *     le joueur à ajouter
     */
    public void addPlayer(AbstractPlayer player) {

        players.add(player);
    }

    /**
     * @return la liste des joueurs
     */
    public List<AbstractPlayer> getPlayers() {

        return players;
    }

    /**
     * @return la map du jeu
     */
    public LevelMap getMap() {

        return map;
    }

    /**
     * Défini la map du jeu
     *
     * @param map
     *     la map à utiliser
     */
    public void setMap(LevelMap map) {

        this.map = map;
    }

    /**
     * Lance la partie. La partie continue à l'infinie pour l'instant. (ou jusqu'a ce que le joueur appuie sur A)
     */
    public void start() {

        clearTerm();
        System.out.println("'ZQSD' pour se déplacer, 'A' pour quitter, '123' pour changer de couleur.");

        Character character = players.get(0).getCharacters().get(0);

        for (; ; ) {

            char read = (char) input.getInput();

            if (read == 'a')
                break;

            switch (read) {
                case 'z':
                    if (map.canCollide(character.getX(), character.getY() - 1))
                        character.decY();
                    break;
                case 'q':
                    if (map.canCollide(character.getX() - 1, character.getY()))
                        character.decX();
                    break;
                case 's':
                    if (map.canCollide(character.getX(), character.getY() + 1))
                        character.incY();
                    break;
                case 'd':
                    if (map.canCollide(character.getX() + 1, character.getY()))
                        character.incX();
                    break;
                case '1':
                    players.get(0).setColor(AnsiColors.ColorFG.FG_RED_BRIGHT);
                    break;
                case '2':
                    players.get(0).setColor(AnsiColors.ColorFG.FG_BLUE_BRIGHT);
                    break;
                case '3':
                    players.get(0).setColor(AnsiColors.ColorFG.FG_GREEN_BRIGHT);
                    break;
            }

            // Render the level with the player
            renderer.render(this);
        }
    }

}
