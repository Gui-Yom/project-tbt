package lorganisation.projecttbt;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.ansi.ANSITerminal;

import java.io.IOException;

public class TerminalGameInput {

    private ANSITerminal terminal;

    public TerminalGameInput(ANSITerminal terminal) {

        super();
        this.terminal = terminal;
    }

    public KeyStroke getInput() {

        try {
            return terminal.readInput();
        } catch (IOException e) {
            System.err.println("Error while trying to read data from terminal : " + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }
}
