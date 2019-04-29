package lorganisation.projectrpg.experiments;

import com.limelion.anscapes.Anscapes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TerminalTest {

    public static void main(String[] args) throws IOException {

        Terminal terminal = TerminalBuilder.builder()
                                           .encoding(StandardCharsets.UTF_8)
                                           .jna(true)
                                           .system(true)
                                           .name("TerminalTest")
                                           .build();

        terminal.enterRawMode();

        for (; ; ) {

            terminal.writer().print("Press a key to test : ");
            char read = (char) terminal.reader().read();
            terminal.writer().print(read);
            Anscapes.moveLeft(100);
        }
    }

}
