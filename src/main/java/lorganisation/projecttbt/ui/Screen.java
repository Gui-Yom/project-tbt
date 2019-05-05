package lorganisation.projecttbt.ui;

import java.util.ArrayList;
import java.util.List;

public abstract class Screen {

    protected List<Widget> components;

    public Screen() {

        components = new ArrayList<>();
    }

    public List<Widget> getComponents() {

        return components;
    }

    public void addComponent(Widget component) {

        components.add(component);
    }

    public void sendEvent(char key) {

        for (Widget component : components)
            if (component instanceof ActionComponent) {
                ActionComponent ac = (ActionComponent) component;
                if (key == ac.getKey())
                    ac.action.run();
            }
    }
}
