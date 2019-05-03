package lorganisation.projecttbt.player;

import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.AnsiColor;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.utils.CyclicList;

public abstract class AbstractPlayer {

    protected AnsiColor playerColor;
    protected CyclicList<Character> characters;
    protected String name;

    public AbstractPlayer(String name, AnsiColor c) {

        this.name = name;
        this.playerColor = c;
        this.characters = new CyclicList<>();
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

    public String getName() {

        return this.name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void addCharacter(Character c) {

        characters.add(c);
    }

    public CyclicList<Character> getCharacters() {

        return characters;
    }

    public AnsiColor getColor() {

        return playerColor;
    }

    public void setColor(Anscapes.Colors color) {

        this.playerColor = color;
    }

    public boolean hasCharacter(String type) {

        for (Character character : characters)
            if (character.getType().equalsIgnoreCase(type))
                return true;
        return false;
    }

}
