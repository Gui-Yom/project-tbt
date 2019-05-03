package lorganisation.projecttbt.utils;

import com.limelion.anscapes.Anscapes;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Une classe comportant différentes méthodes utilitaires.
 */
public class Utils {

    public static <T> List<T> arrToList(T[] arr) {

        return new ArrayList<>(Arrays.asList(arr));
    }

    /**
     * @param list
     * @param <T>
     *
     * @return un élément aléatoire de la liste
     */
    public static <T> T getRandom(List<T> list) {

        if (list != null && list.size() > 0)
            return list.get((int) (Math.random() * list.size()));
        return null;
    }

    /**
     * @param elements
     * @param <T>
     *
     * @return un élément aléatoire de la liste après l'avoir retiré.
     */
    public static <T> T pickRandom(List<T> elements) {

        if (elements != null && elements.size() > 0)
            return elements.remove((int) (Math.random() * elements.size()));
        return null;
    }

    public static void clearTerm() {

        System.out.print(Anscapes.CLEAR);
        System.out.print(Anscapes.cursorPos(1, 1));
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

        if (modifiers == null) modifiers = new String[] { "", "" };

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

    /**
     * Cette méthode gère les signaux.
     *
     * @param sig
     *     le signal à gérer
     *
     * @see <a href="https://en.wikipedia.org/wiki/Signal_(IPC)">Signaux, systèmes POSIX</a>
     */
    public static void handleSignal(Terminal.Signal sig) {

        switch (sig) {
            case INT:
                System.out.println("Received SIGINT !");
                System.exit(0);
                break;
            case QUIT:
                System.out.println("Received SIGQUIT !");
                break;
            case TSTP:
                System.out.println("Received SIGTSTP !");
                break;
            case CONT:
                System.out.println("Received SIGCONT !");
                break;
            case INFO:
                System.out.println("Received SIGINFO !");
                break;
            case WINCH:
                // On window resize
                System.out.println("Received SIGWINCH !");
                break;
        }
    }

    public static String readLine(Terminal term, String prompt) {

        return LineReaderBuilder.builder().terminal(term).build().readLine(prompt);
    }

    public enum Align {
        LEFT, CENTER, RIGHT
    }
}
