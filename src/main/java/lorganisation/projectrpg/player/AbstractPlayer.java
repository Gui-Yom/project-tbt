package lorganisation.projectrpg.player;

import com.limelion.anscapes.AnsiColors;
import lorganisation.projectrpg.Game;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPlayer {

    protected AnsiColors playerColor;
    protected List<Character> characters;
    protected String name;

    public AbstractPlayer() {

        this(AnsiColors.ColorFG.FG_BLUE_BRIGHT);
    }

    public AbstractPlayer(AnsiColors c) {

        this.playerColor = c;
        this.characters = new ArrayList<>();
    }

    public abstract Action play(Game game);

    public abstract boolean isBot();

    @Override
    public String toString() {

        return "AbstractPlayer{" +
               "name=" + name +
               "playerColor=" + playerColor +
               ", characters=" + characters +
               '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void addCharacter(Character c) {

        characters.add(c);
    }

    public List<Character> getCharacters() {

        return characters;
    }

    public AnsiColors getColor() {

        return playerColor;
    }

    public void setColor(AnsiColors color) {

        this.playerColor = color;
    }

}
