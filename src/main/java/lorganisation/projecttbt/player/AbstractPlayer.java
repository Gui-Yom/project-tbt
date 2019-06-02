package lorganisation.projecttbt.player;

import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.AnsiColor;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.utils.CyclicList;

/**
 * Joueur abstrait (Joueur ou BOT / IA)
 */
public abstract class AbstractPlayer {

    protected AnsiColor playerColor;
    protected CyclicList<Character> characters;
    protected String name;
    protected Status status;

    public AbstractPlayer(String name, AnsiColor c) {

        this.name = name;
        this.playerColor = c;
        this.characters = new CyclicList<>();
        this.status = Status.IDLE;
    }

    public abstract ActionType play(Game game, Character character);

    public abstract boolean isBot();

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

    public Status getStatus() {

        return status;
    }

    public void setStatus(Status status) {

        this.status = status;
    }

    public enum Status {

        IDLE("va jouer"),
        CASTING_ATTACK("prépare son attaque"),
        STUNNED("est assommé"),
        SILENCED("est réduit au silence");

        String value;

        Status(String value) {

            this.value = value;
        }

        public String toString() {

            return value;
        }
    }

}
