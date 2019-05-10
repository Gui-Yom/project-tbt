package lorganisation.projecttbt;

import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.ImgConverter;
import lorganisation.projecttbt.map.LevelMap;
import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.player.*;
import lorganisation.projecttbt.ui.screens.LobbyScreen;
import lorganisation.projecttbt.ui.screens.MainScreen;
import lorganisation.projecttbt.ui.screens.MapSelectionScreen;
import lorganisation.projecttbt.ui.screens.TestScreen;
import lorganisation.projecttbt.utils.CyclicList;
import lorganisation.projecttbt.utils.Pair;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;
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

    private Terminal terminal;

    private List<Colors> availableColors;

    /**
     * @param term une référence vers l'objet Terminal à utiliser
     */
    public Game(Terminal term) {

        this.renderer = new TerminalGameRenderer(term);
        this.input = new TerminalGameInput(term);
        this.terminal = term;
        players = new CyclicList<>();
        availableColors = Utils.arrayToList(Colors.values());
    }

    public static void main(String[] args) throws IOException {

        // J'hésite à utiliser commons-cli mais ça ne ferai que rajouter du bordel
        if (args != null && args.length == 1) {

            devenv();
            System.exit(0);
        }

        if (args != null && args.length == 4) {

            convert(ImgConverter.Mode.valueOf(args[1]), Integer.parseInt(args[2]), new File(args[3]));
            System.exit(0);
        }

        Terminal terminal = TerminalBuilder.builder()
                                           .encoding(StandardCharsets.UTF_16LE)
                                           .jansi(true)
                                           .system(true)
                                           .name("Project: TBT")
                                           .nativeSignals(true)
                                           .type("windows-vtp")
                                           //.signalHandler(Utils::handleSignal)
                                           .build();

        if (terminal instanceof DumbTerminal) {
            System.err.println("This terminal isn't supported ! Aborting.");
            System.exit(-1);
        }

        Attributes attr = terminal.enterRawMode();

        Utils.hideCursor();

        Game game = new Game(terminal);

        // On affiche les maps disponibles et on demande au joueur de choisir
        Utils.clearTerm();

        (new TestScreen(game)).display(game.input, game.renderer);
        Utils.clearTerm();

        game.mainMenu();
        Utils.clearTerm();

        game.mapSelection();
        Utils.clearTerm();

        game.lobby();
        Utils.clearTerm();
        game.start();

        Utils.clearTerm();
        Utils.showCursor();
        terminal.close();
    }

    private static void devenv() {

        // Met en place l'environement de développement.
        // Extrait les ressources du jeu
        System.out.println("Mise en place de l'environnement de développement :");
        System.out.println("    Extraction des ressources ...");

        if (!AssetsManager.extract("registry.json")) {
            System.err.println("Unable to extract 'registry.json'.");
        }
        if (!AssetsManager.extract("bots.txt")) {
            System.err.println("Unable to extract 'bots.txt'.");
        }
        for (String file : AssetsManager.gameMapFiles())
            if (!AssetsManager.extract(file))
                System.err.println("Unable to extract '" + file + "'.");
        for (String file : AssetsManager.gameCharacterFiles())
            if (!AssetsManager.extract(file))
                System.err.println("Unable to extract '" + file + "'.");

        System.out.println("Mise en place de l'environnement de développement :");
        System.out.println("    Extraction des ressources ... OK");
    }

    private static void convert(ImgConverter.Mode mode, int reduction, File f) {

        ImgConverter converter = ImgConverter.builder()
                                             .mode(mode)
                                             .smoothing(true)
                                             .reductionScale(reduction)
                                             .build();

        if (!f.exists()) {
            System.err.println("Given file doesn't exist. ('" + f.getAbsolutePath() + "')");
            System.exit(-1);
        }

        BufferedImage image = null;
        try {
            image = ImageIO.read(f);
            System.out.print(converter.convert(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ajoute un joueur dans le jeu.
     *
     * @param player le joueur à ajouter
     */
    public void addPlayer(AbstractPlayer player) {

        players.add(player);
    }

    /**
     * @return la liste des joueurs
     */
    public CyclicList<AbstractPlayer> getPlayers() {

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
     * @param map la map à utiliser
     */
    public void setMap(LevelMap map) {

        this.map = map;
    }

    public void mainMenu() {

        MainScreen mainMenu = new MainScreen();
        mainMenu.display(input, renderer);
    }

    public void mapSelection() {

        MapSelectionScreen mapSelectionMenu = new MapSelectionScreen(this);
        mapSelectionMenu.display(input, renderer);

        /*System.out.println("Available maps :");
        for (Map.Entry<String, String> e : AssetsManager.gameMaps().entrySet())
            System.out.println(" - " + e.getKey() + " (" + e.getValue() + ")");

        LineReader reader = LineReaderBuilder.builder()
                                             .terminal(terminal)
                                             .completer(new StringsCompleter(AssetsManager.gameMapNames()))
                                             .build();

        String map = reader.readLine("Choose a map : ").trim();

        //terminal.setSize(new Size(level.getWidth() + 50, level.getHeight() + 20));
        this.map = LevelMap.load(AssetsManager.gameMaps().get(map));*/
    }

    /**
     * Choix du nombre de joueurs et du nombre de personnages
     */
    public void lobby() {

        LobbyScreen lobbyScreen = new LobbyScreen(this);
        lobbyScreen.display(input, renderer);

        //TODO: Limiter nombre de joueurs et de persos par joueurs selon

        /*System.out.println(getMap());
        Utils.writeFormattedLine(1,
                                 2,
                                 new StyledString("LOBBY", Pair.of(0, Colors.MAGENTA.bg())),
                                 Utils.Align.CENTER,
                                 terminal.getWidth());
        Utils.writeFormattedLine(2,
                                 5,
                                 new StyledString("Nombre de joueurs max : " + getMap().getStartPos().size(),
                                                  Pair.of(0, Colors.RED.fg())),
                                 Utils.Align.CENTER,
                                 terminal.getWidth());

        // On récupère le nombre de personnage maximum par joueur ( <= nombre de personnages existant, interdiction de prendre 2 fois le même)
        int characterCount = Utils.promptReadInt(terminal, "Entrez le nombre de personnages par joueur [1]: ", 1, (n) -> n > 0 && n <= AssetsManager.gameCharacterNames().size());

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

        for (AbstractPlayer player : players)
            pickCharacters(terminal, player, characterCount);*/
    }

    /**
     * Renvoie la liste des couleurs disponibles pour les joueurs
     */
    public List<Colors> getAvailableColors() {

        return this.availableColors;
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
                                 0,
                                 new StyledString("Lobby - Choix des personnages"),
                                 Utils.Align.CENTER,
                                 terminal.getWidth());
        Utils.writeFormattedLine(2,
                                 0,
                                 new StyledString(picker.getName().toUpperCase(), Pair.of(0, picker.getColor().fg())),
                                 Utils.Align.CENTER,
                                 terminal.getWidth());

        LineReader reader = LineReaderBuilder.builder()
                                             .terminal(terminal)
                                             .completer(new StringsCompleter(AssetsManager.gameCharacterNames()))
                                             .build();

        for (int current = 1; current <= characterCount; current++) {
            String characterName = reader.readLine("Personnage " + current + ": ").trim();
            if (picker.hasCharacter(characterName)) {
                --current;
                System.out.print(Anscapes.movePreviousLine(1) + Anscapes.CLEAR_LINE); // TODO: Ajouter message d'erreur
            } else {
                Character character = CharacterTemplate.getCharacterTemplate(characterName).createCharacter();
                map.getNextStartPos().setCharacter(character);
                picker.addCharacter(character);
            }
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
                break; // (13 = ENTER)

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
    public void start() {

        Utils.writeFormattedLine(2,
                                 0,
                                 new StyledString("'Z Q S D' pour se déplacer, 'ESC' pour quitter",
                                                  Pair.of(2, Colors.YELLOW.fg()),
                                                  Pair.of(8, Anscapes.RESET),
                                                  Pair.of(29, Colors.YELLOW.fg()),
                                                  Pair.of(32, Anscapes.RESET)),
                                 Utils.Align.CENTER,
                                 terminal.getWidth());

        input.getInput(); // wait for user keypress to skip menu

        Character character;
        AbstractPlayer player;
        for (int turn = 0; ; turn++) {

            // Render the level
            renderer.render(this);


            player = getPlayers().next();
            character = player.getCharacters().next();
            int movementsLeft = character.getPortee();
            boolean hasAttacked = false;

            System.out.print(Anscapes.cursorPos(character.getY() + 1, character.getX() + 1)); //have cursor blink on playing character

            char read = (char) input.getInput();

            if (read == 27) { // ESCAPE
                break;
            }
            switch (read) {
                case 'z':
                    if (map.canCollide(character.getX(), character.getY() - 1)) {
                        character.decY();
                        movementsLeft--;
                    }
                    break;
                case 'q':
                    if (map.canCollide(character.getX() - 1, character.getY())) {
                        character.decX();
                        movementsLeft--;
                    }
                    break;
                case 's':
                    if (map.canCollide(character.getX(), character.getY() + 1)) {
                        character.incY();
                        movementsLeft--;
                    }
                    break;
                case 'd':
                    if (map.canCollide(character.getX() + 1, character.getY())) {
                        character.incX();
                        movementsLeft--;
                    }
                    break;
                case 'a': {
                    //TODO: attack
                    hasAttacked = true;
                }
            }


            //TODO: change so that player can attack but can't move with 0 movementsLeft
            if (!(hasAttacked) || (movementsLeft <= 0)) //turn isn't finished
                --turn; // don't change turn index
        }
    }
}
