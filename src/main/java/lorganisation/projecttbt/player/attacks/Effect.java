package lorganisation.projecttbt.player.attacks;


import com.google.gson.annotations.Expose;
import lorganisation.projecttbt.player.Character;


public abstract class Effect {

    protected Character owner = null;

    @Expose
    protected int duration;

    @Expose
    protected Runnable function;

    public void applyTo(Character character) {

        if (this.owner != character)
            character.addEffect(this);
    }

    public void tick() {

        function.run();

        if (--duration <= 0)
            owner.removeEffect(this);
    }

    public void increaseDuration() {

        ++duration;
    }
}
