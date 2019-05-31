package lorganisation.projecttbt.experiments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.player.attack.Attack;
import lorganisation.projecttbt.player.attack.CircularAttack;
import lorganisation.projecttbt.player.attack.effects.FireEffect;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.Pair;
import lorganisation.projecttbt.utils.Utils;

import java.io.Serializable;
import java.util.function.BiFunction;

public class SerializationTest {

    public static void main(String[] args) {

        // int magicCost, int cooldown, int minimumRange, int maximumRange, int areaRadius, TargetType target, DamageType damageType, BiFunction<Pair<Character, Character>, Coords, Integer> damages, Effect... effects
        BiFunction<Pair<Character, Character>, Coords, Integer> lightningStrikeDamages = (BiFunction<Pair<Character, Character>, Coords, Integer> & Serializable) (pair, tile) -> (4 - Utils.distance(pair.getV().getPos(), tile));
        CircularAttack lightningStrike = new CircularAttack(10, 3, 2, 5, 2, Attack.TargetType.ENEMIES, Attack.DamageType.MAGIC, lightningStrikeDamages, new FireEffect(1, 3));

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String serializedObject;


        System.out.println("|----------+Serializing+----------|");
        System.out.println(serializedObject = gson.toJson(new FireEffect(1, 3)));
        System.out.println("|---------+Deserializing+---------|");
        System.out.println("Objects are equals: " + lightningStrike.equals(gson.fromJson(serializedObject, FireEffect.class)));
        System.out.println("|------------+Finished+-----------|");
    }
}
