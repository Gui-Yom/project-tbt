package lorganisation.projecttbt.player;

import com.limelion.anscapes.AnsiColor;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.map.LevelMap;
import lorganisation.projecttbt.utils.Coords;

import javax.swing.KeyStroke;

/**
 * Un vrai joueur du jeu
 */
public class Player extends AbstractPlayer {

    public Player(String name, AnsiColor c) {

        super(name, c);
    }

    @Override
    public ActionType play(Game game, Character character) {

        KeyStroke key = game.getInput().readKey();

        ActionType actionType = ActionType.DO_NOTHING;

        int actionPoints = character.getActionPoints();

        if (key != null) {

            if (isMoveKey(key)) {
                if ((status == AbstractPlayer.Status.IDLE ||
                     status == AbstractPlayer.Status.SILENCED) &&
                    actionPoints > 0
                    || status == AbstractPlayer.Status.CASTING_ATTACK) {

                    actionType = ActionType.parseFromKey(key);
                }
            } else if (key.equals(KeyStroke.getKeyStroke('a'))) { // is attack key ?

                if ((status == AbstractPlayer.Status.IDLE && actionPoints >= character.attacks.current().getCost())
                    || status == AbstractPlayer.Status.CASTING_ATTACK) {
                    actionType = ActionType.CAST_ATTACK;

                    if (status != Status.CASTING_ATTACK)
                        character.resetAim();
                }
            }
        }


        LevelMap map = game.getMap();
        Coords target = character.getAimingAt();

        switch (actionType) {

            case MOVE_LEFT: {

                Coords dest = new Coords(character.pos.getX() - 1, character.pos.getY());

                if (status == Status.CASTING_ATTACK) {

                    if (map.isInBounds(target.getX() - 1, target.getY()))
                        character.aimingAt.decX();

                } else if (map.canCollide(dest) && game.isTileFree(dest)) {
                    character.pos.decX();
                    --character.actionPoints;
                }
                break;
            }
            case MOVE_RIGHT: {
                Coords dest = new Coords(character.pos.getX() + 1, character.pos.getY());

                if (status == Status.CASTING_ATTACK) {

                    if (map.isInBounds(target.getX() + 1, target.getY()))
                        character.aimingAt.incX();

                } else if (map.canCollide(dest) && game.isTileFree(dest)) {
                    character.pos.incX();
                    --character.actionPoints;
                }
                break;
            }
            case MOVE_UP: {
                Coords dest = new Coords(character.pos.getX(), character.pos.getY() - 1);

                if (status == Status.CASTING_ATTACK) {

                    if (map.isInBounds(target.getX(), target.getY() - 1))
                        character.aimingAt.decY();

                } else if (map.canCollide(dest) && game.isTileFree(dest)) {
                    character.pos.decY();
                    --character.actionPoints;
                }

                break;
            }
            case MOVE_DOWN: {
                Coords dest = new Coords(character.pos.getX(), character.pos.getY() + 1);

                if (status == Status.CASTING_ATTACK) {

                    if (map.isInBounds(target.getX(), target.getY() + 1))
                        character.aimingAt.incY();

                } else if (map.canCollide(dest) && game.isTileFree(dest)) {
                    character.pos.incY();
                    --character.actionPoints;
                }
                break;
            }
            case CAST_ATTACK: {
                if (status == Status.CASTING_ATTACK) {
                    /* cast attack */
                    if (character.getAttacks().current().use(game, character, character.getAimingAt()))
                        character.getOwner().setStatus(Status.IDLE);
                    else
                        actionType = ActionType.DO_NOTHING;

                } else {
                    /* select attack */
                    character.getOwner().setStatus(Status.CASTING_ATTACK);
                }
                break;
            }
            case DO_NOTHING:
            default:
                break;
        }

        return actionType;
    }

    @Override
    public boolean isBot() {

        return false;
    }

    /**
     * @param key la touche
     *
     * @return true si key est une touche de mouvement
     */
    private boolean isMoveKey(KeyStroke key) {

        if (key == null)
            return false;

        return key.getKeyChar() == 'z' ||
               key.getKeyChar() == 'q' ||
               key.getKeyChar() == 's' ||
               key.getKeyChar() == 'd';
    }
}
