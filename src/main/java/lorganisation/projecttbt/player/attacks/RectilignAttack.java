package lorganisation.projecttbt.player.attacks;

import lorganisation.projecttbt.player.Character;
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
                return origin.getY() == target.getY() && (target.getX() < origin.getX() && target.getX() >= origin.getX() - range);
            case RIGHT:
                return origin.getY() == target.getY() && (target.getX() > origin.getX() && target.getX() <= origin.getX() + range);
            case TOP:
                return origin.getX() == target.getX() && (target.getY() < origin.getY() && target.getY() >= origin.getY() - range);
            case DOWN:
                return origin.getX() == target.getX() && (target.getY() > origin.getY() && target.getY() <= origin.getY() + range);
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
                minY = origin.getY();
                maxY = origin.getY();

                minX = origin.getX() - this.range;
                maxX = origin.getX();
            }
            case RIGHT: {
                minY = origin.getY();
                maxY = origin.getY();

                minX = origin.getX();
                maxX = origin.getX() + this.range;
            }
            case TOP: {
                minX = origin.getX();
                maxX = origin.getX();

                minY = origin.getY() - this.range;
                maxY = origin.getY();
            }
            case DOWN:
                minX = origin.getX();
                maxX = origin.getX();

                minY = origin.getY();
                maxY = origin.getY() + this.range;
            default: {
                minX = origin.getX();
                maxX = minX;
                minY = origin.getY();
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
