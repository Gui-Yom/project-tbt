package lorganisation.projecttbt;

import com.limelion.anscapes.ColorMode;
import lorganisation.projecttbt.map.LevelMap;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.ActionType;
import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.player.Player;
import lorganisation.projecttbt.ui.screen.*;
import lorganisation.projecttbt.utils.Coords;
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
import java.util.ArrayList;
import java.util.List;

import static com.limelion.anscapes.Anscapes.Colors;

/* TODO some ideas:
     -  background images for each menu
     -  correct colors to match game theme
     -  fix TextBoxes (make a maxSize) -> KINDA
     -  fix UI -> KINDA (not dynamic but working)
     -  preview map (MapSelectionScreen) -> flemme, plus tard
     -  make proper Game Info -> OK
     -  make able to go back to previous menu using CyclicList<Screen>
     -  fix the game bounds problem -> OK
     -  add customizable attacks (AttackDeserializer)
     -  add Sound + Game Music Theme
     -  Background(Object) attribute in Screen (can be made with uniform color, gradient, image)
     -  Cooldowns and effects tick
 */
/*  FIXME:
        - do not compare character but instead keyCode (or else you can't navigate in Character Selection)

 */

/**
 * La classe principale du jeu.
 *
 * @see Game#main(String[])
 */
public class Game {

    private static Game INSTANCE;
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
    private int numCharacters;
    private int numTurn = 1;

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
        INSTANCE = new Game(new TerminalGameInput(term), new TerminalGameRenderer(term));

        // On demande à l'utilisateur de redimensionner sa fenêtre
        // Sur Windows, on ne peut pas la redimensionner depuis le programme
        TerminalUtils.askForResize(term, new Size(130, 40));

        //new TestScreen().display(INSTANCE.input, INSTANCE.renderer);

        // Menu principal du jeu
        // L'utilisateur peut choisir de démarrer une partie ou d'utiliser les outils de développement intégrés
        INSTANCE.mainMenu();

        // L'utilisateur peut choisir la map
        INSTANCE.mapSelection();

        // Les joueurs choisissent leurs personnages et leurs couleurs
        INSTANCE.lobby();

        // La partie commence !
        // Elle continue jusqu'à ce que mort s'ensuive mdr
        INSTANCE.start();

        // Quelqu'un a gagné ! Fin du jeu
        INSTANCE.winner();

