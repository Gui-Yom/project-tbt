package lorganisation.projecttbt.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Fonctionne comme une liste normale, mais permet en plus de pouvoir cycler à l'infini sur les éléments, dans les deux sens
 *
 * @param <E>
 */
public class CyclicList<E> extends ArrayList<E> {

    /**
     * Current position of 'iterator' in list
     */
    private int index = 0;

    /**
     * Constructor used to make an empty CyclicList
     */
    public CyclicList() {

        super();
    }

    /**
     * Constructor used to make CyclicList from a collection
     * Default index = 0
     *
     * @param coll base collection
     */
    public CyclicList(Collection<E> coll) {

        super(coll);
        index = 0;
    }

    /**
     * Constructor used to make CyclicList from an array
     * Size isn't fixed. Default index = 0
     *
     * @param arr base array
     */
    public CyclicList(E[] arr) {

        this(Arrays.asList(arr));
    }

    /**
     * Get current item in list without changing index
     *
     * @return current item in list
     */
    public E current() {

        return get(index);
    }


    /**
     * Get current index value
     *
     * @return current index value
     */
    public int getIndex() {

        return index;
    }

    /**
     * Set index to i
     *
     * @param i index
     */
    public void setIndex(int i) {

        if (i >= 0 && i < size())
            this.index = i;
    }


    /**
     * Reset index to 0
     */
    public void reset() {

        index = 0;
    }

    /**
     * Get previous item and decrement index
     */
    public E prev() {

        return get(index = ((index + size() - 1) % size()));
    }

    /**
     * Get next item and increment index
     */
    public E next() {

        return get(index = ((index + 1) % size()));
    }

    /**
     * Get next item without changing current index value
     *
     * @return next item in list
     */
    public E getNext() {

        return get((index + 1) % size());
    }

    /**
     * Get previous item without changing current index value
     *
     * @return previous item in list
     */
    public E getPrev() {

        return get((index + size() - 1) % size());
    }
}
