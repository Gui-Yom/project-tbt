package lorganisation.projecttbt.experiments;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.WindowsTerminal;

import java.io.IOException;

public class LanternaIntegration {

    public static void main(String[] args) {

        try {
            WindowsTerminal terminal = new WindowsTerminal();
            terminal.enterPrivateMode();
            terminal.clearScreen();
            terminal.setCursorVisible(false);
            TextGraphics text = terminal.newTextGraphics();
            text.setForegroundColor(TextColor.ANSI.WHITE);
            text.setBackgroundColor(TextColor.ANSI.BLACK);
            text.putString(2, 1, "Lanterna Tutorial 2 - Press ESC to exit", SGR.BOLD);
            text.setForegroundColor(TextColor.ANSI.DEFAULT);
            text.setBackgroundColor(TextColor.ANSI.DEFAULT);
            text.putString(5, 3, "Terminal Size: ", SGR.BOLD);
            text.putString(5 + "Terminal Size: ".length(), 3, terminal.getTerminalSize().toString());
            terminal.flush();

            terminal.addResizeListener((terminal1, newSize) -> {
                // Be careful here though, this is likely running on a separate thread. Lanterna is threadsafe in
                // a best-effort way so while it shouldn't blow up if you call terminal methods on multiple threads,
                // it might have unexpected behavior if you don't do any external synchronization
                text.drawLine(5, 3, newSize.getColumns() - 1, 3, ' ');
                text.putString(5, 3, "Terminal Size: ", SGR.BOLD);
                text.putString(5 + "Terminal Size: ".length(), 3, newSize.toString());
                try {
                    terminal1.flush();
                } catch (IOException e) {
                    // Not much we can do here
                    throw new RuntimeException(e);
                }
            });

            text.putString(5, 4, "Last Keystroke: ", SGR.BOLD);
            text.putString(5 + "Last Keystroke: ".length(), 4, "<Pending>");
            terminal.flush();

            KeyStroke keyStroke = terminal.readInput();
            while (keyStroke.getKeyType() != KeyType.Escape) {
                text.drawLine(5, 4, terminal.getTerminalSize().getColumns() - 1, 4, ' ');
                text.putString(5, 4, "Last Keystroke: ", SGR.BOLD);
                text.putString(5 + "Last Keystroke: ".length(), 4, keyStroke.toString());
                terminal.flush();
                keyStroke = terminal.readInput();
            }

            terminal.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
