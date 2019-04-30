package lorganisation.projectrpg.player;

import com.limelion.anscapes.Anscapes;
import lorganisation.projectrpg.AssetsManager;
import lorganisation.projectrpg.Game;
import lorganisation.projectrpg.Utils;

import java.util.List;

public class Bot extends AbstractPlayer {

    public Bot(List<Anscapes.Colors> availableColors) {

        // TODO remove bot name from list
        super("[BOT] " + Utils.pickRandom(AssetsManager.botNames()), Utils.pickRandomAndRemove(availableColors));
    }

    public Bot(String name, Anscapes.Colors color) {

        super(name, color);
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
