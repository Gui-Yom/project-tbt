package lorganisation.projectrpg.map;

import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.AnsiColors;

/**
 * Représente une case d'une map. Le joueur peut ou non la traverser. TODO changer vers une vraie classe Tiles,
 * permettant une customisation approfondie de chaque Tiles
 */
public enum Tiles implements Tile {

    // The value field must be unique

    BLANK(AnsiColors.ColorBG.BG_WHITE + " " + Anscapes.RESET, true),
    ROCK(AnsiColors.ColorBG.BG_WHITE_BRIGHT + "\u2588" + Anscapes.RESET, false),
    GRASS(AnsiColors.ColorBG.BG_GREEN + " " + Anscapes.RESET, true),
    WATER(AnsiColors.ColorFG.FG_BLUE_BRIGHT + "█" + Anscapes.RESET, false);

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
