package lorganisation.projecttbt.player;

import com.google.gson.Gson;
import lorganisation.projecttbt.AssetsManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

public class CharacterTemplate {

    private static HashMap<String, CharacterTemplate> characterTemplates;

    private static Gson gson;

    static {
        gson = new Gson();
        characterTemplates = new HashMap<>();
        try {
            for (String name : AssetsManager.gameCharacterNames())
                characterTemplates.put(name, load(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String type; // Type de personnage
    protected String icon; // icone
    // Definition automatique des capacités à partir du type (dans un fichier texte par exemple)

    protected int portee; // Portée du déplacement
    protected int hp; // Points de vie
    protected int mp; // Points de magie
    protected int defense; // Valeur du bouclier
    protected int dommagesAttaque; // Dommages moyens par attaque

    private static CharacterTemplate load(String name) throws IOException {

        if (characterTemplates.containsKey(name))
            return characterTemplates.get(name);

        Reader reader = new InputStreamReader(AssetsManager.openResource("characters/" + AssetsManager.gameCharacters().get(name)));

        return gson.fromJson(reader, CharacterTemplate.class);
    }

    public static CharacterTemplate getCharacterTemplate(String type) {

        return characterTemplates.get(type);
    }

    public static HashMap<String, CharacterTemplate> getCharacterTemplates() {

        return characterTemplates;
    }

    public Character createCharacter() {

        return new Character(this);
    }
}
