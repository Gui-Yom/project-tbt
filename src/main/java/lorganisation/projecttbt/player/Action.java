package lorganisation.projecttbt.player;

public enum Action {

    MOVE_PASS,
    MOVE_LEFT,
    MOVE_RIGHT,
    MOVE_UP,
    MOVE_DOWN,
    CAST_ATTACK,
    DO_NOTHING;

    public String toString() {

        return this.name();
    }
}
