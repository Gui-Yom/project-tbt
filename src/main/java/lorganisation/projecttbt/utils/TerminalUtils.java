package lorganisation.projecttbt.utils;

import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.AnsiColor;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;

import java.util.function.Function;

public class TerminalUtils {

    /**
     * Ask the user to resize the terminal, blocks until resized.
     *
     * @param terminal    the terminal
     * @param desiredSize the target size
     */
    public static void askForResize(Terminal terminal, Size desiredSize) {

        Size lastSize = terminal.getSize();
        int x;
        int y;

        while (!lastSize.equals(desiredSize)) {

            clearTerm();

            lastSize = terminal.getSize();

            //x = lastSize.getColumns() / 2 - 3;
            y = lastSize.getRows() / 2;

            writeAt(0, 0, new StyledString("   ", Pair.of(0, Anscapes.Colors.CYAN_BRIGHT.bg())).toString());
            writeAt(lastSize.getColumns() - 3, lastSize.getRows(), new StyledString("   ", Pair.of(0, Anscapes.Colors.CYAN_BRIGHT.bg())).toString());

            writeFormattedLine(y, 0,
                               String.format("Merci de redimensionner la fenêtre à cette taille : (col: %d, row: %d)   ", desiredSize.getColumns(), desiredSize.getRows()),
                               Utils.Align.CENTER,
                               lastSize.getColumns());

            writeFormattedLine(y + 1, 0,
                               String.format("Actuel : (col: %d, row: %d)   ", lastSize.getColumns(), lastSize.getRows()),
                               Utils.Align.CENTER,
                               lastSize.getColumns());

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        clearTerm();
    }

    public static void clearTerm() {

        System.out.print(Anscapes.CLEAR);
        System.out.print(Anscapes.cursorPos(1, 1));
    }

    /**
     * Ecrit un String à une position donnée
     *
     * @param x
     * @param y
     * @param s
     */
    public static void writeAt(int x, int y, String s) {

        // La première case sur un terminal est (1,1)
        // On décale pour pouvoir mettre (0,0)
        // On inverse x et y, car la convention est ligne, colonne
        System.out.print(Anscapes.cursorPos(y + 1, x + 1));
        System.out.print(s + Anscapes.RESET);
        System.out.print(Anscapes.cursorPos(y + 1, x + 1)); // On refout le curseur là où on a écrit pour avoir le blinking sur cette case et pas celle d'après
    }

    public static String makeBox(Coords coords, Size boxSize, Utils.Align alignement, Size terminalSize, AnsiColor borderColor, AnsiColor backgroundColor, StyledString title) {

        if (title.length() > boxSize.getColumns())
            title.setText(title.subSequence(0, boxSize.getRows() - 1) + ".");

        coords = coordinatesOfAlignedObject(coords.getY(), coords.getX(), boxSize.getColumns(), alignement, terminalSize.getColumns());

        Coords titlePos = new Coords(coords.getX() + (boxSize.getColumns() - title.length()) / 2, coords.getY());

        StringBuilder box = new StringBuilder();
        box.append(Anscapes.cursorPos(coords.getY(), coords.getX()))
           .append(borderColor.bg())
           .append(Utils.repeatString(" ", titlePos.getX() - coords.getX()))
           .append(title.toString())
           .append(Utils.repeatString(" ", coords.getX() + boxSize.getColumns() - titlePos.getX() - title.length()));

        for (int i = 0; i < boxSize.getRows() - 2; i++) {
            box.append(Anscapes.cursorPos(coords.getY() + i + 1, coords.getX()))
               .append(" ").append(backgroundColor.bg()).append(Utils.repeatString(" ", boxSize.getColumns() - 2))
               .append(borderColor.bg()).append(" ");
        }

        box.append(Anscapes.cursorPos(coords.getY() + boxSize.getRows() - 1, coords.getX()))
           .append(Utils.repeatString(" ", boxSize.getColumns()));

        return box.append(Anscapes.RESET).toString();
    }

    /**
     * Ecrit une ligne, avec alignement
     */
    public static void writeFormattedLine(int row, int col, String s, Utils.Align alignment, int width) {

        System.out.println(formattedLine(row, col, s, alignment, width) + Anscapes.RESET);
    }

    public static void writeFormattedLine(int row, int col, StyledString s, Utils.Align alignment, int width) {

        writeFormattedLine(row, col, s, alignment, width);
    }

    public static String formattedLine(int row, int col, StyledString s, Utils.Align alignment, int width) {

        Coords coords = coordinatesOfAlignedObject(row, col, s.length(), alignment, width);

        String result = Anscapes.cursorPos(coords.getY() + 1, coords.getX() + 1); // Aller à la ligne n

        result += s;

        // Ne peut pas return formattedLine(...), car ici s.length() donne length() du rawText sans caractères Ansi
        return result;
    }

    public static String formattedLine(int row, int col, String s, Utils.Align alignment, int width) {

        Coords coords = coordinatesOfAlignedObject(row, col, s.length(), alignment, width);

        String result = Anscapes.cursorPos(coords.getY() + 1, coords.getX() + 1); // Aller à la ligne n

        result += s;

        return result;
    }

    public static Coords coordinatesOfAlignedObject(int row, int col, int length, Utils.Align alignment, int width) {

        int x;
        if (alignment == Utils.Align.LEFT)
            x = col;
        else if (alignment == Utils.Align.RIGHT)
            x = width - length - col;
        else
            x = (width - length) / 2 + col;

        return new Coords(x, row);
    }

    /**
     * Lit un entier correct.
     *
     * @param term
     * @param prompt
     */
    public static int promptReadInt(Terminal term, String prompt, int def, Function<Integer, Boolean> filter) {

        if (!filter.apply(def))
            throw new IllegalArgumentException("Default int does not even match filter.");

        LineReader reader = LineReaderBuilder.builder()
                                             .terminal(term)
                                             .build();

        while (true) {
            try {
                String line = reader.readLine(prompt);

                if (line.equals(""))
                    return def;

                int n = Integer.parseInt(line);

                if (filter.apply(n))
                    return n;

            } catch (NumberFormatException nfe) {
                System.out.print(Anscapes.CLEAR_LINE);
            }
        }
    }
}
