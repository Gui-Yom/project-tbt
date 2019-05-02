package lorganisation.projectrpg.utils;

/**
 * Une classe contenant des coordonnées (x,y)
 */
public class Coords extends Pair<Integer, Integer> {

    public Coords(int x, int y) {

        super(x, y);
    }

    public int getX() {

        return getU();
    }

    public void setX(int x) {

        setU(x);
    }

    public int getY() {

        return getV();
    }

    public void setY(int y) {

        setY(y);
    }

    @Override
    public String toString() {

        return "(" + u + ", " + v + ")";
    }
}
