package lorganisation.projectrpg;

import com.limelion.anscapes.Anscapes;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Utils {

    public static <T> T pickRandom(List<T> elements) {

        if (elements != null && elements.size() > 0)
            return elements.get((int) (Math.random() * elements.size()));
        return null;
    }

    public static <T> T pickRandomAndRemove(List<T> elements) {

        T element = pickRandom(elements);
        elements.remove(element);
        return element;
    }

    public static <T> List<T> arrayToList(T[] arr) {

        List<T> list = new ArrayList<>(arr.length);
        list.addAll(Arrays.asList(arr));
        return list;
    }

    public static void clearTerm() {

        System.out.print(Anscapes.CLEAR);
        System.out.print(Anscapes.cursorPos(0, 0));
    }

    public static void hideCursor() {

        System.out.print(Anscapes.CURSOR_HIDE);
    }

    public static void showCursor() {

        System.out.print(Anscapes.CURSOR_SHOW);
    }

    /**
     * Ecrit une ligne, avec alignement
     */
    public static void writeFormattedLine(int n, String s, String[] modifiers, boolean clear, Align alignment, int offset, int width) {

        int x;
        if (alignment == Align.LEFT)
            x = offset;
        else if (alignment == Align.RIGHT)
            x = width - s.length() - offset;
        else
            x = (width - s.length()) / 2 + offset;

        System.out.print(Anscapes.cursorPos(n, x)); // Aller à la ligne n

        if (clear)
            System.out.print(Anscapes.CLEAR_LINE);

        System.out.println(modifiers[0] + s + modifiers[1] + Anscapes.RESET);
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

    public static String readLine(Terminal term, String prompt) {

        return LineReaderBuilder.builder().terminal(term).build().readLine(prompt);
    }

    public enum Align {
        LEFT, CENTER, RIGHT
    }
}
