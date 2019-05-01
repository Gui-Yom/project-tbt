package lorganisation.projectrpg.map;

import lorganisation.projectrpg.Coords;

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
    }

}