        // END / Cleanup
        shutdownGracefully();
    }

    public static Game getInstance() {

        return INSTANCE;
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
            case TSTP:
            case CONT:
            case INFO:
            case WINCH: // Window resize
                // System.out.println("Received SIGWINCH !");
                break;
        }
    }

    /**
     * Permet de quitter correctement le programme
     */
    public static void shutdownGracefully() {

        TerminalUtils.clearTerm();
        TerminalUtils.exitPrivateMode();

        System.exit(0);
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
     * @return input manager
     */
    public TerminalGameInput getInput() {

        return input;
    }

    /**
     * Renvoie le renderer utilisé par le jeu.
     *
     * @return game renderer
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

        new MapSelectionScreen().display(input, renderer);
    }

    /**
     * Les joueurs peuvent choisir leur personnages et ajouter des IA à la partie.
     */
    public void lobby() {

        LobbyScreen lobbyScreen = new LobbyScreen();
        lobbyScreen.display(input, renderer);

        this.numCharacters = lobbyScreen.getMaxCharacterCount();

        CharacterSelectionScreen characterSelectionScreen = new CharacterSelectionScreen(lobbyScreen.getMaxCharacterCount());
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

    /**
     * Donne une lsite contenant le noms de tous les joueurs dans la partie
     */
    public List<String> getPlayerNames() {

        List<String> names = new ArrayList<>();

        for (AbstractPlayer player : getPlayers())
            names.add(player.getName());

        return names;
    }

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

        // L'écran du jeu
        GameScreen gameScreen = new GameScreen();

        // On cycle sur les personnages tant que la partie n'est pas finie
        while (!isFinished()) {

            //début tour d'un joueur
            AbstractPlayer currPlayer = getPlayers().next();
            Character currCharacter = currPlayer.getCharacters().next();

            while (!isTurnFinished(currPlayer)) {

                gameScreen.display(input, renderer);

                // get status before modified by play()
                AbstractPlayer.Status currStatus = currPlayer.getStatus();

                ActionType actionType = currPlayer.play(this, currCharacter);

                if (actionType == ActionType.DO_NOTHING && currPlayer instanceof Player) {
                    gameScreen.keyPressed(input.getLastKey());
                } else {
                    gameScreen.display(input, renderer);
                }

                /*if (actionType == ActionType.CAST_ATTACK)
                    gameScreen.addInfo(currPlayer.getName() + "(" + currStatus.name() + " -> " + currPlayer.getStatus().name() + ")" + " CASTED " + currCharacter.getAttacks().current().getName() + " with " + currCharacter.getType());*/

                if (actionType == ActionType.CAST_ATTACK && currStatus == AbstractPlayer.Status.CASTING_ATTACK)
                    break; // SI IL LANCE LATTAQUE
            }

            currCharacter.turnReset();
            ++numTurn;

            // on vire tous les personnages morts, si un joueur n'a plus de persos, c'est qu'il a perdu, il est out / kaput, il dégage ses morts de la game.
            clearDeads();
        }
    }

    /**
     * FIXME: à simplifier (pb de Concurrent Modification) Supprime les personnages morts, puis les joueurs n'ayant plus
     * de personnages
     */
    private void clearDeads() {

        List<Integer> aliventPlayers = new ArrayList<>();

        int k = 0;
        for (AbstractPlayer player : getPlayers()) {
            int i = 0;

            List<Integer> toRemove = new ArrayList<>();
            for (Character character : player.getCharacters()) {
                if (character.getHealth() <= 0)
                    toRemove.add(i);
                ++i;
            }

            for (Integer j : toRemove)
                player.getCharacters().remove(j.intValue());

            if (player.getCharacters().size() == 0 || player.getCharacters().isEmpty())
                aliventPlayers.add(k);

            ++k;
        }

        for (Integer l : aliventPlayers)
            getPlayers().remove(l.intValue());
    }

    /**
     * Affiche l'écran du victoire, assez primitif. Le joueur gagnant est le seul restant dans la
     * CyclicList<AbstractPlayer>
     */
    private void winner() {

        WinnerScreen winnerScreen = new WinnerScreen();
        winnerScreen.display(input, renderer);
    }

    /**
     * Check if game is finished
     *
     * @return true if game finished
     */
    public boolean isFinished() {

        int playersAlive = 0;

        for (AbstractPlayer player : getPlayers())
            for (Character character : player.getCharacters())
                if (character.getHealth() > 0) {
                    ++playersAlive;
                    break;
                }

        return playersAlive <= 1;
    }

    /**
     * Check if player's turn is finished, doesn't take into account if player has attacked
     *
     * @param player
     *
     * @return true if turn finished
     */
    public boolean isTurnFinished(AbstractPlayer player) {

        Character c = player.getCharacters().current();

        return c.getActionPoints() <= 0 || c.getHealth() <= 0;
    }

    /**
     * Donne le nombre de tours qui se sont écoulés
     *
     * @return int game turn count
     */
    public int getNumTurn() {

        return numTurn;
    }

    /**
     * @param x
     * @param y
     *
     * @return true si la position donnée est libre (sans joueur)
     */
    public boolean isTileFree(int x, int y) {

        for (AbstractPlayer p : players)
            for (Character c : p.getCharacters())
                if (c.getPos().getX() == x && c.getPos().getY() == y)
                    return false;

        return true;
    }

    /**
     * @param pos
     *
     * @return true si la position donnée est libre (sans joueur)
     */
    public boolean isTileFree(Coords pos) {

        return isTileFree(pos.getX(), pos.getY());
    }
}