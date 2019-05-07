package lorganisation.projecttbt.ui.screens;

import lorganisation.projecttbt.AssetsManager;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.ui.Button;
import lorganisation.projecttbt.ui.IntegerField;
import lorganisation.projecttbt.ui.Label;
import lorganisation.projecttbt.ui.Screen;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;

public class LobbyScreen extends Screen {


    private Game associatedGame;

    public LobbyScreen(Game game) {

        super();

        this.associatedGame = game;

        addComponent(new Label(new Coords(0, 2), new StyledString("Préparation de la partie - Joueurs"), Utils.Align.CENTER));
        addComponent(new Label(new Coords(0, 3), new StyledString("Nombre de joueurs maximum: 4"), Utils.Align.CENTER));
        addComponent(new IntegerField(new Coords(4, 5), new StyledString("Entrez le nombre de personnages par joueur: "), Utils.Align.LEFT, 1,1, AssetsManager.gameCharacterNames().size()));

        addComponent(new Button('*', () -> game.newPlayer(true))); // not working
        addComponent(new Button('+', () -> game.newPlayer(false))); // not working
    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        renderer.render(this);

        int maxPlayers;
        int inputValue;
        IntegerField integerField;
        for(;;) {
            integerField = (IntegerField)this.getComponents().get(2);
            inputValue = input.getInput();
            if (inputValue == 13) { //ENTER
                maxPlayers = integerField.getValue();
                break;
            } else {
                keyPressed((char) inputValue);

                    maxPlayers = associatedGame.getMap().getStartPos().size() / integerField.getValue();
                    Label maxPlayerLabel = (Label) this.getComponents().get(1);
                    maxPlayerLabel.setText("Nombre de joueurs maximum: " + maxPlayers);


                display(input, renderer);
            }

        }
    }
}
