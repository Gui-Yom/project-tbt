package lorganisation.projectrpg;

import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.ImgConverter;
import lorganisation.projectrpg.map.LevelMap;
import lorganisation.projectrpg.player.AbstractPlayer;
import lorganisation.projectrpg.player.Bot;
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
    private List<AbstractPlayer> players;
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

    private Terminal terminal;

    private List<Colors> availableColors;

    /**
     * On crée un Game à partir d'une entrée et d'une sortie.
     *
     * @param input
     *     l'entrée
     * @param renderer
     *     la sortie
     */
    public Game(Terminal term, TerminalGameInput input, TerminalGameRenderer renderer) {

        this.renderer = renderer;
        this.input = input;
        this.terminal = term;
        players = new ArrayList<>();
        availableColors = Utils.arrayToList(Colors.values());
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

        Utils.hideCursor();

        // TODO main menu

        Game game = new Game(terminal, new TerminalGameInput(terminal), new TerminalGameRenderer(terminal));

        // On affiche les maps disponibles et on demande au joueur de choisir
        Utils.clearTerm();
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
        //terminal.setSize(new Size(level.getWidth() + 50, level.getHeight() + 20));

        // TODO map selection menu
        game.lobby(terminal);
        game.start(terminal);

        Utils.clearTerm();
        terminal.close();
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
     * Choix du nombre de joueurs et du nombre de personnages
     */
    public void lobby(Terminal terminal) {

        //TODO: Limiter nombre de joueurs et de persos par joueurs selon

        Utils.clearTerm();
        Utils.writeFormattedLine(1,
                                 "LOBBY",
                                 new String[] { Colors.MAGENTA.bg(), Anscapes.RESET },
                                 true,
                                 Utils.Align.CENTER,
                                 0,
                                 terminal.getWidth());
        Utils.writeFormattedLine(2,
                                 "Nombre de joueurs max : " + getMap().getStartPos().size(),
                                 new String[] { Colors.RED.fg(), Anscapes.RESET },
                                 true,
                                 Utils.Align.CENTER,
                                 0,
                                 terminal.getWidth());

        // On récupère le nombre de personnage maximum par joueur ( <= nombre de personnages existant, interdiction de prendre 2 fois le même)
        int characterCount = Utils.promptReadInt(terminal, "Entrez le nombre de personnages par joueur [1]: ", 1, (n) -> n > 0 && n <= AssetsManager.characterNames().size());

        newPlayer(false);

        int maxPlayers = getMap().getStartPos().size() / characterCount;
        for (int i = getPlayers().size(); i < maxPlayers; i++) {
            System.out.println("Ajouter un joueur (+) | Ajouter un BOT (*) | Choix des personnages");

            char action = (char) this.input.getInput();

            if (action == '*')
                newPlayer(true);
            else if (action == '+')
                newPlayer(false);
            else
                break;
        }

        if (getPlayers().size() == 1)
            newPlayer(true);

        for (AbstractPlayer player : getPlayers())
            pickCharacters(terminal, player, characterCount);
    }

    /**
     * Créer et ajouter un joueur à la partie. Utilisé dans lobby()
     */
    public void newPlayer(boolean isBot) {

        System.out.print(Anscapes.CLEAR_LINE);

        AbstractPlayer player = null;
        if (isBot) {
            player = new Bot(availableColors);
        } else {
            String name = Utils.readLine(terminal, "Joueur " + (getPlayerCount() + 1) + ", lâche ton blaze bg: \n");
            player = new Player(name, pickColor(terminal, name));
        }
        addPlayer(player);
    }

    /**
     * Donne le nombre de joueurs non-bot dans la partie
     */
    public int getPlayerCount() {

        int count = 0;

        for (AbstractPlayer player : getPlayers())
            if (!player.isBot())
                count++;

        return count;
    }

    /**
     * Donne le nombre de BOTs non-bot dans la partie
     */
    public int getBotCount() {

        return getPlayers().size() - getPlayerCount();
    }


    /**
     * Selection d'un certain nombre de personnages pour un joueur
     */
    public void pickCharacters(Terminal terminal, AbstractPlayer picker, int characterCount) {

        Utils.clearTerm();

        Utils.writeFormattedLine(1,
                                 "Lobby - Choix des personnages ",
                                 new String[] { "", "" },
                                 true,
                                 Utils.Align.CENTER,
                                 0,
                                 terminal.getWidth());
        Utils.writeFormattedLine(2,
                                 picker.getName().toUpperCase(),
                                 new String[] { picker.getColor().fg(), "" },
                                 true,
                                 Utils.Align.CENTER,
                                 0,
                                 terminal.getWidth());

        LineReader reader = LineReaderBuilder.builder()
                                             .terminal(terminal)
                                             .completer(new StringsCompleter(AssetsManager.characterNames()))
                                             .build();

        for (int current = 1; current <= characterCount; current++) {
            String characterName = reader.readLine("Personnage " + current + ": ").trim();
            if(picker.hasCharacter(characterName)) {
                --current;
                System.out.print(Anscapes.movePreviousLine(1) + Anscapes.CLEAR_LINE); // TODO: Ajouter message d'erreur
            } else
                picker.addCharacter(new Character(characterName, this));

        }
    }

    /**
     * Selection d'une couleur, une par joueurs, utilisé dans lobby()
     */
    public Colors pickColor(Terminal terminal, String pickerName) {

        System.out.print(Anscapes.movePreviousLine(1)); // Retour à la ligne ou le mec a mis son pseudo pour overwrite

        int currentColor = 0;
        for (; ; ) {
            System.out.print(availableColors.get(currentColor).fg() + pickerName + Anscapes.RESET + " | Q / D pour changer de couleur, ENTRER pour valider"); // Réécrit le pseudo avec la bonne couleur

            char read = (char) input.getInput();
            if (read == 13)
                break; // (13 = SPACE)

            switch (read) {
                case 'q': {
                    currentColor = (currentColor + availableColors.size() - 1) % availableColors.size();
                    break;
                }
                case 'd': {
                    currentColor = (currentColor + 1) % availableColors.size();
                    break;
                }
            }
            System.out.print(Anscapes.moveLeft(terminal.getWidth()) + Anscapes.CLEAR_LINE); //On revient au début de la ligne
        }
        System.out.println();

        Colors color = availableColors.get(currentColor);
        availableColors.remove(color);

        return color;
    }

    /**
     * Lance la partie. La partie continue à l'infinie pour l'instant. (ou jusqu'a ce que le joueur appuie sur A)
     */
    public void start(Terminal terminal) {

        Utils.clearTerm();
        Utils.writeFormattedLine(2,
                                 "'ZQSD' pour se déplacer, 'A' pour quitter, '123' pour changer de couleur.",
                                 new String[] { "", "" },
                                 true,
                                 Utils.Align.CENTER,
                                 0,
                                 terminal.getWidth());

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
                    players.get(0).setColor(Colors.RED_BRIGHT);
                    break;
                case '2':
                    players.get(0).setColor(Colors.BLUE_BRIGHT);
                    break;
                case '3':
                    players.get(0).setColor(Colors.GREEN_BRIGHT);
                    break;
            }

            // Render the level with the player
            renderer.render(this);
        }
    }
}
