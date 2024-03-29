package lorganisation.projecttbt;

import lorganisation.projecttbt.utils.KeyUtils;
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import javax.swing.KeyStroke;
import java.io.IOException;

public class TerminalGameInput {

    // Terminal duquel on reçoit les entrées
    private Terminal terminal;

    // Dernière entrée
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
        keys.bind(KeyUtils.KEY_ESCAPE, KeyMap.esc());
        keys.bind(KeyUtils.KEY_ENTER, "\r");
        keys.bind(KeyUtils.KEY_TAB, "\t");
        keys.bind(KeyUtils.KEY_BACKSPACE, "\b");
        keys.bind(KeyUtils.KEY_UP_ARROW, KeyMap.key(terminal, InfoCmp.Capability.key_up));
        keys.bind(KeyUtils.KEY_LEFT_ARROW, KeyMap.key(terminal, InfoCmp.Capability.key_left));
        keys.bind(KeyUtils.KEY_DOWN_ARROW, KeyMap.key(terminal, InfoCmp.Capability.key_down));
        keys.bind(KeyUtils.KEY_RIGHT_ARROW, KeyMap.key(terminal, InfoCmp.Capability.key_right));
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

    /**
     * Get last KeyStroke
     *
     * @return the last KeyStroke
     */
    public KeyStroke getLastKey() {

        return lastKey;
    }

    /**
     * Get terminal for the inputs
     *
     * @return terminal
     */
    public Terminal getTerminal() {

        return terminal;
    }
}
