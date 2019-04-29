package lorganisation.projectrpg.player;

import com.limelion.anscapes.AnsiColors;
import lorganisation.projectrpg.Game;

public class Bot extends AbstractPlayer {

    public Bot() {

        super();
        setName("BOT"); // Récupérer nombre de BOTs et ajouter son numéro à la suite (e.g.: BOT 1, BOT 2, etc...
    }

    public Bot(AnsiColors c) {

        super(c);
        setName("BOT");
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
