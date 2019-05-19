package lorganisation.projecttbt;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class TerminalGameInput {

    private Terminal terminal;
    private KeyStroke lastKey;
    private KeyMap<KeyStroke> keys;
    private BindingReader br;

    public TerminalGameInput(Terminal terminal) {

        this.terminal = terminal;
        this.lastKey = null;
        this.br = new BindingReader(terminal.reader());

        this.keys = new KeyMap<>();
        for (char c = 32; c < 256; ++c)
            if (c != 157)
                keys.bind(KeyStroke.getKeyStroke(c), Character.toString(c));

        keys.bind(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), KeyMap.esc());
        keys.bind(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), KeyMap.key(terminal, InfoCmp.Capability.tab));
        keys.setNomatch(null);
    }

    public int readInput() {

        try {
            return terminal.reader().read();
        } catch (IOException e) {
            System.err.println("Error while trying to read data from terminal : " + e.getLocalizedMessage());
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Bloque jusquà lecture d'une entrée.
     */
    public KeyStroke readKey() {

        return lastKey = br.readBinding(keys);
    }

    public KeyStroke getLastKey() {

        return lastKey;
    }
}
