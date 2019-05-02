package lorganisation.projectrpg.utils;

import java.util.ArrayList;
import java.util.List;

public class CyclicList<E> {

    private List<E> list;
    private int last = -1;

    public CyclicList () {
        this.list = new ArrayList<E>();
    }
    public CyclicList (ArrayList<E> list) {
        this.list = list;
    }

    public List<E> asList() {
        return this.list;
    }

    public int size() {
        return this.list.size();
    }

    public void add(E obj) {
        this.list.add(obj);
    }

    public int getLastIndex() {
        return this.last;
    }

    public void reset() {
        this.last = 0;
    }

    public E next() {
        return list.get((this.last == this.list.size() - 1) ? (this.last = 0) : ++last);
    }
}
