package lorganisation.projectrpg.map;

import com.limelion.anscapes.*;
import com.limelion.anscapes.AnsiColors.ColorBG;

/**
 * Représente une case d'une map. Le joueur peut ou non la traverser. TODO changer vers une vraie classe Tile,
 * permettant une customisation approfondie de chaque Tile
 */
public enum Tile {

    // The value field must be unique

    BLANK(0, ColorBG.BG_BLACK + " ", true),
    ROCK(1, "", false),
    GRASS(2, "", true),
    WATER(3, "", false);

    private int value;
    private String icon;
    private boolean canStepOn;

    Tile(int value, String icon, boolean canStepOn) {

        this.value = value;
        this.icon = icon;
        this.canStepOn = canStepOn;
    }

    @Override
    public String toString() {

        return "Tile{" +
               "value=" + value +
               ", icon=" + icon +
               ", canStepOn=" + canStepOn +
               '}';
    }

    public int getValue() {

        return value;
    }

    public boolean canStepOn() {

        return canStepOn;
    }

    public char getIcon() {

        return icon;
    }

    public Tile valueOf(int value) {

        switch (value) {

            case 0:
                return BLANK;
            case 1:
                return ROCK;
            case 2:
                return GRASS;
            case 3:
                return WATER;

            default:
                return null;
        }
    }

}
