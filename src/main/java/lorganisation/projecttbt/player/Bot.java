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

    public static boolean isWorking() {

        return false;
    }

    public void pickCharacters(List<String> characterNames, int quantity) {

        while (characters.size() < quantity) {

            String name;
            do {
                name = Utils.getRandom(characterNames);

                // Une variable effectivement-finale est nécessaire pour un lambda
                final String finalName = name;
                if (!Utils.match(characters, c -> c.type.equals(finalName)))
                    break;

            } while (true);

            characters.add(new Character(CharacterTemplate.getCharacterTemplate(name), this));
        }
    }

    @Override
    public ActionType play(Game game, Character character) {

        return ActionType.DO_NOTHING;
    }

    @Override
    public boolean isBot() {

        return true;
    }
}
