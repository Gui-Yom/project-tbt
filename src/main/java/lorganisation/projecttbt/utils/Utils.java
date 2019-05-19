package lorganisation.projecttbt.utils;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.Character;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

import java.util.*;
import java.util.function.Function;

/**
 * Une classe comportant différentes méthodes utilitaires.
 */
public class Utils {

    public static <T> List<T> arrayToList(T[] arr) {

        return new ArrayList<>(Arrays.asList(arr));
    }

    public static <U, V> Map<U, V> pairArrayToMap(Pair<U, V>[] pairs) {

        Map<U, V> map = new HashMap<>();

        for (Pair<U, V> p : pairs)
            map.put(p.getU(), p.getV());

        return map;
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

    /**
     * Ecrit une ligne, avec alignement
     */
    public static void writeFormattedLine(int row, int col, StyledString s, Align alignment, int width) {

        System.out.println(formattedLine(row, col, s, alignment, width) + Anscapes.RESET);
    }

    /**
     * @param row
     * @param col
     * @param s
     * @param alignment
     * @param width
     *
     * @return
     */
    public static String formattedLine(int row, int col, StyledString s, Align alignment, int width) {

        Coords coords = coordinatesOfAlignedObject(row, col, s.length(), alignment, width);

        String result = Anscapes.cursorPos(coords.getY() + 1, coords.getX() + 1); // Aller à la ligne n

        result += s;

        return result;
    }

    public static Coords coordinatesOfAlignedObject(int row, int col, int length, Align alignment, int width) {

        int x;
        if (alignment == Align.LEFT)
            x = col;
        else if (alignment == Align.RIGHT)
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

    /**
     * Cette méthode gère les signaux.
     *
     * @param sig le signal à gérer
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

    public static int distance(Coords coords1, Coords coords2) {

        return (int) Math.sqrt(Math.pow(coords1.getX() - coords2.getX(), 2) + Math.pow(coords1.getY() - coords2.getY(), 2));
    }

    public static AbstractPlayer getCharacterOwner(List<AbstractPlayer> players, Character character) {

        for (AbstractPlayer player : players) {
            if (player.getCharacters().contains(character))
                return player;
        }

        return null;
    }

    public static int max(Collection<Integer> coll) {

        Integer max = null;

        for (Integer i : coll)
            if (max == null || i > max)
                max = i;

        return max != null ? max : 0;
    }

    public enum Align {
        LEFT, CENTER, RIGHT
    }
}
