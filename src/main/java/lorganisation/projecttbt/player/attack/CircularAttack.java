package lorganisation.projecttbt.player.attack;

import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.player.attack.effects.Effect;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.Pair;
import lorganisation.projecttbt.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class CircularAttack extends Attack {

    protected Pair<Integer, Integer> range; //Pair.of(min, max)

    public CircularAttack(int magicCost, int cooldown, int minimumRange, int maximumRange, int areaRadius, TargetType target, DamageType damageType, BiFunction<Pair<Character, Character>, Coords, Integer> damages, Effect... effects) {

        super(magicCost, cooldown, areaRadius, target, damageType, damages, effects);

        this.range = Pair.of(minimumRange, maximumRange);
        this.damages = damages;
    }

    @Override
    public boolean condition(Character origin, Coords target) {

        int distance = Utils.distance(origin.getPos(), target);

        return distance >= range.getU() && distance <= range.getV();
    }

    @Override
    public List<Coords> getReachableTiles(Character origin) {

        List<Coords> result = new ArrayList<>();

        int Ox = origin.getPos().getX(), Oy = origin.getPos().getY();

        for (int x = Ox - this.range.getV(); x < Ox + this.range.getV(); x++)
            for (int y = Oy - this.range.getV(); y < Oy + this.range.getV(); y++) {
                int distance = (int) Math.sqrt(Math.pow(Ox - x, 2) + Math.pow(Oy - y, 2));

                if (distance <= this.range.getV() && distance >= this.range.getU())
                    result.add(new Coords(x, y));
            }


        return result;
    }
}
