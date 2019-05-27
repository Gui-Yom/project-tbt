package lorganisation.projecttbt.utils;

import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.Character;

import java.util.*;

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

    public static String repeatString(String str, int n) {

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++)
            result.append(str);

        return result.toString();
    }

    /**
     * @param coords1
     * @param coords2
     *
     * @return the orthogonal 2d distance between those 2 points
     */
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

    /**
     * @param coll a collection of integer
     *
     * @return the maximum integer contained in the given collection
     */
    public static int max(Iterable<Integer> coll) {

        Integer max = null;

        for (Integer i : coll)
            if (max == null || i > max)
                max = i;

        return max != null ? max : 0;
    }

    public static int findLongestSequence(Iterable<? extends CharSequence> list) {

        Integer max = null;

        for (CharSequence i : list)
            if (max == null || i.length() > max)
                max = i.length();

        return max != null ? max : 0;
    }

    public enum Align {
        LEFT, CENTER, RIGHT
    }
}


