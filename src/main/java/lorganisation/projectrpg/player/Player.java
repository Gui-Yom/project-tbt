package lorganisation.projectrpg.player;

import com.limelion.anscapes.AnsiColors;
import lorganisation.projectrpg.Game;

public class Player extends AbstractPlayer {

    public Player() {

        super();
    }

    public Player(AnsiColors c) {

        super(c);
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
