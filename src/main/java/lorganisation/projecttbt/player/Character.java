package lorganisation.projecttbt.player;

import lorganisation.projecttbt.player.attack.Attack;
import lorganisation.projecttbt.player.attack.effects.Effect;
import lorganisation.projecttbt.utils.Coords;

import java.util.ArrayList;
import java.util.List;

public class Character {

    // Position
    protected Coords pos;
    protected String type; // Type de personnage
    protected String icon;

    // Definition automatique des capacités à partir du type (dans un fichier texte par exemple)
    protected int actionPoints; // Portée du déplacement
    protected int maxActionPoints;
    protected int health; // Points de vie
    protected int maxHealth;

    protected int magicArmor; // Points de magie
    protected int armor; // Valeur du bouclier

    protected int physicAtk; // Dommages moyens par attaque
    protected int magicAtk;

    protected List<Effect> effects;

    private Character(String type, String icon, int actionPoints, int health, int magicArmor, int armor, int physicAtk) {

        this.pos = new Coords(0, 0);
        this.type = type;
        this.icon = icon;
        this.actionPoints = actionPoints;
        this.maxActionPoints = actionPoints;
        this.health = health;
        this.magicArmor = magicArmor;
        this.armor = armor;
        this.physicAtk = physicAtk;
        this.effects = new ArrayList<>();
    }

    public Character(CharacterTemplate template) {

        this(template.type, template.icon, template.portee, template.hp, template.mp, template.defense, template.dommagesAttaque);
    }

    public void addEffect(Effect e) {

        this.effects.add(e);
    }

    public void removeEffect(Effect e) {

        this.effects.remove(e);
    }

    public void damage(Attack.DamageType type, int damage) {

        switch (type) {

            case TRUE: {
                this.health -= damage;
                break;
            }

            case MAGIC: {
                this.health -= damage * 100 / (magicArmor + 100);
                break;
            }

            case PHYSIC: {
                this.health -= damage * 100 / (armor + 100);
                break;
            }
        }

    }

    public String getType() {

        return type;
    }

    public String getIcon() {

        return icon;
    }

    public int getActionPoints() {

        return actionPoints;
    }

    public void setActionPoints(int actionPoints) {

        this.actionPoints = actionPoints;
    }

    public int getDefaultActionPoints() {

        return maxActionPoints;
    }

    public int getHealth() {

        return health;
    }

    public void setHealth(int health) {

        this.health = health;
    }

    public int getMagicArmor() {

        return magicArmor;
    }

    public void setMagicArmor(int magicArmor) {

        this.magicArmor = magicArmor;
    }

    public int getArmor() {

        return armor;
    }

    public void setArmor(int armor) {

        this.armor = armor;
    }

    public int getPhysicAtk() {

        return physicAtk;
    }

    public void setPhysicAtk(int physicAtk) {

        this.physicAtk = physicAtk;
    }

    public void decreaseMana(int magicCost) {

        this.magicArmor -= magicCost;
    }

    public Coords getPos() {

        return pos;
    }

    public void setPos(Coords pos) {

        this.pos = pos;
    }
}
