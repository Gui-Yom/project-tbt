package lorganisation.projecttbt;

import lorganisation.projecttbt.ui.Button;
import lorganisation.projecttbt.ui.Menu;
import lorganisation.projecttbt.ui.Text;
import lorganisation.projecttbt.utils.Coords;

public class MainMenu extends Menu {

    public MainMenu() {

        super();
        addComponent(new Button('b', () -> System.out.println("Clicked button !")));
        addComponent(new Text("title", new Coords(1, 5), "Project: TBT"));
        addComponent(new Text("start", new Coords(5, 1), "Appuyez sur 'p' pour commencer !"));
    }
}
