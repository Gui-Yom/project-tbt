package lorganisation.projecttbt.ui;

import java.util.ArrayList;
import java.util.List;

public abstract class Menu {

    protected List<MenuComponent> components;

    public Menu() {

        components = new ArrayList<>();
    }

    public List<MenuComponent> getComponents() {

        return components;
    }

    public void addComponent(MenuComponent component) {

        components.add(component);
    }

    public void sendEvent(char key) {


        for (MenuComponent component : components) {
            if (component instanceof ActionComponent) {
                ActionComponent ac = (ActionComponent) component;
                if (key == ac.getKey()) {
                    ac.action.run();
                }
            }
        }
    }
}
