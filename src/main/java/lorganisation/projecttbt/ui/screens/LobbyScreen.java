package lorganisation.projecttbt.ui.screens;

import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.player.Bot;
import lorganisation.projecttbt.player.Player;
import lorganisation.projecttbt.ui.*;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

public class LobbyScreen extends Screen {


    private Game associatedGame;

    private boolean skip = false;

    private TextField pseudoField;
    private ColorPicker colorPicker;
    private IntegerField characterPerPlayerField;

    private InvisibleButton addPlayerButton;
    private InvisibleButton addBotButton;
    private InvisibleButton confirmButton;


    public LobbyScreen(Game game) {

        super();

        this.associatedGame = game;


        addComponent(new Label(new Coords(0, 2), new StyledString("Map - " + game.getMap().getName().toUpperCase()), Utils.Align.CENTER));
        addComponent(new Label(new Coords(0, 3), new StyledString("Préparation de la partie - Joueurs"), Utils.Align.CENTER));
        addComponent(new Label(new Coords(0, 4), new StyledString("Nombre de joueurs maximum: 4"), Utils.Align.CENTER));
        characterPerPlayerField = (IntegerField) setFocused(addComponent(new IntegerField(new Coords(4, 7), new StyledString("Entrez le nombre de personnages par joueur: "), Utils.Align.LEFT, 1, 1, /*AssetsManager.gameCharacterNames().size()*/5)));

        addBotButton = new InvisibleButton(() -> {
            game.addPlayer(new Bot(game.getAvailableColors()));
            showPlayerSubMenu(game);
        }, KeyStroke.getKeyStroke('*'));

        addComponent(addBotButton);
        addBotButton.setEnabled(false);

        addPlayerButton = new InvisibleButton(() -> {
            if (!characterPerPlayerField.isFocusable()) {showPlayerSubMenu(game);}
        }, KeyStroke.getKeyStroke('+'));

        addComponent(addPlayerButton);
        addPlayerButton.setEnabled(false);

        confirmButton = new InvisibleButton(() -> {
            if (characterPerPlayerField.isFocusable()) {

                characterPerPlayerField.setEnabled(false);

                showPlayerSubMenu(game);

                setFocused(pseudoField);
            } else if (pseudoField.isVisible() && pseudoField.getValue().length() >= 3) { // only visible if is creating player

                associatedGame.addPlayer(new Player(pseudoField.getValue(), colorPicker.getValue()));

                showPlayerSubMenu(game);
            } else {

                if (game.getPlayers().size() > 1) {skip = true;}
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));

        addComponent(confirmButton);

        pseudoField = (TextField) addComponent(new TextField(new Coords(4, 9), new StyledString("Pseudo: "), Utils.Align.LEFT, 16));
        pseudoField.setVisible(false);

        colorPicker = (ColorPicker) addComponent(new ColorPicker(new Coords(4 + 8 /* "Pseudo: " */ + 16 /* maxSize */ + 2 /* some space*/, 9), game.getAvailableColors(), Utils.Align.LEFT));
        colorPicker.setVisible(false);

    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        Utils.clearTerm();

        //int maxPlayers = associatedGame.getMap().getStartPos().size() / characterPerPlayerField.getValue();
        int maxPlayers = 4 / characterPerPlayerField.getValue();

        if (associatedGame.getPlayers().size() >= maxPlayers) {
            addPlayerButton.setEnabled(false);
            addBotButton.setEnabled(false);


            hidePlayerSubMenu();
            disableFocus();
        }

        Label maxPlayerLabel = (Label) this.getComponents().get(2);
        maxPlayerLabel.setText("Nombre de joueurs maximum: " + maxPlayers);

        renderer.render(this);
        if (getFocusedWidget() != null)
            Utils.writeAt(getFocusedWidget().getCoords().getX() - 2, getFocusedWidget().getCoords().getY(), "> ");

        keyPressed(input.readKey());


        if (!skip) display(input, renderer);
    }

    private void showPlayerSubMenu(Game game) {

        addBotButton.setEnabled(true);
        addPlayerButton.setEnabled(true);

        pseudoField.setValue("");
        pseudoField.setVisible(true);

        colorPicker.setAvailableColors(game.getAvailableColors());
        colorPicker.setVisible(true);
    }

    private void hidePlayerSubMenu() {

        addBotButton.setEnabled(false);
        addPlayerButton.setEnabled(false);

        pseudoField.setVisible(false);
        colorPicker.setVisible(false);
    }
}
