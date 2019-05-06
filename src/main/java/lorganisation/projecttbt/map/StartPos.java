package lorganisation.projecttbt.map;

import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.utils.Coords;

public class StartPos extends Coords {

    Character character;

    public StartPos(int x, int y, Character character) {

        super(x, y);

        this.character = character;
    }

    public StartPos(int x, int y) {

        super(x, y);

        this.character = null;
    }


    public Character getCharacter() {

        return this.character;
    }

    public void setCharacter(Character c) {

        this.character = c;
        c.setX(this.getU());
        c.setY(this.getV());
    }

}
