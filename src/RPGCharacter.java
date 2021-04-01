import java.util.ArrayList;
import java.util.List;

public class RPGCharacter {
    public int health = 1000;
    public int level = 1;
    public int maxRange = 2;
    private int strength = 300;
    public List<String> factions = new ArrayList();
    private boolean isAlive = true;

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isAlly(RPGCharacter target) {
        boolean isAlly = false;
        for (String faction : factions) {
            if (target.factions.contains(faction))
                isAlly = true;
        }
        return isAlly || target == this;
    }

    public int takeDamage(int amount) {
        if (amount >= health)
            return die();
        return health -= amount;
    }

    public void attack(RPGCharacter target, int distance) {
        boolean canAttack = canAttack(target, distance);
        if (canAttack)
            target.takeDamage(getDamageToDeal(target));
    }

    public void attack(Thing target) {
        target.takeDamage(strength);
    }

    public void heal(RPGCharacter target) {
        boolean canHeal = canHeal(target);
        if (canHeal)
            target.health += 200;
    }

    public void leaveFaction(String test) {
        if (factions.size() == 0)
            throw new RPGCharacter.NoMoreFactionException();
        factions.remove(factions.indexOf(test));
    }

    public void joinFaction(String test) {
        factions.add(test);
    }

    private int die() {
        isAlive = false;
        return health = 0;
    }

    private boolean canHeal(RPGCharacter target) {
        return isAlly(target) && target.isAlive() && target.health < 1000;
    }

    private int getDamageToDeal(RPGCharacter target) {
        return (int) (strength * getDamageVariant(target));
    }

    private double getDamageVariant(RPGCharacter target) {
        int levelDifference = level - target.level;
        return  1 - getReduction(levelDifference) + getIncrease(levelDifference);
    }

    private double getReduction(int levelDifference) {
        if (levelDifference <= -5)
            return .5;
        return 0;
    }

    private double getIncrease(int levelDifference) {
        if(levelDifference >= 5)
            return .5;
        return 0;
    }

    private boolean canAttack(RPGCharacter target, int distance) {
        return target != this && distance <= this.maxRange && !isAlly(target);
    }

    class NoMoreFactionException extends RuntimeException {
    }
}
