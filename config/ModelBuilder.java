package de.unisaarland.cs.se.selab.config;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.model.Adventurer;
import de.unisaarland.cs.se.selab.model.AttackStrategy;
import de.unisaarland.cs.se.selab.model.Model;
import de.unisaarland.cs.se.selab.model.Monster;
import de.unisaarland.cs.se.selab.model.Trap;
import de.unisaarland.cs.se.selab.model.dungeon.Room;
import de.unisaarland.cs.se.selab.spells.BiddingSpell;
import de.unisaarland.cs.se.selab.spells.Buff;
import de.unisaarland.cs.se.selab.spells.Resource;
import de.unisaarland.cs.se.selab.spells.RoomSpell;
import de.unisaarland.cs.se.selab.spells.Spell;
import de.unisaarland.cs.se.selab.spells.SpellType;
import de.unisaarland.cs.se.selab.spells.Structural;
import de.unisaarland.cs.se.selab.spells.StructureEffect;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * The model builder collects all the data from the parsed config file and creates the model when
 * {@link ModelBuilder#build()} is called.
 */
public class ModelBuilder implements ModelBuilderInterface<Model> {

    private final List<Adventurer> adventurers;
    private final List<Monster> monsters;
    private final List<Trap> traps;
    private final List<Room> rooms;
    private final List<Spell> spells;
    private int maxPlayers;
    private long randomSeed;
    private int years;
    private int dungeonSideLength;
    private int initialFood;
    private int initialGold;
    private int initialImps;
    private int initialEvilness;

    public ModelBuilder() {
        this.adventurers = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.traps = new ArrayList<>();
        this.rooms = new ArrayList<>();
        this.spells = new ArrayList<>();
    }

    @Override
    public void addMonster(final int id, final int hunger, final int damage, final int evilness,
                           final String attack) {
        this.monsters.add(
                new Monster(id, hunger, damage, evilness, AttackStrategy.valueOf(attack)));
    }

    @Override
    public void addAdventurer(final int id, final int difficulty, final int healthPoints,
                              final int healValue, final int defuseValue,
                              final boolean charge, final int magicPoints) {
        this.adventurers.add(new Adventurer(id, difficulty,
                healthPoints, healValue, defuseValue, charge, magicPoints));
    }

    @Override
    public void addTrap(final int id, final String attack, final int damage) {
        this.traps.add(new Trap(id, AttackStrategy.valueOf(attack), damage));
    }

    @Override
    public void addTrap(final int id, final String attack, final int damage, final int target) {
        this.traps.add(new Trap(id, AttackStrategy.valueOf(attack), damage, target));
    }

    @Override
    public void addRoom(final int id, final int activation, final String restriction,
                        final int food, final int gold, final int imps, final int niceness) {
        final EnumMap<BidType, Integer> production = new EnumMap<>(BidType.class);
        if (food > 0) {
            production.put(BidType.FOOD, food);
        }
        if (gold > 0) {
            production.put(BidType.GOLD, gold);
        }
        if (imps > 0) {
            production.put(BidType.IMPS, imps);
        }
        if (niceness > 0) {
            production.put(BidType.NICENESS, niceness);
        }
        this.rooms.add(new Room(id, activation, Room.BuildingRestriction.valueOf(restriction),
                production));
    }

    @Override
    public void checkResourceSpell(final DataChecker dataChecker) {
        this.spells.add(new Resource(dataChecker.getId(),
                SpellType.valueOf(dataChecker.getSpellType()),
                BidType.valueOf(dataChecker.getBidType()), dataChecker.getSlot(),
                dataChecker.getFood(), dataChecker.getGold()));
    }

    @Override
    public void checkBuffSpell(final DataChecker dataChecker) {
        this.spells.add(new Buff(dataChecker.getId(), SpellType.valueOf(dataChecker.getSpellType()),
                BidType.valueOf(dataChecker.getBidType()), dataChecker.getSlot(),
                dataChecker.getHealthPoints(), dataChecker.getHealValue(),
                dataChecker.getDefuseValue()));
    }

    @Override
    public void checkRoomSpell(final DataChecker dataChecker) {
        this.spells.add(new RoomSpell(dataChecker.getId(),
                SpellType.valueOf(dataChecker.getSpellType()),
                BidType.valueOf(dataChecker.getBidType()), dataChecker.getSlot()));
    }

    @Override
    public void checkBiddingSpell(final DataChecker dataChecker) {
        this.spells.add(new BiddingSpell(dataChecker.getId(),
                SpellType.valueOf(dataChecker.getSpellType()),
                BidType.valueOf(dataChecker.getBidType()), dataChecker.getSlot(),
                BidType.valueOf(dataChecker.getBidTypeBlocked())));
    }

    @Override
    public void checkStructureSpell(final DataChecker dataChecker) {
        this.spells.add(new Structural(dataChecker.getId(),
                SpellType.valueOf(dataChecker.getSpellType()),
                BidType.valueOf(dataChecker.getBidType()), dataChecker.getSlot(),
                StructureEffect.valueOf(dataChecker.getStructureEffect())));
    }

    /*@Override
    public void addSpell(final int id, final String spellType, final String bidType,
                         final int slot, final int food, final int gold) {
        this.spells.add(new Resource(id, SpellType.valueOf(spellType), BidType.valueOf(bidType),
                slot, food, gold));
    }

    @Override
    public void addSpell(final int id, final String spellType, final String bidType,
                         final int slot, final int healthPoints,
                         final int healValue, final int defuseValue) {
        this.spells.add(new Buff(id, SpellType.valueOf(spellType), BidType.valueOf(bidType),
                slot, healthPoints, healValue, defuseValue));
    }

    @Override
    public void addSpell(final int id, final String spellType, final String bidType,
                         final int slot, final String bidOrStructure) {
        if ("DESTROY".equals(bidOrStructure) || "CONQUER".equals(bidOrStructure)) {
            this.spells.add(new Structural(id, SpellType.valueOf(spellType),
                    BidType.valueOf(bidType),
                    slot, StructureEffect.valueOf(bidOrStructure)));
        } else if ("FOOD".equals(bidOrStructure) || "GOLD".equals(bidOrStructure)
                || "TUNNEL".equals(bidOrStructure) || "IMPS".equals(bidOrStructure)
                || "NICENESS".equals(bidOrStructure) || "TRAP".equals(bidOrStructure)
                || "MONSTER".equals(bidOrStructure) || "ROOM".equals(bidOrStructure)) {
            this.spells.add(new BiddingSpell(id,
                    SpellType.valueOf(spellType), BidType.valueOf(bidType),
                    slot, BidType.valueOf(bidOrStructure)));
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void addSpell(final int id, final String spellType, final String bidType,
                         final int slot) {
        this.spells.add(new RoomSpell(id, SpellType.valueOf(spellType), BidType.valueOf(bidType),
                slot));
    }*/

    @Override
    public void setMaxPlayers(final int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    @Override
    public void setSeed(final long seed) {
        this.randomSeed = seed;
    }

    @Override
    public void setYears(final int years) {
        this.years = years;
    }

    @Override
    public void setDungeonSideLength(final int dungeonSideLength) {
        this.dungeonSideLength = dungeonSideLength;
    }

    @Override
    public void setInitialFood(final int food) {
        this.initialFood = food;
    }

    @Override
    public void setInitialGold(final int gold) {
        this.initialGold = gold;
    }

    @Override
    public void setInitialImps(final int imps) {
        this.initialImps = imps;
    }

    @Override
    public void setInitialEvilness(final int evilness) {
        this.initialEvilness = evilness;
    }

    @Override
    public Model build() {
        return new Model(
                this.monsters,
                this.adventurers,
                this.traps,
                this.rooms,
                this.spells,
                this.randomSeed,
                this.maxPlayers,
                this.years,
                this.dungeonSideLength,
                this.initialFood,
                this.initialGold,
                this.initialImps,
                this.initialEvilness);
    }
}
