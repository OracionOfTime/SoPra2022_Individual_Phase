package de.unisaarland.cs.se.selab.model;

/**
 * Class for adventurers.
 */
public class Adventurer {

    private final int id;
    private final int difficulty;
    private final boolean charge;
    private final int maxHealthPoints;
    private final int magicPoints;
    private int healValue;
    private int defuseValue;
    private int currentHealthPoints;
    private boolean defeated;

    //the healValue given by the Buff-Spell
    private int buffedHealValue;

    //the defuseValue given by the Buff-Spell
    private int buffedDefuseValue;

    public Adventurer(final int id, final int difficulty, final int healthPoints,
                      final int healValue, final int defuseValue, final boolean charge,
                      final int magicPoints) {
        this.id = id;
        this.difficulty = difficulty;
        this.maxHealthPoints = healthPoints;
        this.currentHealthPoints = this.maxHealthPoints;
        this.healValue = healValue;
        this.defuseValue = defuseValue;
        this.charge = charge;
        this.defeated = false;
        this.buffedHealValue = 0;
        this.buffedDefuseValue = 0;
        this.magicPoints = magicPoints;
    }

    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public int getCurrentHealthPoints() {
        return currentHealthPoints;
    }

    public void setCurrentHealthPoints(final int currentHealthPoints) {
        this.currentHealthPoints = currentHealthPoints;
    }

    public void setBuffedHealValue(final int healValue) {
        this.buffedHealValue = healValue;
    }

    public void setBuffedDefuseValue(final int defuseValue) {
        this.buffedDefuseValue = defuseValue;
    }

    public int getId() {
        return id;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getHealValue() {
        return healValue;
    }

    public void setHealValue(final int healValue) {

        this.healValue = healValue;
    }

    public int getDefuseValue() {
        return defuseValue;
    }

    public void setDefuseValue(final int defuseValue) {

        this.defuseValue = defuseValue;
    }

    public int getMagicPoints() {
        return this.magicPoints;
    }

    /**
     * Heal an adventurer by the given amount.
     * <p>
     * This performs a bounds-check for the adventurers maximal health points before healing.
     * </p>
     *
     * @param amount the amount to heal
     * @return the actual amount of health points the player was healed (after bounds-check)
     */
    public int heal(final int amount) {
        final int effectiveHeal = Math.min(this.maxHealthPoints - this.currentHealthPoints, amount);
        this.currentHealthPoints += effectiveHeal;
        return effectiveHeal;
    }

    /**
     * Damage an adventurer by the given amount.
     * <p>
     * This performs a bounds-check for the adventurers minimal health points before damaging and
     * sets the adventurer's defeated flag if the health points drop to 0.
     * </p>
     *
     * @param amount the amount of damage
     * @return the actual amount of health points the player was damaged (after bounds-check)
     */
    public int damage(final int amount) {
        final int effectiveDamage = Math.min(this.currentHealthPoints, amount);
        this.currentHealthPoints -= effectiveDamage;
        if (this.currentHealthPoints <= 0) {
            this.defeated = true;
        }
        return effectiveDamage;
    }

    public boolean isDefeated() {
        return this.defeated;
    }

    public boolean isCharging() {
        return this.charge;
    }

    /**
     *Three functions to buff the attributes of adventurer
     * Updates 'buffed'-fields in case of stacking of Buff-Spell
     */
    public void buffHealthPoints(final int buffedHealthPoints) {
        this.currentHealthPoints += buffedHealthPoints;
    }

    public void buffDefuseValue(final int buffedDefuseValue) {
        this.defuseValue += buffedDefuseValue;
        this.buffedDefuseValue += buffedDefuseValue;
    }

    public void buffHealValue(final int buffedHealValue) {
        this.healValue += buffedHealValue;
        this.buffedHealValue += buffedHealValue;
    }

    /**
     * Three functions to 'unbuff' adventurer's attributes
     * If currentHealthPoints are above the maximum, they are set back to max
     *
     */
    public void unBuffHealthPoints() {
        if (this.currentHealthPoints > this.maxHealthPoints) {
            this.currentHealthPoints = this.maxHealthPoints;
        }
    }

    public void unBuffDefuseValue() {
        this.defuseValue -= this.buffedDefuseValue;
    }

    public void unBuffHealValue() {
        this.healValue -= this.buffedHealValue;
    }
}
