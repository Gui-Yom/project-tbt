package lorganisation.projecttbt.player.attack;

import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.player.attack.effects.Effect;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.Pair;
import lorganisation.projecttbt.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public abstract class Attack {

    protected TargetType target;
    protected int areaRadius; // radius of attack's impact zone
    protected int cooldown; // default cooldown
    protected int magicCost;
    protected List<Effect> effects;
    protected DamageType damageType;
    // Pair of ( Caster, Hit Character(if any) ), Tile Hit ===function===> Integer (damages dealt to Hit Character)
    protected BiFunction<Pair<Character, Character>, Coords, Integer> damages;

    public Attack(int magicCost, int cooldown, int areaRadius, TargetType target, DamageType damageType, BiFunction<Pair<Character, Character>, Coords, Integer> damages, Effect... effects) {

        this.effects = Utils.arrayToList(effects);
        this.target = target;
        this.damages = damages;
        this.cooldown = cooldown;
        this.magicCost = magicCost;
        this.damageType = damageType;
        this.areaRadius = areaRadius;
    }

    /**
     * @return true if attack has been casted
     */
    public boolean use(Game game, Character origin, Coords tile) {

        if (this.target.equals(TargetType.SELF) && condition(origin, tile)) {

            // if damages < 0 it's a heal, if it's > 0 then the spell costs health to cast, damages = 0 -> not affecting health, only effects applied to player
            origin.damage(damageType, this.damages.apply(Pair.of(origin, origin), tile));

            for (Effect effect : effects) {
                origin.addEffect(effect);
            }

            return true;
        }

        if (condition(origin, tile) && origin.getMagicArmor() >= this.magicCost) {
            origin.decreaseMana(this.magicCost);

            for (AbstractPlayer player : game.getPlayers())
                for (Character target : player.getCharacters()) {

                    // if spell is targeting Allies and the tested Character doesn't belong to casting character's player, skip this character;
                    if (this.target.equals(TargetType.ALLIES) && Utils.getCharacterOwner(game.getPlayers(), target) != Utils.getCharacterOwner(game.getPlayers(), origin)) {
                        continue;
                        // if spell is targeting enemies and character belongs to same player, skip this character
                    } else if (this.target.equals(TargetType.ENEMIES) && Utils.getCharacterOwner(game.getPlayers(), target) == Utils.getCharacterOwner(game.getPlayers(), origin)) {
                        continue;
                    } else if (this.target == TargetType.SELF)
                        continue;

                    if (Utils.distance(tile, target.getPos()) <= areaRadius) {
                        target.damage(damageType, this.damages.apply(Pair.of(origin, target), tile));


                        for (Effect effect : effects) {
                            target.addEffect(effect);
                        }

                        return true;
                    }
                }
        }

        return false;
    }

    public abstract boolean condition(Character origin, Coords target);

    public abstract List<Coords> getReachableTiles(Character origin);

    public List<Coords> getHitTiles(Coords tile) {

        List<Coords> hitTiles = new ArrayList<>();

        for (int x = tile.getX() - this.areaRadius; x < tile.getX() + this.areaRadius; x++)
            for (int y = tile.getY() - this.areaRadius; y < tile.getY() + this.areaRadius; y++) {
                int distance = (int) Math.sqrt(Math.pow(tile.getX() - x, 2) + Math.pow(tile.getY() - y, 2));

                if (distance <= this.areaRadius && distance >= areaRadius)
                    hitTiles.add(new Coords(x, y));
            }


        return hitTiles;
    }

    public enum TargetType {

        ALLIES, SELF, ENEMIES

    }

    public enum DamageType {

        MAGIC, PHYSIC, TRUE
    }
}
