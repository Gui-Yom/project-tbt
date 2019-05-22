package lorganisation.projecttbt.ui.screens;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.AssetsManager;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.player.Bot;
import lorganisation.projecttbt.player.Player;
import lorganisation.projecttbt.ui.*;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.TerminalUtils;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Size;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

public class LobbyScreen extends Screen {


    private Game associatedGame;

    private boolean skip = false;

    private TextField pseudoField;
    private ColorPicker colorPicker;
    private IntegerField characterPerPlayerField;

    private PlayerListWidget playerList;

    private InvisibleButton addPlayerButton;
    private InvisibleButton addBotButton;
    private InvisibleButton confirmButton;

    public LobbyScreen(Game game) {

        super(game.getInput().getTerminal());

        this.associatedGame = game;


        addComponent(new Label(new Coords(0, 2), new StyledString("Map - " + game.getMap().getName().toUpperCase()), Utils.Align.CENTER));
        addComponent(new Label(new Coords(0, 3), new StyledString("Préparation de la partie - Joueurs"), Utils.Align.CENTER));
        addComponent(new Label(new Coords(0, 4), new StyledString("Nombre de joueurs maximum: ?"), Utils.Align.CENTER));

        characterPerPlayerField = new IntegerField(new Coords(4, 7),
                                                   new StyledString("Entrez le nombre de personnages par joueur: "),
                                                   Utils.Align.LEFT,
                                                   1,
                                                   1,
                                                   AssetsManager.gameCharacterNames().size());
        addComponent(characterPerPlayerField);
        setFocused(characterPerPlayerField);

        addBotButton = new InvisibleButton(() -> {

            game.addPlayer(new Bot(game.getAvailableColors()));
            playerList.updatePlayerList(game.getPlayers());

        }, KeyStroke.getKeyStroke('*'));
        addBotButton.setDescription("Press * to add a BOT");

        addComponent(addBotButton);

        addPlayerButton = new InvisibleButton(() -> {
            if (!characterPerPlayerField.isFocusable()) {showPlayerSubMenu(game);}
        }, KeyStroke.getKeyStroke('+'));
        addPlayerButton.setDescription("Press + to add a player");

        addComponent(addPlayerButton);


        confirmButton = new InvisibleButton(() -> {
            if (characterPerPlayerField.isFocusable()) {

                characterPerPlayerField.setEnabled(false);
                characterPerPlayerField.setFocusable(false);

                playerList.setVisible(true);

                showPlayerSubMenu(game);

                setFocused(pseudoField);
            } else if (pseudoField.isVisible() && pseudoField.getValue().length() >= 3) { // only visible if is creating player

                associatedGame.addPlayer(new Player(pseudoField.getValue(), colorPicker.getValue()));

                playerList.updatePlayerList(game.getPlayers());
                hidePlayerSubMenu();
                setFocused(pseudoField);

            } else {

                if (game.getPlayers().size() > 1) {skip = true;}
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        confirmButton.setDescription("Press ENTER to confirm");

        addComponent(confirmButton);


        playerList = new PlayerListWidget(new Coords(5, 5),
                                          new Size(0, 0),
                                          Utils.Align.RIGHT,
                                          Utils.Align.LEFT,
                                          new StyledString("Joueurs"),
                                          Anscapes.Colors.BLUE_BRIGHT,
                                          Anscapes.Colors.WHITE_BRIGHT);
        playerList.setVisible(false);
        addComponent(playerList);

        pseudoField = (TextField) addComponent(new TextField(new Coords(4, 9), new StyledString("Pseudo: "), Utils.Align.LEFT, 16));
        pseudoField.setVisible(false);

        colorPicker = (ColorPicker) addComponent(new ColorPicker(new Coords(4 + 8 /* "Pseudo: " */ + 16 /* maxSize */ + 2 /* some space*/, 9), game.getAvailableColors(), Utils.Align.LEFT));
        colorPicker.setVisible(false);

        hidePlayerSubMenu();
        addBotButton.setEnabled(false);
        addPlayerButton.setEnabled(false);

    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        while (!skip) {

            TerminalUtils.clearTerm();

            //Utils.writeAt(0, 0, " widget -> " + confirmButton);
            //Utils.writeAt(0, 1, " lastKey -> " + input.getLastKey());

            //int maxPlayers = associatedGame.getMap().getStartPos().size() / characterPerPlayerField.getValue();
            int maxPlayers = 4 / characterPerPlayerField.getValue();

            if (associatedGame.getPlayers().size() >= maxPlayers) {

                hidePlayerSubMenu();

                addPlayerButton.setEnabled(false);
                addBotButton.setEnabled(false);

                disableFocus();

                addComponent(new Label(new Coords(0, (int) (renderer.getSize().getRows() * .65)), new StyledString("Press ENTER to go start the game"), Utils.Align.CENTER));
            }

            Label maxPlayerLabel = (Label) this.getComponents().get(2);
            maxPlayerLabel.setText("Nombre de joueurs maximum: " + maxPlayers);

            renderer.render(this);

            if (getFocusedWidget() != null)
                TerminalUtils.writeAt(getFocusedWidget().getCoords().getX() - 2, getFocusedWidget().getCoords().getY(), "> ");

            keyPressed(input.readKey());
        }
    }

    private void showPlayerSubMenu(Game game) {

        addBotButton.setEnabled(false);
        addPlayerButton.setEnabled(false);

        pseudoField.setValue("");
        pseudoField.setVisible(true);

        colorPicker.setAvailableColors(game.getAvailableColors());
        colorPicker.setVisible(true);
    }

    private void hidePlayerSubMenu() {

        addBotButton.setEnabled(true);
        addPlayerButton.setEnabled(true);

        pseudoField.setVisible(false);
        colorPicker.setVisible(false);
    }

    public int getMaxCharacterCount() {

        return this.characterPerPlayerField.getValue();
    }
}
