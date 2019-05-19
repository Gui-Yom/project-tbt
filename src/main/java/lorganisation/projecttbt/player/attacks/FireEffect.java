package lorganisation.projecttbt.player.attacks;

import java.io.Serializable;

public class FireEffect extends Effect {

    public FireEffect(int strength, int duration) {

        this.duration = duration;
        this.function = (Runnable & Serializable) () -> this.owner.damage(Attack.DamageType.MAGIC, strength);
    }
}
