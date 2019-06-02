package lorganisation.projecttbt.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lorganisation.projecttbt.AssetsManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

public class CharacterTemplate {

    private static HashMap<String, CharacterTemplate> characterTemplates;

    private static Gson gson;

    static {
        GsonBuilder gBuild = new GsonBuilder();
        //gBuild.registerTypeAdapter(CharacterTemplate.class, new CharacterDeserializer());

        gson = gBuild.create();
        characterTemplates = new HashMap<>();
        try {
            for (String name : AssetsManager.gameCharacterNames())
                characterTemplates.put(name, load(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Expose
    protected String type; // Type de personnage
    @Expose
    protected String icon; // icone
    // Definition automatique des capacités à partir du type (dans un fichier texte par exemple)

    @Expose
    protected int maxActionPoints;
    @Expose
    protected int maxHealth;

    @Expose
    protected int magicArmor; // Points de magie
    @Expose
    protected int physicArmor; // Valeur du bouclier

    @Expose
    protected int physicAtk; // Dommages moyens par attaque
    @Expose
    protected int magicAtk;

    //protected List<Attack> attacks;

    private static CharacterTemplate load(String name) throws IOException {

        if (characterTemplates.containsKey(name))
            return characterTemplates.get(name);

        Reader reader = new InputStreamReader(AssetsManager.openResource(AssetsManager.gameCharacters().get(name)));

        return gson.fromJson(reader, CharacterTemplate.class);
    }

    public static CharacterTemplate getCharacterTemplate(String type) {

        return characterTemplates.get(type);
    }

    public static HashMap<String, CharacterTemplate> getCharacterTemplates() {

        return characterTemplates;
    }
/*
    public static class CharacterDeserializer implements JsonDeserializer<CharacterTemplate> {

        @Override
        public CharacterTemplate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

            JsonObject jsonObj = json.getAsJsonObject();

            System.out.println(jsonObj.toString());

            if (!jsonObj.has("type"))
                throw new JsonParseException("Character is missing type");
            if (!jsonObj.has("icon"))
                throw new JsonParseException("Character is missing icon");
            if (!jsonObj.has("actionPoints"))
                throw new JsonParseException("Character is missing actionPoints");
            if (!jsonObj.has("health"))
                throw new JsonParseException("Character is missing health");
            if (!jsonObj.has("magicArmor"))
                throw new JsonParseException("Character is missing magicArmor");
            if (!jsonObj.has("physicArmor"))
                throw new JsonParseException("Character is missing physicArmor");
            if (!jsonObj.has("physicAtk"))
                throw new JsonParseException("Character is missing physicAtk");
            if (!jsonObj.has("magicAtk"))
                throw new JsonParseException("Character is missing magicAtk");
            if (!jsonObj.has("attacks"))
                throw new JsonParseException("Character is missing attacks");


            CharacterTemplate template = new CharacterTemplate();
            template.type = jsonObj.get("type").getAsString();
            template.icon = jsonObj.get("icon").getAsString();
            template.maxActionPoints = jsonObj.get("actionPoints").getAsInt();
            template.maxHealth = jsonObj.get("health").getAsInt();
            template.magicArmor = jsonObj.get("magicArmor").getAsInt();
            template.physicArmor = jsonObj.get("physicArmor").getAsInt();
            template.physicAtk = jsonObj.get("physicAtk").getAsInt();
            template.magicAtk = jsonObj.get("magicAtk").getAsInt();

            ArrayList<Attack> attacks = new ArrayList<>();
            JsonArray jsonAtks = jsonObj.getAsJsonArray("attacks");
            for (JsonElement jsonAtk : jsonAtks)
                attacks.add(Attack.fromJson(jsonAtk));

            template.attacks = attacks;

            return template;
        }
    }

 */
}
