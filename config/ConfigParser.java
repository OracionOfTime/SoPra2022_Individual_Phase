package de.unisaarland.cs.se.selab.config;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Parses the config file and builds the model from the config using a {@link ModelBuilder}.
 */
public class ConfigParser {

    public static <M> M parse(final String config, final ModelBuilderInterface<M> builder) {
        final JSONObject json = new JSONObject(config);
        builder.setMaxPlayers(json.getInt(ModelBuilderInterface.CFG_MAX_PLAYERS));
        builder.setYears(json.getInt(ModelBuilderInterface.CFG_YEARS));
        builder.setDungeonSideLength(json.getInt(ModelBuilderInterface.CFG_DUNGEON_SIDELENGTH));
        builder.setInitialFood(json.optInt(ModelBuilderInterface.CFG_INITIAL_FOOD, 3));
        builder.setInitialGold(json.optInt(ModelBuilderInterface.CFG_INITIAL_GOLD, 3));
        builder.setInitialImps(json.optInt(ModelBuilderInterface.CFG_INITIAL_IMPS, 3));
        builder.setInitialEvilness(json.optInt(ModelBuilderInterface.CFG_INITIAL_EVILNESS, 5));
        final JSONArray monsters = json.getJSONArray(ModelBuilderInterface.CFG_MONSTERS);
        for (int i = 0; i < monsters.length(); i++) {
            ConfigParser.parseMonster(monsters.getJSONObject(i), builder);
        }
        final JSONArray adventurers = json.getJSONArray(ModelBuilderInterface.CFG_ADVENTURERS);
        for (int i = 0; i < adventurers.length(); i++) {
            ConfigParser.parseAdventurer(adventurers.getJSONObject(i), builder);
        }
        final JSONArray traps = json.getJSONArray(ModelBuilderInterface.CFG_TRAPS);
        for (int i = 0; i < traps.length(); i++) {
            ConfigParser.parseTrap(traps.getJSONObject(i), builder);
        }
        final JSONArray rooms = json.getJSONArray(ModelBuilderInterface.CFG_ROOMS);
        for (int i = 0; i < rooms.length(); i++) {
            ConfigParser.parseRoom(rooms.getJSONObject(i), builder);
        }
        final JSONArray spells = json.getJSONArray(ModelBuilderInterface.CFG_SPELLS);
        for (int i = 0; i < spells.length(); i++) {
            ConfigParser.parseSpell(spells.getJSONObject(i), builder);
        }
        return builder.build();
    }

    private static <M> void parseMonster(final JSONObject json,
                                         final ModelBuilderInterface<M> builder) {
        final int id = json.getInt(ModelBuilderInterface.CFG_ID);
        final int hunger = json.optInt(ModelBuilderInterface.CFG_MONSTER_HUNGER);
        final int damage = json.getInt(ModelBuilderInterface.CFG_MONSTER_DAMAGE);
        final int evilness = json.optInt(ModelBuilderInterface.CFG_MONSTER_EVILNESS);
        final String attack = json.getString(ModelBuilderInterface.CFG_MONSTER_ATK_STRATEGY);
        builder.addMonster(id, hunger, damage, evilness, attack);
    }

    private static <M> void parseAdventurer(final JSONObject json,
                                            final ModelBuilderInterface<M> builder) {
        final int id = json.getInt(ModelBuilderInterface.CFG_ID);
        final int difficulty = json.getInt(ModelBuilderInterface.CFG_ADV_DIFFICULTY);
        final int healthPoints = json.getInt(ModelBuilderInterface.CFG_ADV_HEALTH_POINTS);
        final int healValue = json.optInt(ModelBuilderInterface.CFG_ADV_HEAL_VALUE);
        final int defuseValue = json.optInt(ModelBuilderInterface.CFG_ADV_DEFUSE_VALUE);
        final boolean charge = json.optBoolean(ModelBuilderInterface.CFG_ADV_CHARGE);
        final int magicPoints = json.optInt(ModelBuilderInterface.CFG_MAGIC_POINTS);
        builder.addAdventurer(id, difficulty, healthPoints,
                healValue, defuseValue, charge, magicPoints);
    }

