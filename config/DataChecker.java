package de.unisaarland.cs.se.selab.config;

/**
 * Collects all parameters for spells
 * Checks validity of each spell
 */
public class DataChecker {
    private final int id;
    private final String spellType;
    private final String bidType;
    private final int slot;
    private int food;
    private int gold;
    private int healthPoints;
    private int healValue;
    private int defuseValue;
    private final String structureEffect;
    private final String bidTypeBlocked;

    public DataChecker(final int id, final String spellType,
                       final String bidType, final int slot,
                       final int food, final int gold,
                       final int healthPoints,
                       final int healValue, final int defuseValue,
                       final String structureEffect,
                       final String bidTypeBlocked) {
        this.id = id;
        this.spellType = spellType;
        this.bidType = bidType;
        this.slot = slot;
        this.food = food;
        this.gold = gold;
        this.healthPoints = healthPoints;
        this.healValue = healValue;
        this.defuseValue = defuseValue;
        this.structureEffect = structureEffect;
        this.bidTypeBlocked = bidTypeBlocked;
    }

    public int getId() {
        return id;
    }

    public String getSpellType() {
        return spellType;
    }

    public String getBidType() {
        return bidType;
    }

    public int getSlot() {
        return slot;
    }

    public int getFood() {
        return food;
    }

    public int getGold() {
        return gold;
    }

    public String getStructureEffect() {
        return structureEffect;
    }

    public String getBidTypeBlocked() {
        return bidTypeBlocked;
    }

    public int getHealthPoints() {
        return this.healthPoints;
    }

    public int getHealValue() {
        return this.healValue;
    }

    public int getDefuseValue() {
        return this.defuseValue;
    }

    public void checkResource() {
        if (this.food == -1) {
            this.food = 0;
        }
        if (this.gold == -1) {
            this.gold = 0;
        }
        if ((this.food == 0 && this.gold == 0)
                || (this.healthPoints != -1 || this.healValue != -1 || this.defuseValue != -1
                || !this.structureEffect.isEmpty() || !this.bidTypeBlocked.isEmpty())) {
            throw new IllegalArgumentException();
        }
    }

    public void checkBuff() {
        if (this.healthPoints == -1) {
            this.healthPoints = 0;
        }
        if (this.healValue == -1) {
            this.healValue = 0;
        }
        if (this.defuseValue == -1) {
            this.defuseValue = 0;
        }
        if ((this.healthPoints == 0 && this.healValue == 0
                && this.defuseValue == 0)
                || (this.food != -1 || this.gold != -1
                || !this.structureEffect.isEmpty() || !this.bidTypeBlocked.isEmpty())) {
            throw new IllegalArgumentException();
        }
    }

    public void checkRoom() {
        if (this.food != -1 || this.gold != -1
                || this.healthPoints != -1 || this.healValue != -1 || this.defuseValue != -1
                || !this.structureEffect.isEmpty() || !this.bidTypeBlocked.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public void checkBidding() {
        if (this.food != -1 || this.gold != -1
                || this.healthPoints != -1 || this.healValue != -1 || this.defuseValue != -1
                || !this.structureEffect.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public void checkStructure() {
        if (this.food != -1 || this.gold != -1
                || this.healthPoints != -1 || this.healValue != -1 || this.defuseValue != -1
                || !this.bidTypeBlocked.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
