package lorganisation.projecttbt.player.attack.effects;

import lorganisation.projecttbt.player.attack.Attack;

public class PoisonEffect extends Effect {

    public PoisonEffect(int strength, int duration) {

        this.duration = duration;
        this.function = () -> this.owner.damage(Attack.DamageType.TRUE, strength);
    }
}
