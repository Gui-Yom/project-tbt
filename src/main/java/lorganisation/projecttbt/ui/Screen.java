package lorganisation.projecttbt.ui;

import lorganisation.projecttbt.utils.CyclicList;

import java.util.List;

public abstract class Screen {

    protected CyclicList<Widget> components;
    protected Widget focus = null;
    protected char focusKey = (char) 9;//TAB by default

    public Screen() {

        components = new CyclicList<>();
    }

    public Widget setFocused(Widget widget) {

        if (widget.isFocusable() && widget.isVisible() && components.contains(widget)) {
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
        while (setFocused(components.next()) == null) {
            if (i-- <= 0) return null;
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

        if (key == focusKey && getFocusedWidget() != null) {

            getFocusedWidget().onFocusLost();

            nextFocused();

            getFocusedWidget().onFocused();

            return;
        }

        //first execute InvisibleButtons & unFocusable Buttons (require no focus and has priority)
        for (Widget widget : components)
            if (widget instanceof Button || widget instanceof InvisibleButton) {
                if (!(widget instanceof Button && widget.isFocusable()) && widget.handleEvent(key))
                    return;
            }

        // if a widget is focused
        if (getFocusedWidget() != null)
            getFocusedWidget().handleEvent(key); // let it handle the keyPressed event

    }
}
