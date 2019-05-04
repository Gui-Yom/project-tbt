package lorganisation.projecttbt.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class Pair<U, V> implements JsonSerializer<Pair<U, V>>, JsonDeserializer<Pair<U, V>> {

    protected U u;
    protected V v;

    public Pair(U u, V v) {

        this.u = u;
        this.v = v;
    }

    /**
     * Used for serialization only
     */
    private Pair() {

    }

    public static <U, V> Pair<U, V> of(U u, V v) {

        return new Pair<>(u, v);
    }

    public U getU() {

        return u;
    }

    public void setU(U u) {

        this.u = u;
    }

    public V getV() {

        return v;
    }

    public void setV(V v) {

        this.v = v;
    }

    @Override
    public String toString() {

        return "Pair{" +
               "u=" + u +
               ", v=" + v +
               '}';
    }

    @Override
    public Pair<U, V> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext ctx) throws JsonParseException {

        JsonArray arr = (JsonArray) jsonElement;
        return new Pair<>(ctx.deserialize(arr.get(0), new TypeToken<U>() {}.getType()),
                          ctx.deserialize(arr.get(1), new TypeToken<V>() {}.getType()));
    }

    @Override
    public JsonElement serialize(Pair<U, V> uvPair, Type type, JsonSerializationContext ctx) {

        JsonArray arr = new JsonArray(2);
        arr.add(ctx.serialize(uvPair.u, new TypeToken<U>() {}.getType()));
        arr.add(ctx.serialize(uvPair.v, new TypeToken<V>() {}.getType()));
        return arr;
    }
}
