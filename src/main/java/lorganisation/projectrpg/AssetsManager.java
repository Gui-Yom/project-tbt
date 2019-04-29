package lorganisation.projectrpg;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Cette classe gère les ressources du jeu. Du fait que l'on veuille pouvoir modifier les ressources du jeu facilement
 * et pendant son fonctionnement, les ressources sont répertoriées dans un fichier registry.json. Le registre est chargé
 * une première fois au chargement du jeu. Les ressources peuvent aussi être chargée depuis un dossier en dehors du jar
 * (avec la même structure). Les assets en dehors du jar écrasent alors ceux à l'intérieur.
 *
 * @see AssetsManager#openResource(String)
 */
public class AssetsManager {

    /**
     * Un ClassLoader permettant de charger les assets depuis le dossier 'assets' dans le jar du jeu.
     */
    private static final ClassLoader assets = new ClassLoader() {
        @Override
        public URL getResource(String name) {

            name = (name.startsWith("assets/") ? "" : "assets/") + name;
            URL res = AssetsManager.class.getClassLoader().getResource(name);
            if (res != null)
                return res;
            else
                return super.getResource(name);
        }
    };

    /**
     * Le parser JSON
     */
    private static final Gson gson = new Gson();
    /**
     * Le registre des ressources
     */
    private static Registry registry;

    // Traitement effectué au chargement de la classe, cad au premier accès à celle-ci
    static {
        if (!reload())
            System.err.println("Unable to load registry !");
    }

    /**
     * Cette classe n'a pas besoin d'être instanciée pour fonctionner.
     */
    private AssetsManager() {

    }

    /**
     * Charge un asset du jeu depuis le jar ou le dossier où est contenu le jar. La priorité est donnée aux fichiers
     * extraits. Un try-with-resource est particulièrement adapté :
     * <pre>{@code
     * try (InputStream in = AssetsManager.openstream("dir/ressource.json") {
     *     in.read();
     * } catch (IOException ioe) {
     *     ioe.printStackTrace();
     * }
     * }</pre>
     *
     * @param path
     *     le chemin d'accès à la ressource.
     *
     * @return un stream permettant de lire l'asset du jeu.
     */
    public static InputStream openResource(String path, boolean allowFromFile) throws IOException {

        URL res = AssetsManager.getResource(path, allowFromFile);
        if (res != null)
            return res.openStream();
        else
            throw new IOException("La ressource '" + path + "' n'existe pas.");
    }

    public static InputStream openResource(String path) throws IOException {

        return openResource(path, true);
    }

    /**
     * Cherche une ressource à l'intérieur ou à l'extérieur du jar
     *
     * @param path
     *     le chemin de la ressource
     *
     * @return l'URL de la ressource ou null si elle n'existe pas
     */
    public static URL getResource(String path, boolean allowFromFile) {

        // ressource extraite
        if (allowFromFile) {
            File ext = new File("assets/" + path);
            if (ext.exists()) {
                try {
                    return ext.toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        // ressource dans le jar ou nul part
        return assets.getResource(path);
    }

    public static URL getResource(String path) {

        return getResource(path, true);
    }

    /**
     * @return une map contenant les noms des maps comme clé et les noms des fichiers comme valeurs.
     */
    public static Map<String, String> listMaps() {

        return registry.maps;
    }

    /**
     * @return une liste des noms des map disponibles
     */
    public static Set<String> mapNames() {

        return listMaps().keySet();
    }

    /**
     * @return une liste des noms des fichiers des map disponibles
     */
    public static Set<String> mapFiles() {

        return new HashSet<>(listMaps().values());
    }

    /**
     * @return une map contenant les noms des charactères comme clé et les noms des fichiers comme valeurs.
     */
    public static Map<String, String> listCharacters() {

        return registry.characters;
    }

    /**
     * @return une liste des noms des map disponibles
     */
    public static Set<String> characterNames() {

        return listCharacters().keySet();
    }

    /**
     * @return une liste des noms des fichiers des map disponibles
     */
    public static Set<String> characterFiles() {

        return new HashSet<>(listCharacters().values());
    }

    /**
     * Charge le registre des assets du jeu en mémoire.
     *
     * @return true si le registre a bien été chargé
     */
    public static boolean reload() {

        System.out.print("Loading ressources ...");
        try {
            registry = gson.fromJson(new InputStreamReader(openResource("registry.json")), Registry.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (registry == null) {
            System.err.println(" FAIL !");
            return false;
        } else {
            System.out.println(" OK");
            return true;
        }
    }

    /**
     * Extrait les assets du jeu dans un dossier à côté du jar.
     */
    public static void extractAll() {

        if (!extract("registry.json")) {
            System.err.println("Unable to extract 'registry.json'.");
        }
        for (String s : mapFiles())
            if (!extract("maps/" + s))
                System.err.println("Unable to extract '" + s + "'.");
        for (String s : characterFiles())
            if (!extract("characters/" + s))
                System.err.println("Unable to extract '" + s + "'.");
    }

    /**
     * Extrait une ressource du jar. Il s'agit juste d'une copie de données vers un nouveau fichier.
     *
     * @param path
     *     le chemin de la ressource à extraire
     *
     * @return true si l'extraction a réussie
     */
    public static boolean extract(String path) {

        System.out.println("Extracting " + path);
        File f = new File((path.startsWith("assets/") ? "" : "assets/") + path);
        if (f.exists())
            f.delete();
        else {
            f.mkdirs();
            f.delete();
        }

        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(openResource(path, false)));
             BufferedWriter out = new BufferedWriter(new FileWriter(f))) {

            String l = null;
            while ((l = in.readLine()) != null) {
                out.append(l).append(System.lineSeparator());
                //System.out.print(l);
            }
            out.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Contient les références vers les assets du jeu
     */
    private static class Registry {

        /**
         * Une liste de paires contenant chacune le nom de la map et son fichier.
         */
        @Expose
        Map<String, String> maps;
        /**
         * Une liste de paires contenant chacune le nom du caractère et son fichier.
         */
        @Expose
        Map<String, String> characters;
    }
}
