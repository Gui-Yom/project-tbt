package lorganisation.projecttbt.player.attack;

import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.player.attack.effects.Effect;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class RectilignAttack extends Attack {

    private int range;
    private Direction direction;

    public RectilignAttack(int magicCost, int cooldown, int range, Direction direction, int areaRadius, TargetType target, DamageType damageType, BiFunction<Pair<Character, Character>, Coords, Integer> damages, Effect... effects) {

        super(magicCost, cooldown, areaRadius, target, damageType, damages, effects);

        this.range = range;
        this.direction = direction;
    }

    @Override
    public boolean condition(Character origin, Coords target) {

        switch (direction) {
            case LEFT:
                return origin.getPos().getY() == target.getY() && (target.getX() < origin.getPos().getX() && target.getX() >= origin.getPos().getX() - range);
            case RIGHT:
                return origin.getPos().getY() == target.getY() && (target.getX() > origin.getPos().getX() && target.getX() <= origin.getPos().getX() + range);
            case TOP:
                return origin.getPos().getX() == target.getX() && (target.getY() < origin.getPos().getY() && target.getY() >= origin.getPos().getY() - range);
            case DOWN:
                return origin.getPos().getX() == target.getX() && (target.getY() > origin.getPos().getY() && target.getY() <= origin.getPos().getY() + range);
            default:
                return false;
        }
    }

    @Override
    public List<Coords> getReachableTiles(Character origin) {

        List<Coords> result = new ArrayList<>();

        int minX, maxX, minY, maxY;
        switch (direction) {
            case LEFT: {
                minY = origin.getPos().getY();
                maxY = origin.getPos().getY();

                minX = origin.getPos().getX() - this.range;
                maxX = origin.getPos().getX();
            }
            case RIGHT: {
                minY = origin.getPos().getY();
                maxY = origin.getPos().getY();

                minX = origin.getPos().getX();
                maxX = origin.getPos().getX() + this.range;
            }
            case TOP: {
                minX = origin.getPos().getX();
                maxX = origin.getPos().getX();

                minY = origin.getPos().getY() - this.range;
                maxY = origin.getPos().getY();
            }
            case DOWN:
                minX = origin.getPos().getX();
                maxX = origin.getPos().getX();

                minY = origin.getPos().getY();
                maxY = origin.getPos().getY() + this.range;
            default: {
                minX = origin.getPos().getX();
                maxX = minX;
                minY = origin.getPos().getY();
                maxY = minY;
            }
        }

        for (int x = minX; x <= maxX; x++)
            for (int y = minY; y <= maxY; y++)
                result.add(new Coords(x, y));

        return result;
    }

    enum Direction {

        LEFT, RIGHT, DOWN, TOP
    }
}
