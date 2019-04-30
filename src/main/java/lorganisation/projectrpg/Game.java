package lorganisation.projectrpg;

import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.AnsiColors;
import com.limelion.anscapes.AnsiColors.ColorFG;
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

        game.lobby(terminal);
        game.start(terminal);

        clearTerm();
        terminal.close();
    }

    public static void clearTerm() {

        System.out.print(Anscapes.CLEAR);
        System.out.print(Anscapes.cursorPos(0, 0));
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
    public void lobby(Terminal terminal) { //TODO: Limiter nombre de joueurs et de persos par joueurs selon si
        clearTerm();
        writeFormatttedLine(2, "Préparation - Lobby", new String[] { "", "" }, true, Align.CENTER, 0, terminal.getWidth());
        writeFormatttedLine(2, String.valueOf(getMap().getStartPos().size()), new String[] { "", "" }, true, Align.CENTER, 0, terminal.getWidth());

        LineReader reader = LineReaderBuilder.builder()
                                             .terminal(terminal)
                                             .build();

        // On récupère le nombre de personnage maximum par joueur ( <= nombre de personnages existant, interdiction de prendre 2 fois le même)
        int characterCount;
        String input;
        do {
            input = reader.readLine(" Entrez le nombre de personnages par joueur: ");
            try {
                characterCount = Integer.parseInt(input);

                if (!(characterCount <= AssetsManager.characterNames().size() && characterCount > 0))
                    System.out.println("Il doit y avoir au minimum 1 personnage par joueur, et au maximum " + AssetsManager.characterNames().size() + Anscapes.moveUp(2));
                else
                    break;

            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre !");
            }
        } while (true);

        newPlayer(terminal, reader, false);

        int maxPlayers = getMap().getStartPos().size() / characterCount;
        for (int i = getPlayerCount(); i < maxPlayers; i++) {
            System.out.println("Ajouter un joueur (+) | Ajouter un BOT (*) | Choix des personnages");

            char action = (char) this.input.getInput();

            if (action == '*')
                newPlayer(terminal, reader, true);
            else if (action == '+')
                newPlayer(terminal, reader, false);
            else
                break;
        }

        if (getPlayers().size() == 1)
            newPlayer(terminal, reader, true);

        for (AbstractPlayer player : getPlayers())
            pickCharacters(terminal, player, characterCount);
    }

    /**
     * Créer et ajouter un joueur à la partie. Utilisé dans lobby()
     */
    public void newPlayer(Terminal terminal, LineReader reader, boolean isBot) {

        System.out.print(Anscapes.CLEAR_LINE);

        AbstractPlayer player = isBot ? new Bot() : new Player();
        String name;
        ColorFG color;
        if (isBot) {
            name = "BOT " + (getBotCount() + 1);
            System.out.println(name);
        } else
            name = reader.readLine("Joueur " + (getPlayerCount() + 1) + ", lâche ton blaze bg: \n");

        color = pickColor(terminal, name);

        player.setName(name);
        player.setColor(color);
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
     * Donne les couleurs non-utilisées par les autres joueurs, et la carte utilisée
     */
    public List<ColorFG> getAvailableColors() {

        ColorFG[] colors = ColorFG.values();
        List<ColorFG> availableColors = new ArrayList<>();
        boolean mustAdd;
        for (ColorFG color : colors) {
            mustAdd = true;
            for (AbstractPlayer player : getPlayers())
                if (player.getColor() == color) // Aussi enlever les couleurs utilisées par la map sélectionnée histoire de pas avoir de perso. invisible
                    mustAdd = false;
            if (mustAdd)
                availableColors.add(color);
        }

        return availableColors;
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

        clearTerm();
        writeFormatttedLine(1, "Lobby - Choix des personnages", new String[] { "", "" }, true, Align.CENTER, 0, terminal.getWidth());
        writeFormatttedLine(2, picker.getName().toUpperCase(), new String[] { picker.getColor().toString(), "" }, true, Align.CENTER, 0, terminal.getWidth());

        LineReader reader = LineReaderBuilder.builder()
                                             .terminal(terminal)
                                             .completer(new StringsCompleter(AssetsManager.characterNames()))
                                             .build();

        for (int current = 1; current <= characterCount; current++) {
            String characterName = reader.readLine("Personnage " + current + ": ").trim();
            picker.addCharacter(new Character(characterName, this));
        }
    }

    /**
     * Ecrit une ligne, avec alignement
     */
    public void writeFormatttedLine(int n, String s, String[] modifiers, boolean clear, Align alignment, int offset, int width) {

        int x;
        if (alignment.equals(Align.LEFT))
            x = offset;
        else if (alignment.equals(Align.RIGHT))
            x = width - s.length() - offset;
        else
            x = (width - s.length()) / 2 + offset;

        System.out.print(Anscapes.cursorPos(n, x)); // Aller à la ligne n

        if (clear)
            System.out.print(Anscapes.CLEAR_LINE);

        System.out.println(modifiers[0] + s + modifiers[1] + Anscapes.RESET);
    }

    /**
     * Selection d'une couleur, une par joueurs, utilisé dans lobby()
     */
    public ColorFG pickColor(Terminal terminal, String pickerName) {

        //Récupère couleurs dispo. (non utilisées par autres joueurs) => Faire méthode
        List<ColorFG> availableColors = getAvailableColors();

        System.out.print(Anscapes.movePreviousLine(1)); // Retour à la ligne ou le mec a mis son pseudo pour overwrite

        int currentColor = 0;
        for (; ; ) {
            System.out.print(availableColors.get(currentColor) + pickerName + AnsiColors.ColorFG.FG_WHITE + "    Q / D pour changer de couleur, ENTRER pour valider" + Anscapes.RESET); // Réécrit le pseudo avec la bonne couleur

            char read = (char) input.getInput();
            if (read == 13) break; // (13 = SPACE)

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


        return availableColors.get(currentColor);
    }

    /**
     * Lance la partie. La partie continue à l'infinie pour l'instant. (ou jusqu'a ce que le joueur appuie sur A)
     */
    public void start(Terminal terminal) {

        clearTerm();
        writeFormatttedLine(2, "'ZQSD' pour se déplacer, 'A' pour quitter, '123' pour changer de couleur.", new String[] { "", "" }, true, Align.CENTER, 0, terminal.getWidth());

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

    enum Align {
        LEFT, CENTER, RIGHT
    }

}
