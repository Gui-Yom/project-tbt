package lorganisation.projecttbt.map;

import lorganisation.projecttbt.AssetsManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represent a game level where the player can move.
 */
public class LevelMap {

    // https://regex101.com/r/dbNwhj/1
    private static final Pattern validInstruction = Pattern.compile("^(\\S+) ?(.+)?$");

    String visual = null;
    private Tiles[][] tiles;
    private int width;
    private int height;
    private String name;
    private String description;
    private int maxPlayers;
    private List<StartPos> startPos;

    public LevelMap(String name, int width, int height) {

        this(width, height, blankLevel(width, height), name, "", 4, Collections.unmodifiableList(Collections.singletonList(new StartPos(1, 1))));
    }

    public LevelMap(int width, int height, Tiles[][] tiles, String name, String description, int maxPlayers, List<StartPos> startPos) {

        this.width = width;
        this.height = height;
        this.name = name;
        this.description = description;
        this.maxPlayers = maxPlayers;
        this.startPos = startPos;
        if (tiles.length != width && tiles[0].length != height)
            throw new IllegalArgumentException("Given array does not correspond to parameters");
        this.tiles = tiles;

        // Création du visuel de la map
        // FIXME probleme de rotation de la map
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x)
                sb.append(tiles[x][y].getIcon());
            sb.append('\n');
        }
        visual = sb.toString();
    }

    /**
     * Genere une map vide avec des bordures.
     *
     * @param width
     * @param height
     *
     * @return une map vide avec bordures
     */
    public static Tiles[][] blankLevel(int width, int height) {

        Tiles[][] level = new Tiles[width][height];

        for (int i = 0; i < width; ++i)
            for (int j = 0; j < height; ++j)
                if (i == 0 || i == width - 1 || j == 0 || j == height - 1)
                    level[i][j] = Tiles.ROCK;
                else
                    level[i][j] = Tiles.BLANK;

        return level;
    }

    /**
     * Charge une map depuis un fichier .map
     *
     * @param name
     *     le nom de la map à charger (ex: 'blankmap.map')
     *
     * @return la map chargée en mémoire
     *
     * @throws IOException
     *     si il est impossible d'accéder à la ressource spécifiée
     */
    public static LevelMap load(String name) {

        InputStream is = null;
        try {
            is = AssetsManager.openResource("maps/" + name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (is == null) {
            System.err.println("Cannot read specified file.");
            return null;
        }

        Scanner scanner = new Scanner(is, "utf-8");
        String line = null;
        int linenum = 0;

        Map<Character, Tiles> conversion = new HashMap<>();
        List<StartPos> startpos = new ArrayList<>(1);
        String description = "default empty map";
        int width = 10;
        int height = 10;
        int maxplayer = 1;
        Tiles[][] mapdata = null;

        while (scanner.hasNextLine()) {

            line = scanner.nextLine();
            ++linenum;

            if (line == null || line.isEmpty() || line.startsWith("//"))
                continue;

            Matcher m = validInstruction.matcher(line);
            if (m.matches()) {
                switch (m.group(1)) {

                    case "desc":
                        if (m.groupCount() > 1)
                            description = m.group(2);
                        else
                            System.err.println("Ligne " + linenum + " : Une chaîne de caractères est attendue.");
                        break;

                    case "size":
                        if (m.groupCount() > 1) {
                            String[] params = m.group(2).split(" ");
                            if (params.length == 2) {
                                width = Integer.parseInt(params[0]);
                                height = Integer.parseInt(params[1]);
                            } else {
                                System.err.println("Ligne " + linenum + " : 2 paramètres sont attendus pour l'instruction 'size'. " + params.length + " paramètres ont été lu.");
                            }
                        } else {
                            System.err.println("Ligne " + linenum + " : 2 paramètres sont attendus pour l'instruction 'size'. Aucun paramètre n'a été lu.");
                        }
                        break;

                    case "def":
                        if (m.groupCount() > 1) {
                            // Special treatment as we allow ' ' to be a valid tile identifier
                            String params = m.group(2);
                            if (params.startsWith(" ")) {
                                params = params.trim();
                                conversion.put(' ', Tiles.valueOf(params));
                            } else {
                                String[] paramsArr = params.split(" ");
                                if (paramsArr.length == 2)
                                    conversion.put(paramsArr[0].charAt(0), Tiles.valueOf(paramsArr[1]));
                                else
                                    System.err.println("Ligne " + linenum + " : 2 paramètres sont attendus.");
                            }
                        } else
                            System.err.println("Ligne " + linenum + " : 2 paramètres sont attendus.");
                        break;

                    case "max":
                        if (m.groupCount() > 1) {
                            try {
                                maxplayer = Integer.parseInt(m.group(2));
                            } catch (NumberFormatException nfe) {
                                System.err.println("Ligne " + linenum + " : Un nombre est attendu.");
                            }
                        } else
                            System.err.println("Ligne " + linenum + " : 1 paramètre est attendu.");
                        break;

                    case "startpos":
                        if (m.groupCount() > 1) {
                            String[] params = m.group(2).split(" ");
                            if (params.length == 2) {
                                try {
                                    startpos.add(new StartPos(Integer.parseInt(params[0]), Integer.parseInt(params[1])));
                                } catch (NumberFormatException nfe) {
                                    System.err.println("Ligne " + linenum + " : 2 nombres sont attendus.");
                                }
                            } else
                                System.err.println("Ligne " + linenum + " : 2 paramètres sont attendus.");
                        } else
                            System.err.println("Ligne " + linenum + " : 2 paramètres sont attendus.");
                        break;

                    case "map":
                        mapdata = new Tiles[width][height];
                        for (int i = 0; i < width; ++i) {
                            line = scanner.nextLine().replaceAll("!", "");
                            ++linenum;

                            if (!line.isEmpty())

                                if (line.length() == height)
                                    for (int j = 0; j < height; ++j)
                                        mapdata[i][j] = conversion.get(line.charAt(j));
                                else
                                    System.err.println("Ligne " + linenum + " : la largeur est différente de celle spécifiée.");

                            else
                                System.err.println("Ligne " + linenum + " : Une ligne de définition de map est attendue.");
                        }
                        break;

                    default:
                        System.err.println("Ligne " + linenum + " : Instruction inconnue. ('" + m.group(2) + "')");
                }
            }
        }

        scanner.close();

        if (mapdata == null) {
            System.err.println("Aucune définition de map dans ce fichier !");
        }

        return new LevelMap(width,
                            height,
                            mapdata,
                            name.replace(".map", ""),
                            description,
                            maxplayer,
                            Collections.unmodifiableList(startpos));
    }

    public Tiles[][] getTiles() {

        return tiles;
    }

    public String getDescription() {

        return description;
    }

    public int getMaxPlayers() {

        return maxPlayers;
    }

    public List<StartPos> getStartPos() {

        return startPos;
    }

    public StartPos getNextStartPos() {

        for (StartPos sPos : startPos)
            if (sPos.getCharacter() == null)
                return sPos;

        return new StartPos(getWidth() / 2, getHeight() / 2);
    }

    public int getWidth() {

        return width;
    }

    public int getHeight() {

        return height;
    }

    public String getName() {

        return name;
    }

    public String visual() {

        return visual;
    }

    /**
     * Vérifie si la position donnée correspond à une tile où le joueur peut se rendre.
     *
     * @param x
     * @param y
     *
     * @return true si le joueur peut aller sur cette case
     */
    public boolean canCollide(int x, int y) {

        return x > 0 && y > 0 && x < width && y < height && tiles[x][y].canStepOn();
    }

    @Override
    public String toString() {

        return "LevelMap{" +
               "width=" + width +
               ", height=" + height +
               ", name='" + name + '\'' +
               '}';
    }
}
