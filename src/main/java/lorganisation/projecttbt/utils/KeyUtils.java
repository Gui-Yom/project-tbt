package lorganisation.projecttbt.utils;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

public class KeyUtils {

    public static final KeyStroke
        KEY_ENTER = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
        KEY_ESCAPE = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        KEY_TAB = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0),
        KEY_LEFT_ARROW = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
        KEY_RIGHT_ARROW = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
        KEY_UP_ARROW = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
        KEY_DOWN_ARROW = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);

    public static boolean hasModifier(KeyStroke key, int modifier) {

        return (key.getModifiers() & modifier) == modifier;
    }
}
