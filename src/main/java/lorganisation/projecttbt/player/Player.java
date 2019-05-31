package lorganisation.projecttbt.player;

import com.limelion.anscapes.AnsiColor;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.map.LevelMap;
import lorganisation.projecttbt.utils.KeyUtils;

import javax.swing.KeyStroke;

public class Player extends AbstractPlayer {

    public Player(String name, AnsiColor c) {

        super(name, c);
    }

    @Override
    public Action play(Game game, Character character) {

        // TODO get player action

        KeyStroke key = game.getInput().readKey();

        Action action = Action.DO_NOTHING;
        int actionPoints = character.getActionPoints(); // FIXME needs to be character.getActionPoints() - usedActionPoints()

        if (isMoveKey(key)) {
            if ((status == AbstractPlayer.Status.IDLE || status == AbstractPlayer.Status.SILENCED) && actionPoints > 0 || status == AbstractPlayer.Status.CASTING_ATTACK) {
                if (key.equals(KeyStroke.getKeyStroke('q'))) action = Action.MOVE_LEFT;
                else if (key.equals(KeyStroke.getKeyStroke('d'))) action = Action.MOVE_RIGHT;
                else if (key.equals(KeyStroke.getKeyStroke('z'))) action = Action.MOVE_UP;
                else action = Action.MOVE_DOWN;
            }
        } else if (key.equals(KeyUtils.KEY_SPACE_BAR)) { // is attack key ?

            if ((status == AbstractPlayer.Status.IDLE && actionPoints > 0) || status == AbstractPlayer.Status.CASTING_ATTACK) { // TODO load attacks & attackCost
                action = Action.CAST_ATTACK;
            }
        }

        LevelMap map = game.getMap();
        switch (action) {

            case MOVE_LEFT: {
                if (map.canCollide(character.pos.getX() - 1, character.pos.getY())) {
                    character.pos.decX();
                    --character.actionPoints;
                }
                break;
            }
            case MOVE_RIGHT: {
                if (map.canCollide(character.pos.getX() + 1, character.pos.getY())) {
                    character.pos.incX();
                    --character.actionPoints;
                }
                break;
            }
            case MOVE_UP: {
                if (map.canCollide(character.pos.getX(), character.pos.getY() - 1)) {
                    character.pos.decY();
                    --character.actionPoints;
                }
                break;
            }
            case MOVE_DOWN: {
                if (map.canCollide(character.pos.getX(), character.pos.getY() + 1)) {
                    character.pos.incY();
                    --character.actionPoints;
                }
                break;
            }
            case CAST_ATTACK: {
                if (status == Status.CASTING_ATTACK) {
                    /* cast attack */
                } else {
                    /* select attack */
                }
                break;
            }
            case DO_NOTHING:
            default:
                break;
        }

        return action;
    }

    @Override
    public boolean isBot() {

        return false;
    }

    private boolean isMoveKey(KeyStroke key) {

        return (key.equals(KeyStroke.getKeyStroke('q')) || key.equals(KeyStroke.getKeyStroke('d')) || key.equals(KeyStroke.getKeyStroke('z')) || key.equals(KeyStroke.getKeyStroke('s')));
    }
}
