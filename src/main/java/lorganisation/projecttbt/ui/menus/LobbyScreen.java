package lorganisation.projecttbt.ui.menus;

import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.ui.Screen;
import lorganisation.projecttbt.ui.Text;
import lorganisation.projecttbt.ui.TextField;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;

public class LobbyScreen extends Screen {

    public LobbyScreen() {

        super();

        addComponent(new Text(new Coords(0, 2), new StyledString("Préparation de la partie - Joueurs"), Utils.Align.CENTER));
        addComponent(new Text(new Coords(0, 2), new StyledString("Nombre de joueurs maximum: 4"), Utils.Align.CENTER));
        addComponent(new TextField("Entrez le nombre de personnages par joueur", 2));
    }

    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        renderer.render(this);

        int inputValue = input.getInput();


    }
}
