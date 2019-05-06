package lorganisation.projecttbt.player;

public class Character {

    protected int x;
    protected int y;
    protected String type; // Type de personnage
    protected String icon;

    // Definition automatique des capacités à partir du type (dans un fichier texte par exemple)
    protected int portee; // Portée du déplacement
    protected int hp; // Points de vie
    protected int mp; // Points de magie
    protected int defense; // Valeur du bouclier
    protected int dommagesAttaque; // Dommages moyens par attaque

    private Character(String type, String icon, int portee, int hp, int mp, int defense, int dommagesAttaque) {

        this.type = type;
        this.icon = icon;
        this.portee = portee;
        this.hp = hp;
        this.mp = mp;
        this.defense = defense;
        this.dommagesAttaque = dommagesAttaque;
    }

    public Character(int x, int y, String type, String icon) {

        this.x = x;
        this.y = y;
        this.type = type;
        this.icon = icon;
    }

    public Character(CharacterTemplate template) {

        this(template.type, template.icon, template.portee, template.hp, template.mp, template.defense, template.dommagesAttaque);
    }

    @Override
    public String toString() {

        // Je prefere qu'on utilise les toString() pour le déboguage
        // Il vaut mieux créer une autre méthode pour les afficher à la console joliment
        return "Character{" +
               "x=" + x +
               ", y=" + y +
               ", type='" + type + '\'' +
               ", portee=" + portee +
               ", hp=" + hp +
               ", mp=" + mp +
               ", defense=" + defense +
               ", dommagesAttaque=" + dommagesAttaque +
               '}';
    }

    public int getX() {

        return x;
    }

    public void setX(int x) {

        this.x = x;
    }

    public int getY() {

        return y;
    }

    public void setY(int y) {

        this.y = y;
    }

    public void incX() {

        this.x++;
    }

    public void decX() {

        this.x--;
    }

    public void incY() {

        this.y++;
    }

    public void decY() {

        this.y--;
    }

    public String getType() {

        return type;
    }

    public String getIcon() {

        return icon;
    }

    public int getPortee() {

        return portee;
    }

    public int getHp() {

        return hp;
    }

    public int getMp() {

        return mp;
    }

    public int getDefense() {

        return defense;
    }

    public int getDommagesAttaque() {

        return dommagesAttaque;
    }
}
