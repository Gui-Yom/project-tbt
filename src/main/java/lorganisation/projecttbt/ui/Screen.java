package lorganisation.projecttbt.ui;

import lorganisation.projecttbt.utils.CyclicList;
import lorganisation.projecttbt.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public abstract class Screen {

    protected CyclicList<Widget> components;
    protected Widget focus = null;
    protected char focusKey = (char)9;//TAB by default

    public Screen() {

        components = new CyclicList<>();
    }

    public Widget setFocused(Widget widget) {
        if(widget.isFocusable() && widget.isVisible() && components.contains(widget)) {
            //widget.onFocus();
            components.setAt(components.indexOf(widget));
            return this.focus = widget;
        } else
            return null;
    }

    public Widget setFocused(int i) {
        return setFocused(components.get(i));
    }

    public Widget getFocusedWidget() {
        return this.focus;
    }

    public Widget nextFocused() {
        int i = components.size();
        while(setFocused(components.next()) == null) {
            if(i-- <= 0) return null;
        }

        return getFocusedWidget();
    }

    public List<Widget> getComponents() {

        return components;
    }

    public Widget addComponent(Widget component) {

        components.add(component);
        return component;
    }

    public void keyPressed(char key) {

        if(key == focusKey)
            nextFocused();

        if (getFocusedWidget() == null) {
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
        } else
            getFocusedWidget().handleEvent(key);
    }
}
