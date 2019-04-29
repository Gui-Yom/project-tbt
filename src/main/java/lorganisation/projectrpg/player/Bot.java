package lorganisation.projectrpg.player;

import com.limelion.anscapes.AnsiColors;
import lorganisation.projectrpg.Game;

public class Bot extends AbstractPlayer {

    public Bot() {

        super();
    }

    public Bot(AnsiColors c) {

        super(c);
    }

    @Override
    public Action play(Game game) {

        return Action.DO_NOTHING;
    }

    @Override
    public boolean isBot() {

        return true;
    }
}
