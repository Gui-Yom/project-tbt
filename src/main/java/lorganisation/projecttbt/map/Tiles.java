package lorganisation.projecttbt.map;

import com.limelion.anscapes.Anscapes;

import static com.limelion.anscapes.Anscapes.Colors;

/**
 * Représente une case d'une map. Le joueur peut ou non la traverser. TODO changer vers une vraie classe Tiles,
 * permettant une customisation approfondie de chaque Tiles
 */
public enum Tiles implements Tile {

    BLANK(Colors.WHITE.bg() + " " + Anscapes.RESET, true),
    ROCK(Colors.WHITE_BRIGHT.bg() + " " + Anscapes.RESET, false),
    GRASS(Colors.GREEN.bg() + " " + Anscapes.RESET, true),
    WATER(Colors.BLUE_BRIGHT.bg() + " " + Anscapes.RESET, false);

    private String icon;
    private boolean canStepOn;

    Tiles(String icon, boolean canStepOn) {

        this.icon = icon;
        this.canStepOn = canStepOn;
    }

    @Override
    public String toString() {

        return "Tiles{" +
               "icon=" + icon +
               ", canStepOn=" + canStepOn +
               '}';
    }

    public boolean canStepOn() {

        return canStepOn;
    }

    public String getIcon() {

        return icon;
    }

}
