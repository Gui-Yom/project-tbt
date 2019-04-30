package lorganisation.projectrpg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
}
