package lorganisation.projecttbt.map;

/**
 * Représente une case d'une carte
 */
public interface Tile {

    boolean canStepOn();

    String getIcon();
}
