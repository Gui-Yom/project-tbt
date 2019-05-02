package lorganisation.projectrpg.player;

import com.limelion.anscapes.Anscapes;
import lorganisation.projectrpg.Game;
import lorganisation.projectrpg.utils.CyclicList;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPlayer {

    protected Anscapes.Colors playerColor;
    protected CyclicList<Character> characters;
    protected String name;

    public AbstractPlayer(String name, Anscapes.Colors c) {

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

    public Anscapes.Colors getColor() {

        return playerColor;
    }

    public void setColor(Anscapes.Colors color) {

        this.playerColor = color;
    }

    public boolean hasCharacter(String type) {
        for(Character character : characters.asList())
            if(character.getType().equalsIgnoreCase(type))
                return true;
        return false;
    }

}
