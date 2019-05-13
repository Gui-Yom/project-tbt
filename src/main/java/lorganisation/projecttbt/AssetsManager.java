package lorganisation.projecttbt;

import com.google.gson.Gson;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.*;

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
     * Un ClassLoader permettant de charger des ressources depuis le jar.
     */
    private static final ClassLoader assets = new ClassLoader() {
        @Override
        public URL getResource(String name) {

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
     * Les noms des bots.
     */
    private static List<String> botNames;

    private static Map<String, String> maps;

    private static Map<String, String> characters;

    // Comme un constructeur mais dans le domaine statique
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
     * Charge un asset du jeu depuis le jar ou le dossier où est contenu le jar. Un try-with-resource est
     * particulièrement adapté :
     * <pre>{@code
     * try (InputStream in = AssetsManager.openstream("dir/ressource.json") {
     *     in.read();
     * } catch (IOException ioe) {
     *     ioe.printStackTrace();
     * }
     * }</pre>
     *
     * @param path          le chemin d'accès à la ressource.
     * @param allowFromFile true pour autoriser le chargement depuis un fichier en dehors du jar.
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
     * @param path le chemin d'accès à la ressource.
     *
     * @return un stream permettant de lire l'asset du jeu.
     *
     * @see AssetsManager#openResource(String, boolean)
     */
    public static InputStream openResource(String path) throws IOException {

        return openResource(path, true);
    }

    /**
     * Cherche une ressource à l'intérieur ou à l'extérieur du jar
     *
     * @param path          le chemin de la ressource
     * @param allowFromFile true pour autoriser le chargement depuis un fichier en dehors du jar.
     *
     * @return l'URL de la ressource ou null si elle n'existe pas
     */
    public static URL getResource(String path, boolean allowFromFile) {

        // ressource extraite
        if (allowFromFile) {
            File ext = new File(path);
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

    /**
     * Cherche une ressource à l'intérieur ou à l'extérieur du jar. La priorité est donnée aux fichiers extraits.
     *
     * @param path le chemin de la ressource
     *
     * @return l'URL de la ressource ou null si elle n'existe pas
     */
    public static URL getResource(String path) {

        return getResource(path, true);
    }

    /**
     * @return une map contenant les noms des maps comme clé et les noms des fichiers comme valeurs.
     */
    public static Map<String, String> gameMaps() {

        return maps;
    }

    /**
     * @return une liste des noms des map disponibles
     */
    public static Set<String> gameMapNames() {

        return gameMaps().keySet();
    }

    /**
     * @return une liste des noms des fichiers des map disponibles
     */
    public static Set<String> gameMapFiles() {

        return new HashSet<>(gameMaps().values());
    }

    /**
     * @return une map contenant les noms des caractères comme clé et les noms des fichiers comme valeurs.
     */
    public static Map<String, String> gameCharacters() {

        return characters;
    }

    /**
     * @return une liste des noms des map disponibles
     */
    public static Set<String> gameCharacterNames() {

        return gameCharacters().keySet();
    }

    /**
     * @return une liste des noms des fichiers des map disponibles
     */
    public static Set<String> gameCharacterFiles() {

        return new HashSet<>(gameCharacters().values());
    }

    /**
     * @return une liste des noms des noms de bots.
     */
    public static List<String> botNames() {

        return botNames;
    }

    /**
     * Détecte les assets du jeu et charge leur chemin en mémoire. Détecte d'abord les assets du jar, puis ceux à
     * l'extérieur.Les assets à l'extérieur ont la priorité.
     *
     * @return true si les ressources ont été trouvées
     */
    public static boolean reload() {

        //System.out.print("Loading ressources ...");

        maps = new HashMap<>();
        characters = new HashMap<>();

        try {

            listFiles("assets/maps", "*.map")
                .forEach(s -> maps.put(s.substring(0, s.lastIndexOf('.')), "assets/maps/" + s));

            listFiles("assets/characters", "*.json")
                .forEach(s -> characters.put(s.substring(0, s.lastIndexOf('.')), "assets/characters/" + s));

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        try {

            botNames = new ArrayList<>();
            Scanner scanner = new Scanner(openResource("assets/bots.txt"));
            String line = null;

            while (scanner.hasNextLine())
                if (!(line = scanner.nextLine().trim()).equals(""))
                    botNames.add(line);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (maps == null || characters == null || botNames.size() == 0) {
            //System.err.println(" FAIL !");
            return false;
        } else {
            //System.out.println(" OK");
            return true;
        }
    }

    /**
     * Extrait une ressource du jar. Il s'agit juste d'une copie de données vers un nouveau fichier.
     *
     * @param path le chemin de la ressource à extraire
     *
     * @return true si l'extraction a réussie
     */
    public static boolean extract(String path) {

        System.out.println("Extracting " + path);
        File f = new File(path);
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
     * Liste les fichiers dans un dossier du jar.
     *
     * @param dir        the directory
     * @param globFilter glob filter for filenames
     *
     * @return a list of files matching the filter
     *
     * @throws URISyntaxException
     * @throws IOException
     */
    private static List<String> listFilesInJar(String dir, String globFilter) throws IOException, URISyntaxException {

        List<String> files = new ArrayList<>();

        try (FileSystem fs = getJarFileSystem();
             DirectoryStream<Path> dirStream = Files.newDirectoryStream(fs.getPath(dir), globFilter)) {

            dirStream.forEach(path -> files.add(path.getName(path.getNameCount() - 1).toString()));
        }

        return files;
    }

    private static List<String> listFilesOutside(String dir, String globFilter) throws IOException {

        List<String> files = new ArrayList<>();

        File f = new File(dir);
        if (!f.exists())
            return files;

        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(dir), globFilter)) {

            dirStream.forEach(path -> files.add(path.getName(path.getNameCount() - 1).toString()));
        }

        return files;
    }

    private static List<String> listFiles(String dir, String globFilter) throws IOException, URISyntaxException {

        List<String> files = new ArrayList<>();

        files.addAll(listFilesInJar(dir, globFilter));

        listFilesOutside(dir, globFilter).forEach(s -> {
            boolean exist = false;
            for (int i = 0; i < files.size(); i++) {
                if (files.get(i).equals(s))
                    exist = true;
            }
            if (!exist)
                files.add(s);
        });

        return files;
    }

    private static FileSystem getJarFileSystem() throws IOException {

        URI uri = null;
        // This should not happen unless the jar has been packed without assets
        try {
            uri = AssetsManager.class.getClassLoader().getResource("assets").toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        try {
            return FileSystems.getFileSystem(uri);
        } catch (FileSystemNotFoundException e) {
            return FileSystems.newFileSystem(uri, Collections.emptyMap());
        }
    }
}
