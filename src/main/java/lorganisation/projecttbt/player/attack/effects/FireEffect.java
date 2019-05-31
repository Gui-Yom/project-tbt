package lorganisation.projecttbt.player.attack.effects;

import lorganisation.projecttbt.player.attack.Attack;

import java.io.Serializable;

public class FireEffect extends Effect {

    public FireEffect(int strength, int duration) {

        this.duration = duration;
        this.function = (Runnable & Serializable) () -> this.owner.damage(Attack.DamageType.MAGIC, strength);
    }
}
