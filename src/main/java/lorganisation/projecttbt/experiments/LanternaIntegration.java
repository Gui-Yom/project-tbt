package lorganisation.projecttbt.experiments;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import lorganisation.projecttbt.LanternaTerminal;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class LanternaIntegration {

    public static void main(String[] args) {

        try {

            Terminal term = TerminalBuilder.builder()
                                           .jansi(true)
                                           .system(true)
                                           .name("LanternaIntegration")
                                           .encoding(StandardCharsets.UTF_8)
                                           //.type("windows-vtp")
                                           .build();

            //term.enterRawMode();

            LanternaTerminal terminal = new LanternaTerminal(term);
            /*
            The DefaultTerminalFactory class doesn't provide any helper method for creating a TerminalLabel GUI, you'll need to
             get a Screen like we did in the previous tutorial and start it so it puts the terminal in private mode.
             */
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();

            /*
            Let's turn off the cursor for this tutorial
             */
            screen.setCursorPosition(null);

            /*
            Now let's draw some random content in the screen buffer
             */
            Random random = new Random();
            TerminalSize terminalSize = screen.getTerminalSize();
            for (int column = 0; column < terminalSize.getColumns(); column++) {
                for (int row = 0; row < terminalSize.getRows(); row++) {
                    screen.setCharacter(column, row, new TextCharacter(
                        ' ',
                        TextColor.ANSI.DEFAULT,
                        // This will pick a random background color
                        TextColor.ANSI.values()[random.nextInt(TextColor.ANSI.values().length)]));
                }
            }

            /*
            So at this point, we've only modified the back buffer in the screen, nothing is visible yet. In order to
            move the content from the back buffer to the front buffer and refresh the screen, we need to call refresh()
             */
            screen.refresh();

            /*
            Now there should be completely random colored cells in the terminal (assuming your terminal (emulator)
            supports colors). Let's look at it for two seconds or until the user press a key.
             */
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 2000) {
                // The call to pollInput() is not blocking, unlike readInput()
                if (screen.pollInput() != null) {
                    break;
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignore) {
                    break;
                }
            }

            /*
            Ok, now we loop and keep modifying the screen until the user exits by pressing escape on the keyboard or the
            input stream is closed. When using the Swing/AWT bundled emulator, if the user closes the window this will
            result in an EOF KeyStroke.
             */
            while (true) {
                KeyStroke keyStroke = screen.pollInput();
                if (keyStroke != null && (keyStroke.getKeyType() == KeyType.Escape || keyStroke.getKeyType() == KeyType.EOF)) {
                    break;
                }

                /*
                Screens will automatically listen and record size changes, but you have to let the Screen know when is
                a good time to update its internal buffers. Usually you should do this at the start of your "drawing"
                loop, if you have one. This ensures that the dimensions of the buffers stays constant and doesn't change
                while you are drawing content. The method doReizeIfNecessary() will check if the terminal has been
                resized since last time it was called (or since the screen was created if this is the first time
                calling) and update the buffer dimensions accordingly. It returns null if the terminal has not changed
                size since last time.
                 */
                TerminalSize newSize = screen.doResizeIfNecessary();
                if (newSize != null) {
                    terminalSize = newSize;
                }

                // Increase this to increase speed
                final int charactersToModifyPerLoop = 1;
                for (int i = 0; i < charactersToModifyPerLoop; i++) {
                    /*
                    We pick a random location
                     */
                    TerminalPosition cellToModify = new TerminalPosition(
                        random.nextInt(terminalSize.getColumns()),
                        random.nextInt(terminalSize.getRows()));

                    /*
                    Pick a random background color again
                     */
                    TextColor.ANSI color = TextColor.ANSI.values()[random.nextInt(TextColor.ANSI.values().length)];

                    /*
                    Update it in the back buffer, notice that just like TerminalPosition and TerminalSize, TextCharacter
                    objects are immutable so the withBackgroundColor(..) call below returns a copy with the background color
                    modified.
                     */
                    TextCharacter characterInBackBuffer = screen.getBackCharacter(cellToModify);
                    characterInBackBuffer = characterInBackBuffer.withBackgroundColor(color);
                    characterInBackBuffer = characterInBackBuffer.withCharacter(' ');   // Because of the label box further down, if it shrinks
                    screen.setCharacter(cellToModify, characterInBackBuffer);
                }

                /*
                Just like with Terminal, it's probably easier to draw using TextGraphics. Let's do that to put a little
                box with information on the size of the terminal window
                 */
                String sizeLabel = "Terminal Size: " + terminalSize;
                TerminalPosition labelBoxTopLeft = new TerminalPosition(1, 1);
                TerminalSize labelBoxSize = new TerminalSize(sizeLabel.length() + 2, 3);
                TerminalPosition labelBoxTopRightCorner = labelBoxTopLeft.withRelativeColumn(labelBoxSize.getColumns() - 1);
                TextGraphics textGraphics = screen.newTextGraphics();
                //This isn't really needed as we are overwriting everything below anyway, but just for demonstrative purpose
                textGraphics.fillRectangle(labelBoxTopLeft, labelBoxSize, ' ');

                /*
                Draw horizontal lines, first upper then lower
                 */
                textGraphics.drawLine(
                    labelBoxTopLeft.withRelativeColumn(1),
                    labelBoxTopLeft.withRelativeColumn(labelBoxSize.getColumns() - 2),
                    Symbols.DOUBLE_LINE_HORIZONTAL);
                textGraphics.drawLine(
                    labelBoxTopLeft.withRelativeRow(2).withRelativeColumn(1),
                    labelBoxTopLeft.withRelativeRow(2).withRelativeColumn(labelBoxSize.getColumns() - 2),
                    Symbols.DOUBLE_LINE_HORIZONTAL);

                /*
                Manually do the edges and (since it's only one) the vertical lines, first on the left then on the right
                 */
                textGraphics.setCharacter(labelBoxTopLeft, Symbols.DOUBLE_LINE_TOP_LEFT_CORNER);
                textGraphics.setCharacter(labelBoxTopLeft.withRelativeRow(1), Symbols.DOUBLE_LINE_VERTICAL);
                textGraphics.setCharacter(labelBoxTopLeft.withRelativeRow(2), Symbols.DOUBLE_LINE_BOTTOM_LEFT_CORNER);
                textGraphics.setCharacter(labelBoxTopRightCorner, Symbols.DOUBLE_LINE_TOP_RIGHT_CORNER);
                textGraphics.setCharacter(labelBoxTopRightCorner.withRelativeRow(1), Symbols.DOUBLE_LINE_VERTICAL);
                textGraphics.setCharacter(labelBoxTopRightCorner.withRelativeRow(2), Symbols.DOUBLE_LINE_BOTTOM_RIGHT_CORNER);

                /*
                Finally put the text inside the box
                 */
                textGraphics.putString(labelBoxTopLeft.withRelative(1, 1), sizeLabel);

                /*
                Ok, we are done and can display the change. Let's also be nice and allow the OS to schedule other
                threads so we don't clog up the core completely.
                 */
                screen.refresh();
                Thread.yield();

                /*
                Every time we call refresh, the whole terminal is NOT re-drawn. Instead, the Screen will compare the
                back and front buffers and figure out only the parts that have changed and only update those. This is
                why in the code drawing the size information box above, we formattedLine it out every time we loop but it's
                actually not sent to the terminal except for the first time because the Screen knows the content is
                already there and has not changed. Because of this, you should never use the underlying Terminal object
                when working with a Screen because that will cause modifications that the Screen won't know about.
                 */
            }

            screen.stopScreen();
            terminal.close();
            term.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
