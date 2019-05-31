package lorganisation.projecttbt.ui.screen;

import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.ui.widget.ActionWidget;
import lorganisation.projecttbt.ui.widget.Widget;
import lorganisation.projecttbt.utils.CyclicList;
import lorganisation.projecttbt.utils.KeyUtils;
import org.jline.terminal.Terminal;

import javax.swing.KeyStroke;
import java.util.List;

/**
 * Un écran, contient des composants avec lesquels l'utilisateur interagit
 */
public abstract class Screen {

    protected CyclicList<Widget> components;
    protected Widget focus = null;
    protected KeyStroke focusKey = KeyUtils.KEY_TAB;

    public Screen(Terminal terminal) {

        components = new CyclicList<>();
    }

    public void disableFocus() {

        this.focus = null;
    }

    public void enableFocus() {

        setFocused(components.current());
    }

    public Widget setFocused(Widget widget) {

        if (widget.isFocusable() && widget.isVisible() && components.contains(widget) && widget.isEnabled()) {
            widget.onFocus();
            components.setIndex(components.indexOf(widget));
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

    public Widget nextFocus() {

        int i = components.size();
        while (setFocused(components.next()) == null) {
            if (i-- <= 0)
                return null;
        }

        return getFocusedWidget();
    }

    public Widget prevFocus() {

        int i = components.size();
        while (setFocused(components.prev()) == null) {
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

        if (key.getKeyCode() == focusKey.getKeyCode() && getFocusedWidget() != null) {

            getFocusedWidget().onFocusLost();

            nextFocus();

        } else {

            // Les buttons peuvent avoir un scope global et ont la priorité sur le focus (et les inputs)
            for (Widget widget : components) {
                if (widget instanceof ActionWidget && !widget.isFocusable() && widget.isEnabled() && widget.handleInput(key)) {
                    return;
                }
            }

            // if a widget is focused
            if (getFocusedWidget() != null)
                getFocusedWidget().handleInput(key); // let it handle the keyPressed event
        }
    }

    public abstract void display(TerminalGameInput input, TerminalGameRenderer renderer);
}
