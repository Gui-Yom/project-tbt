package lorganisation.projecttbt.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Fonctionne comme une liste normale, mais permet en plus de pouvoir cycler à l'infini sur les éléments.
 *
 * @param <E>
 */
public class CyclicList<E> extends ArrayList<E> {

    private int index = -1;

    public CyclicList() {

        super();
    }

    public CyclicList(Collection<E> coll) {

        super(coll);
    }

    public CyclicList(E[] arr) {

        this(Arrays.asList(arr));
    }

    public E current() {

        if (index() == -1) return next();

        return get(index);
    }

    public int index() {

        return index;
    }

    public void reset() {

        index = -1;
    }

    public E prev() {

        return get((index <= 0) ? (index = size() - 1) : --index);
    }

    public E next() {

        return get((index >= size() - 1) ? (index = 0) : ++index);
    }

    public void setAt(int i) {

        if (i >= 0 && i < size())
            this.index = i;
    }
}
