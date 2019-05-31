package lorganisation.projecttbt.utils;

/**
 * Une classe contenant des coordonnées (x,y) mutables.
 */
public class Coords extends Pair<Integer, Integer> {

    /**
     * Creates Coords object from x and y coordinates
     *
     * @param x (horizontal) coordinates (where 0 is left)
     * @param y (vertical) coordinates (where 0 is top)
     */
    public Coords(int x, int y) {

        super(x, y);
    }

    /**
     * Get x coordinate
     *
     * @return x coordinate
     */
    public int getX() {

        return getU();
    }

    /**
     * Set x coordinate
     *
     * @param x horizontal coordinate
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
     * @return toString format -> (x, y)
     */
    @Override
    public String toString() {

        return "(" + u + ", " + v + ")";
    }
}
