package lorganisation.projecttbt.ui.screen;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.AssetsManager;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.player.attack.Attack;
import lorganisation.projecttbt.ui.widget.*;
import lorganisation.projecttbt.utils.*;
import org.jline.terminal.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Cet écran gère l'affichage du jeu pendant la partie
 */
public class GameScreen extends Screen {

    private TextBoxWidget gameInfo;
    private List<StyledString> infos = new ArrayList<>();
    private HorizontalComboBoxWidget attackSelect;
    private PlayerListWidget playerListW;
    private CharacterListWidget characterListW;
    private Label statusLabel, subStatusLabel;
    private Game game = Game.getInstance();

    public GameScreen() {

        super(Game.getInstance().getInput().getTerminal());

        Size termSize = game.getInput().getTerminal().getSize();

        addComponent(TerminalUtils.getTitle());

        statusLabel = new Label(new Coords(0, 3),
                                new StyledString(""),
                                Utils.Align.CENTER);
        addComponent(statusLabel);

        subStatusLabel = new Label(new Coords(0, 4),
                                   new StyledString(""),
                                   Utils.Align.CENTER);
        addComponent(subStatusLabel);

        gameInfo = new TextBoxWidget(new Coords(0, 0),
                                     new Size(termSize.getColumns() / 4, termSize.getRows() - 2),
                                     Utils.Align.RIGHT,
                                     Utils.Align.LEFT,
                                     new StyledString("Infos", Pair.of(0, Anscapes.Colors.BLUE_BRIGHT.fg())),
                                     Anscapes.Colors.MAGENTA_BRIGHT,
                                     Anscapes.Colors.BLACK);
        gameInfo.setAllowResize(false);
        addComponent(gameInfo);

        attackSelect = new HorizontalComboBoxWidget(new Coords(0, 3),
                                                    Utils.Align.RIGHT);

        InvisibleButton quitBtn = new InvisibleButton(Game::shutdownGracefully,
                                                      KeyUtils.KEY_ESCAPE,
                                                      "ESC to exit");
        addComponent(quitBtn);

        playerListW = new PlayerListWidget(new Coords(0, 1),
                                           new Size(termSize.getColumns() / 7, termSize.getRows() - 2),
                                           Utils.Align.LEFT,
                                           Utils.Align.LEFT,
                                           new StyledString("Joueurs"),
                                           Anscapes.Colors.BLUE_BRIGHT,
                                           Anscapes.Colors.WHITE_BRIGHT);
        playerListW.setAllowResize(false);
        addComponent(playerListW);

        characterListW = new CharacterListWidget(new Coords(0, 1),
                                                 new Size(playerListW.getSize().getColumns(), termSize.getRows() - 2 - playerListW.getSize().getRows()),
                                                 Utils.Align.LEFT,
                                                 Utils.Align.LEFT,
                                                 new StyledString("Personnages"),
                                                 Anscapes.Colors.MAGENTA_BRIGHT,
                                                 Anscapes.Colors.WHITE_BRIGHT,
                                                 game.getPlayers());
        characterListW.setAllowResize(false);
        addComponent(characterListW);
    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        TerminalUtils.clearTerm();
        Size termSize = renderer.getTerminal().getSize();

        AbstractPlayer currPlayer = game.getPlayers().current();
        Character currCharacter = currPlayer.getCharacters().current();

        attackSelect.resetOptions();
        for (Attack atk : currCharacter.getAttacks())
            attackSelect.addOption(new StyledString(atk.getName().toUpperCase(), Pair.of(0, currPlayer.getColor().fg())));

        if (currCharacter.getAttacks().current() != null) {
            gameInfo.setText(new ArrayList<>());
            gameInfo.addLine(new StyledString(""));
            gameInfo.addLines(currCharacter.getAttacks().current().getDescription());
        }
        gameInfo.addLine(new StyledString("_______"));
        gameInfo.addLine(new StyledString(""));
        gameInfo.addLines(currCharacter.getDescription());

        gameInfo.setSize(new Size(Utils.findLongestSequence(gameInfo.getLines()) + 4, termSize.getRows() - 1));

        for (StyledString info : infos)
            gameInfo.addLine(info);

        List<String> leftMenuText = new ArrayList<>();
        leftMenuText.addAll(game.getPlayerNames());
        leftMenuText.addAll(AssetsManager.gameCharacterNames());
        int leftMenuWidth = Utils.findLongestSequence(leftMenuText) + 9 /* 9 = colorBlock(2) + spaces(4) + borders(2) + bonus(1) */;

        playerListW.setSelected(game.getPlayers().getIndex());
        playerListW.setSize(new Size(leftMenuWidth, game.getPlayers().size() + 5));
        playerListW.updatePlayerList(game.getPlayers());

        characterListW.setSelected(game.getPlayers().getIndex());
        characterListW.setCoords(new Coords(playerListW.getCoords().getX(), playerListW.getCoords().getY() + playerListW.getSize().getRows()));
        characterListW.setSize(new Size(playerListW.getSize().getColumns(), termSize.getRows() - 2 - playerListW.getSize().getRows())); // -2 car taille de la liste de controls sur le GameScreen (constant)
        characterListW.updatePlayerList(game.getPlayers());

        statusLabel.setText(new StyledString(" Tour no " + game.getNumTurn() + " : " + currPlayer.getName(), Pair.of(12 + String.valueOf(game.getNumTurn()).length(), currPlayer.getColor().fg())));

        subStatusLabel.setText(new StyledString(currPlayer.getName() + " " + currPlayer.getStatus() + " avec " + currCharacter.getType(),
                                                Pair.of(0, currPlayer.getColor().fg()),
                                                Pair.of(currPlayer.getName().length(), Anscapes.RESET),
                                                Pair.of(currPlayer.getName().length() + 7 + currPlayer.getStatus().toString().length(), currPlayer.getColor().fg()),
                                                Pair.of(currPlayer.getName().length() + 7 + currPlayer.getStatus().toString().length() + currCharacter.getType().length(), Anscapes.RESET)));

        // On affiche le jeu et les composants
        renderer.render(game, 0, 10, Utils.Align.CENTER);
        renderer.render(this);
    }

    public void addInfo(StyledString styledString) {

        this.infos.add(styledString);
    }

    public void addInfo(String string) {

        if (string.length() > gameInfo.getSize().getColumns())
            for (int i = 0; i < string.length(); i = i + gameInfo.getSize().getColumns()) {
                int a = i + gameInfo.getSize().getColumns();
                if (a > string.length())
                    a = string.length();
                this.infos.add(new StyledString(string.substring(i, a)));
            }

        else
            this.infos.add(new StyledString(string));
    }
}