package lorganisation.projecttbt.utils;

/**
 * Une classe contenant des coordonnées (x,y) (mutable).
 */
public class Coords extends Pair<Integer, Integer> {

    /**
     * Crée un objet Coords à partir des coordonnées
     *
     * @param x (horizontal) coordinates (where 0 is left)
     * @param y (vertical) coordinates (where 0 is top)
     */
    public Coords(int x, int y) {

        super(x, y);
    }

    /**
     * @return la coordonnée x
     */
    public int getX() {

        return getU();
    }

    /**
     * Set x coordinate
     *
     * @param x la coordonnée x
     */
    public void setX(int x) {

        setU(x);
    }

    /**
     * Get y coordinate
     *
     * @return y coordinate
     */
    public int getY() {

        return getV();
    }

    /**
     * Set y coordinate
     *
     * @param y vertical coordinate
     */
    public void setY(int y) {

        setU(y);
    }

    public void incX() {

        ++u;
    }

    public void decX() {

        --u;
    }

    public void incY() {

        ++v;
    }

    public void decY() {

        --v;
    }

    /**
     * @return (x, y)
     */
    @Override
    public String toString() {

        return "(" + u + ", " + v + ")";
    }
}
