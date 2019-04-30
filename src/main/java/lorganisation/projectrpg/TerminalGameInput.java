package lorganisation.projectrpg;

import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;

import java.io.IOException;

public class TerminalGameInput extends GameInput {

    private Terminal terminal;

    public TerminalGameInput(Terminal terminal) {

        super();
        this.terminal = terminal;
    }

    @Override
    public int getInput() {

        try {
            return terminal.reader().read();
        } catch (IOException e) {
            System.err.println("Error while trying to read data from terminal : " + e.getLocalizedMessage());
        }
        return -1;
    }

    public String promptLine(LineReader reader, String prompt) {

        return reader.readLine(prompt);
    }
}
