package lorganisation.projecttbt.player.attack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.player.attack.effects.Effect;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.Pair;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public abstract class Attack {

    public int areaRadius; // radius of attack's impact zone
    protected int cooldown; // default cooldown
    protected int cost;
    protected List<Effect> effects;
    protected TargetType targetType;
    protected DamageType damageType;
    protected String name;

    // Pair of ( Caster, Hit Character(if any) ), Tile Hit ===function===> Integer (damages dealt to Hit Character)
    protected BiFunction<Pair<Character, Character>, Coords, Integer> damages; // function that evaluates damages (Integer) to deal using Pair< caster, target > and hitTile

    public Attack(String name, int cost, int cooldown, int areaRadius, TargetType target, DamageType damageType, BiFunction<Pair<Character, Character>, Coords, Integer> damages, Effect... effects) {

        this.name = name;
        this.effects = Utils.arrayToList(effects);
        this.targetType = target;
        this.damages = damages;
        this.cooldown = cooldown;
        this.cost = cost;
        this.damageType = damageType;
        this.areaRadius = areaRadius;
    }

    //FIXME TODO prendre la fonction damages depuis le json
    public static Attack fromJson(JsonElement jsonElement) {

        JsonObject jsonObj = jsonElement.getAsJsonObject();

        if (!jsonObj.has("type")
            || !jsonObj.has("name")
            || !jsonObj.has("cost")
            || !jsonObj.has("cooldown")
            || !jsonObj.has("areaRadius")
            || !jsonObj.has("targetType")
            || !jsonObj.has("damageType")
            || !jsonObj.has("damages"))
            throw new JsonParseException("Attack is missing some parameters, check the manual !");

        String type = jsonObj.get("type").getAsString();
        DamageType damageType;
        try {
            damageType = DamageType.valueOf(jsonObj.get("damageType").getAsString());
        } catch (IllegalArgumentException ex) {
            throw new JsonParseException("DamageType isn't valid, choose from: MAGIC, PHYSIC, TRUE");
        }

        TargetType targetType;
        try {
            targetType = TargetType.valueOf(jsonObj.get("targetType").getAsString());
        } catch (IllegalArgumentException ex) {
            throw new JsonParseException("TargetType isn't valid, choose from: ALL, SELF, ALLIES, ENEMIES");
        }

        if (type.toLowerCase().contains("circular")) {

            if (!jsonObj.has("minimumRange")
                || !jsonObj.has("maximumRange"))
                throw new JsonParseException("CircularAttack needs parameters: minimumRange(int) & maximumRange(int)");

            CircularAttack atk = new CircularAttack(jsonObj.get("name").getAsString(),
                                                    jsonObj.get("cost").getAsInt(),
                                                    jsonObj.get("cooldown").getAsInt(),
                                                    jsonObj.get("minimumRange").getAsInt(),
                                                    jsonObj.get("maximumRange").getAsInt(),
                                                    jsonObj.get("areaRadius").getAsInt(),
                                                    targetType, damageType, (chars, tile) -> 10);

            return atk;

        } else if (type.toLowerCase().contains("rectilign")) {

            if (!jsonObj.has("range")
                || !jsonObj.has("direction"))
                throw new JsonParseException("RectilignAttack needs parameters: range(int) & direction(Direction: LEFT, RIGHT, DOWN, TOP)");

            RectilignAttack.Direction direction;
            try {
                direction = RectilignAttack.Direction.valueOf(jsonObj.get("direction").getAsString());
            } catch (IllegalArgumentException ex) {
                throw new JsonParseException("Direction isn't valid, choose from: UP, RIGHT, DOWN, LEFT");
            }


            RectilignAttack atk = new RectilignAttack(jsonObj.get("range").getAsString(),
                                                      jsonObj.get("cost").getAsInt(),
                                                      jsonObj.get("cooldown").getAsInt(),
                                                      jsonObj.get("range").getAsInt(),
                                                      direction,
                                                      jsonObj.get("areaRadius").getAsInt(),
                                                      targetType, damageType, (chars, tile) -> 10);

            return atk;

        } else
            throw new JsonParseException("Attack Type is not correct (CircularAttack or RectilignAttack are available atm.)");
    }

    /**
     * @return true if attack has been casted
     */
    public boolean use(Game game, Character origin, Coords tile) {

        if (this.targetType.equals(TargetType.SELF) && condition(origin, tile)) {

            // if damages < 0 it's a heal, if it's > 0 then the spell costs health to cast, damages = 0 -> not affecting health, only effects applied to player
            origin.damage(damageType, this.damages.apply(Pair.of(origin, origin), tile));

            for (Effect effect : effects) {
                origin.addEffect(effect);
            }

            return true;
        }

        if (condition(origin, tile) && origin.getActionPoints() >= this.cost) {

            origin.consumeActionPoints(this.cost);

            for (AbstractPlayer player : game.getPlayers())
                for (Character target : player.getCharacters()) {

                    // if spell is targeting Allies and the tested Character doesn't belong to casting character's player, skip this character;
                    if (this.targetType.equals(TargetType.ALLIES) && target.getOwner() != origin.getOwner()) {
                        continue;
                        // if spell is targeting enemies and character belongs to same player, skip this character
                    } else if (this.targetType.equals(TargetType.ENEMIES) && target.getOwner() == origin.getOwner()) {
                        continue;
                    } else if (this.targetType == TargetType.SELF)
                        continue;

                    if (Utils.distance(tile, target.getPos()) <= areaRadius) {
                        target.damage(damageType, this.damages.apply(Pair.of(origin, target), tile));

                        for (Effect effect : effects) {
                            target.addEffect(effect);
                        }
                    }
                }

            return true;
        }

        return false;
    }

    public abstract boolean condition(Character origin, Coords target);

    public abstract List<Coords> getReachableTiles(Character origin);

    public List<Coords> getHitTiles(Coords tile) {

        List<Coords> hitTiles = new ArrayList<>();

        for (int x = tile.getX() - this.areaRadius; x <= tile.getX() + this.areaRadius; x++)
            for (int y = tile.getY() - this.areaRadius; y <= tile.getY() + this.areaRadius; y++) {
                int distance = (int) Math.sqrt(Math.pow(tile.getX() - x, 2) + Math.pow(tile.getY() - y, 2));

                if (distance <= this.areaRadius)
                    hitTiles.add(new Coords(x, y));
            }

        hitTiles.add(new Coords(tile.getX(), tile.getY()));

        return hitTiles;
    }

    public List<StyledString> getDescription() {

        List<StyledString> desc = new ArrayList<>();
        desc.add(new StyledString(this.getName()));
        desc.add(new StyledString("Type: " + this.getClass().getSimpleName().replaceAll("Attack", "") + " | " + damageType + " DAMAGE"));
        //TODO give color to damageType
        desc.add(new StyledString("Zone d'impact: " + (areaRadius + 1)));
        desc.add(new StyledString("Atteint: " + targetType.name()));

        if (!effects.isEmpty()) {
            desc.add(new StyledString("Effets:"));
            for (Effect effect : effects)
                desc.add(new StyledString(" - " + effect.toString()));
        }

        desc.add(new StyledString("Coût: " + cost + " AP"));
        desc.add(new StyledString("Récupération: " + cooldown));

        return desc;
    }

    public String getName() {

        return this.name;
    }

    public int getCost() {

        return this.cost;
    }

    public enum TargetType {

        ALLIES, SELF, ENEMIES, ALL
    }

    public enum DamageType {

        MAGIC, PHYSIC, TRUE
    }
}
