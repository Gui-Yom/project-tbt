package lorganisation.projecttbt.player;

import javax.swing.KeyStroke;

public enum ActionType {

    MOVE_PASS,
    MOVE_LEFT,
    MOVE_RIGHT,
    MOVE_UP,
    MOVE_DOWN,
    CAST_ATTACK,
    DO_NOTHING;

    public static ActionType parseFromKey(KeyStroke key) {

        if (key.getKeyChar() == 'z')
            return MOVE_UP;
        if (key.getKeyChar() == 'q')
            return MOVE_LEFT;
        if (key.getKeyChar() == 's')
            return MOVE_DOWN;
        if (key.getKeyChar() == 'd')
            return MOVE_RIGHT;
        if (key.getKeyChar() == ' ')
            return CAST_ATTACK;

        return DO_NOTHING;
    }

    public String toString() {

        return this.name();
    }
}
