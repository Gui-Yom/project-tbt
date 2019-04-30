package lorganisation.projectrpg.player;

import com.limelion.anscapes.Anscapes;
import lorganisation.projectrpg.Game;

public class Player extends AbstractPlayer {

    public Player(String name, Anscapes.Colors c) {

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
