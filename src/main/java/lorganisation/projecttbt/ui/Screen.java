package lorganisation.projecttbt.ui;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import lorganisation.projecttbt.utils.CyclicList;

import java.util.List;

public abstract class Screen {

    protected CyclicList<Widget> components;
    protected Widget focus = null;
    protected KeyStroke focusKey = new KeyStroke(KeyType.Tab); //TAB by default

    public Screen() {

        components = new CyclicList<>();
    }

    public void disableFocus() {

        this.focus = null;
    }

    public void enableFocus() {

        setFocused(components.current());
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

    public void keyPressed(KeyStroke key) {

        if (key.equals(focusKey) && getFocusedWidget() != null) {

            //getFocusedWidget().onFocusLost();

            nextFocused();

            //getFocusedWidget().onFocused();

        } else {

            //first execute InvisibleButtons & unFocusable Buttons (require no focus and has priority)
            for (Widget widget : components)
                if (widget instanceof Button || widget instanceof InvisibleButton) {
                    if (!(widget instanceof Button && widget.isFocusable()) && !(widget instanceof InvisibleButton && !widget.isActivated()) && widget.handleEvent(key)) {
                        widget.setVisible(false);
                        return;
                    }
                }

            // if a widget is focused
            if (getFocusedWidget() != null)
                getFocusedWidget().handleEvent(key); // let it handle the keyPressed event
        }
    }
}
