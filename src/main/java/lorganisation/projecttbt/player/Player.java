package lorganisation.projecttbt.player;

import com.limelion.anscapes.AnsiColor;
import lorganisation.projecttbt.Game;

public class Player extends AbstractPlayer {

    public Player(String name, AnsiColor c) {

        super(name, c);
    }

    @Override
    public Action play(Game game) {

        // TODO get player action
        return Action.DO_NOTHING;
    }

    @Override
    public boolean isBot() {

        return false;
    }
}
