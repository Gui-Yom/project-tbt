package lorganisation.projecttbt.player;

import lorganisation.projecttbt.player.attack.Attack;
import lorganisation.projecttbt.player.attack.CircularAttack;
import lorganisation.projecttbt.player.attack.effects.Effect;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.CyclicList;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;

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

    // FIXME physicAtk et magicAtk useless ?
    protected int physicAtk; // Dommages moyens par attaque
    protected int magicAtk;

    protected List<Effect> effects;
    protected CyclicList<Attack> attacks;

    protected Coords aimingAt;
    protected boolean hasAttacked;

    protected AbstractPlayer owner;

    public Character(CharacterTemplate template, AbstractPlayer owner) {

        this.owner = owner;
        this.pos = new Coords(0, 0);
        this.type = template.type;
        this.icon = template.icon;
        this.maxActionPoints = template.maxActionPoints;
        this.health = template.maxHealth;
        this.maxHealth = template.maxHealth;
        this.magicArmor = template.magicArmor;
        this.magicAtk = template.magicAtk;
        this.armor = template.physicArmor;
        this.physicAtk = template.physicAtk;
        this.effects = new ArrayList<>();
        this.attacks = new CyclicList<>();
        attacks.add(new CircularAttack("Basic Attack", 2, 0, 0, this.maxActionPoints, 0, Attack.TargetType.ENEMIES, (this.magicAtk > this.physicAtk) ? Attack.DamageType.MAGIC : (magicAtk == physicAtk ? Attack.DamageType.TRUE : Attack.DamageType.PHYSIC), (chars, tile) -> 15 - Utils.distance(chars.getU().pos, chars.getV().pos)));
        // FIXME attacks
/*
        if (template.attacks != null)
            this.attacks = new CyclicList<>(template.attacks);
 
            this.attacks = new CyclicList<>();
            
 */

        turnReset();
    }

    public String toString() {

        return type + "(" + owner.getName() + ")" + ": icon=" + icon + ", maxAP=" + maxActionPoints + ", health=" + health + ", magicArmor=" + magicArmor + " armor=" + armor + ", physicAtk=" + physicAtk + ", magicAtk=" + magicAtk + ", effects=" + effects.size() + ", attacks=" + attacks.size();
    }

    public void turnReset() {

        this.actionPoints = maxActionPoints;
        this.hasAttacked = false;

        resetAim();
    }

    public void addEffect(Effect e) {

        this.effects.add(e);
    }

    public void removeEffect(Effect e) {

        this.effects.remove(e);
    }

    public void attack(Attack atk) {

        this.hasAttacked = true;
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

    public CyclicList<Attack> getAttacks() {

        return this.attacks;
    }

    public int getActionPoints() {

        return actionPoints;
    }

    public int getMaxActionPoints() {

        return maxActionPoints;
    }

    public int getHealth() {

        return health;
    }

    public int getMaxHealth() {

        return maxHealth;
    }

    public int getMagicArmor() {

        return magicArmor;
    }

    public int getArmor() {

        return armor;
    }

    public int getPhysicAtk() {

        return physicAtk;
    }

    public int getMagicAtk() {

        return magicAtk;
    }

    public List<Effect> getEffects() {

        return effects;
    }

    public boolean hasAttacked() {

        return hasAttacked;
    }

    public AbstractPlayer getOwner() {

        return owner;
    }

    public Coords getPos() {

        return pos;
    }

    public void setPos(Coords pos) {

        this.pos = pos;
    }

    public Coords getAimingAt() {

        return aimingAt;
    }

    public void setAimingAt(Coords aimingAt) {

        this.aimingAt = aimingAt;
    }

    public void resetAim() {

        this.aimingAt = new Coords(pos.getX(), pos.getY());
    }

    public void consumeActionPoints(int cost) {

        this.actionPoints = Math.max(this.actionPoints -= cost, 0);
    }

    public List<StyledString> getDescription() {


        List<StyledString> desc = new ArrayList<>();
        desc.add(new StyledString("Type: " + type + " (" + icon + ")"));
        desc.add(new StyledString("Action: " + actionPoints + " / " + maxActionPoints));
        desc.add(new StyledString("Vie: " + health + " / " + maxHealth));
        desc.add(new StyledString("Defense: " + armor + " | " + magicArmor));
        desc.add(new StyledString("Attaque: " + physicAtk + " | " + magicAtk));

        if (!effects.isEmpty()) {
            desc.add(new StyledString("Effets:"));
            for (Effect effect : effects)
                desc.add(new StyledString(" - " + effect.toString()));
        }

        return desc;
    }
}