    private static <M> void parseTrap(final JSONObject json,
                                      final ModelBuilderInterface<M> builder) {
        final int id = json.getInt(ModelBuilderInterface.CFG_ID);
        final String attack = json.getString(ModelBuilderInterface.CFG_TRAP_ATK_STRATEGY);
        final int damage = json.getInt(ModelBuilderInterface.CFG_TRAP_DAMAGE);
        if (json.has(ModelBuilderInterface.CFG_TRAP_TARGET)) {
            builder.addTrap(
                    id, attack, damage, json.getInt(ModelBuilderInterface.CFG_TRAP_TARGET));
        } else {
            builder.addTrap(id, attack, damage);
        }
    }

    private static <M> void parseRoom(final JSONObject json,
                                      final ModelBuilderInterface<M> builder) {
        final int id = json.getInt(ModelBuilderInterface.CFG_ID);
        final int activation = json.getInt(ModelBuilderInterface.CFG_ROOM_ACTIVATION);
        final String restriction = json.getString(ModelBuilderInterface.CFG_ROOM_RESTRICTION);
        final int food = json.optInt(ModelBuilderInterface.CFG_ROOM_FOOD);
        final int gold = json.optInt(ModelBuilderInterface.CFG_ROOM_GOLD);
        final int imps = json.optInt(ModelBuilderInterface.CFG_ROOM_IMPS);
        final int niceness = json.optInt(ModelBuilderInterface.CFG_ROOM_NICENESS);
        builder.addRoom(id, activation, restriction, food, gold, imps, niceness);
    }

    private static <M> void parseSpell(final JSONObject json,
                                       final ModelBuilderInterface<M> builder) {
        final int id = json.getInt(ModelBuilderInterface.CFG_ID);
        final String spellType = json.getString(ModelBuilderInterface.CFG_SPELL_TYPE);
        final String bidType = json.getString(ModelBuilderInterface.CFG_SPELL_BID_TYPE);
        final int slot = json.getInt(ModelBuilderInterface.CFG_SPELL_SLOT);
        final int food = json.optInt(ModelBuilderInterface.CFG_SPELL_FOOD, -1);
        final int gold = json.optInt(ModelBuilderInterface.CFG_SPELL_GOLD, -1);
        final int healthPoints = json.optInt(ModelBuilderInterface.CFG_SPELL_HEALTH_POINTS, -1);
        final int healValue = json.optInt(ModelBuilderInterface.CFG_SPELL_HEAL_VALUE, -1);
        final int defuseValue = json.optInt(ModelBuilderInterface.CFG_SPELL_DEFUSE_VALUE, -1);
        final String structureEffect =
                json.optString(ModelBuilderInterface.CFG_STRUCTURE_EFFECT);
        final String bidTypeBlocked =
                json.optString(ModelBuilderInterface.CFG_BID_TYPE_BLOCKED);
        final DataChecker dataChecker = new DataChecker(id, spellType, bidType,
                slot, food, gold, healthPoints, healValue,
                defuseValue, structureEffect, bidTypeBlocked);
        if ("RESOURCE".equals(spellType)) {
            builder.checkResourceSpell(dataChecker);
        } else if ("BUFF".equals(spellType)) {
            builder.checkBuffSpell(dataChecker);
        } else if ("ROOM".equals(spellType)) {
            builder.checkRoomSpell(dataChecker);
        } else if ("BIDDING".equals(spellType)) {
            builder.checkBiddingSpell(dataChecker);
        } else {
            builder.checkStructureSpell(dataChecker);
        }

        /*if ("RESOURCE".equals(spellType)) {
            builder.addSpell(id, spellType, bidType, slot, food, gold);
        } else if ("BUFF".equals(spellType)) {
            builder.addSpell(id, spellType, bidType, slot, healthPoints, healValue, defuseValue);
        } else if ("STRUCTURE".equals(spellType)) {
            builder.addSpell(id, spellType, bidType, slot, structureEffect);
        } else if ("BIDDING".equals(spellType)) {
            builder.addSpell(id, spellType, bidType, slot, bidTypeBlocked);
        } else {
            builder.addSpell(id, spellType, bidType, slot);
        }*/
    }
}
