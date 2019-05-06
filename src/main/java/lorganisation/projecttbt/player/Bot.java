package lorganisation.projecttbt.player;

import com.limelion.anscapes.AnsiColor;
import lorganisation.projecttbt.AssetsManager;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Bot extends AbstractPlayer {

    /**
     * Un pool des noms de bots disponibles.
     */
    private static final List<String> botNames = new ArrayList<>(AssetsManager.botNames());

    public Bot(List<? extends AnsiColor> availableColors) {

        super("[BOT] " + Utils.pickRandom(botNames), Utils.pickRandom(availableColors));
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
