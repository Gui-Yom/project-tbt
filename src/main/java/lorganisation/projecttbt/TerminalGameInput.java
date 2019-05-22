package lorganisation.projecttbt;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class TerminalGameInput {

    private Terminal terminal;
    private KeyStroke lastKey;

    //Permet de lire et d'interpréter les entrées clavier
    private KeyMap<KeyStroke> keys;
    private BindingReader br;

    public TerminalGameInput(Terminal terminal) {

        this.terminal = terminal;
        this.lastKey = null;
        this.br = new BindingReader(terminal.reader());

        this.keys = new KeyMap<>();

        // Enregistre les touches du clavier
        for (char c = 32; c < 256; ++c)
            if (c != 157)
                keys.bind(KeyStroke.getKeyStroke(c), Character.toString(c));

        // Ici les touches spéciales (TAB, BACKSPACE etc...)
        keys.bind(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), KeyMap.esc());
        keys.bind(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "\r");
        keys.bind(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "\t");
        keys.bind(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "\b");

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

    public int readRawInput() {

        return br.readCharacter();
    }

    public KeyStroke getLastKey() {

        return lastKey;
    }

    public Terminal getTerminal() {

        return terminal;
    }
}
