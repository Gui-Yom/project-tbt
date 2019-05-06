package lorganisation.projecttbt.ui;

import java.util.ArrayList;
import java.util.List;

public abstract class Screen {

    protected List<Widget> components;
    protected Widget selected;

    public Screen() {

        components = new ArrayList<>();
    }

    public List<Widget> getComponents() {

        return components;
    }

    public void addComponent(Widget component) {

        components.add(component);
    }

    public void keyPressed(char key) {

        for (Widget component : components)
            if (component instanceof ActionWidget) {
                ActionWidget ac = (ActionWidget) component;
                if (key == ac.getKey())
                    ac.action.run();
            } else if (component instanceof TextField) {
                TextField tf = (TextField) component;
                tf.handleEvent(key);
            } else if (component instanceof IntegerField) {
                IntegerField iF = (IntegerField) component;
                iF.handleEvent(key);
            }
    }
}
