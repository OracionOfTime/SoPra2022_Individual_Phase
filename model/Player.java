package de.unisaarland.cs.se.selab.model;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.model.dungeon.Dungeon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * This holds all player-related data.
 */
public class Player {

    public static final int MIN_EVILNESS = 0;
    public static final int MAX_EVILNESS = 15;

    private final int id;
    private final String name;
    private final List<Monster> monsters;
    private final List<Trap> traps;
    private final Dungeon dungeon;
    private final List<Optional<BidType>> bids;
    private final List<BidType> lockedTypes;

    //Map of size MAX_ROUNDS to check bids locked by spell
    private final Map<Integer, List<BidType>> lockedBySpell;
    private int evilness;
    private int imps;
    private int gold;
    private int food;
    private int numTunnelDigsAllowed;
    private boolean alive;
    private int scorePoints;

    //counters for evaluation of titles
    private int withstoodSpells;
    private int withstoodMage;
    private int numberOfCounterSpells;

    public Player(final int id,
                  final String name,
                  final int initialFood,
                  final int initialGold,
                  final int initialImps,
                  final int initialEvilness,
                  final Dungeon dungeon) {
        this.id = id;
        this.name = name;
        this.monsters = new ArrayList<>();
        this.traps = new ArrayList<>();
        this.dungeon = dungeon;
        this.lockedTypes = new ArrayList<>();
        this.lockedBySpell = initiliazeMap();
        this.food = initialFood;
        this.gold = initialGold;
        this.imps = initialImps;
        this.evilness = initialEvilness;
        this.bids = new ArrayList<>(Model.BID_LIMIT);
        this.alive = true;
        clearBidTypes();
    }

    private Map<Integer, List<BidType>> initiliazeMap() {
        final Map<Integer, List<BidType>> lockedBidsBySpell = new HashMap<>(Model.MAX_ROUNDS);
        for (int i = 1; i <= Model.MAX_ROUNDS; i++) {
            final List<BidType> bids = new ArrayList<>();
            lockedBidsBySpell.put(i, bids);
        }
        return lockedBidsBySpell;
    }

    /**
     * After each Building Phase, Map of locked Bids by Spells is cleaned
     */
    public void clearSpelledBids() {
        for (int i = 0; i < Model.MAX_ROUNDS; i++) {
            final List<BidType> value = this.lockedBySpell.get(i);
            if (value != null) {
                this.lockedBySpell.get(i).clear();
            }
        }
    }

    public final int getId() {
        return this.id;
    }

    public final String getName() {
        return this.name;
    }

    /**
     * Total number of used counter spells
     */
    public int getNumberOfCounterSpells() {
        return numberOfCounterSpells;
    }

    public void setNumberOfCounterSpells(final int numberOfCounterSpells) {
        this.numberOfCounterSpells = numberOfCounterSpells;
    }

    /**
     *Total number of how often Arch mage was triggered
     */
    public int getWithstoodMage() {
        return withstoodMage;
    }

    public void setWithstoodMage(final int withstoodMage) {
        this.withstoodMage = withstoodMage;
    }

    /**
     *Total number of cast spells
     */
    public int getWithstoodSpells() {
        return withstoodSpells;
    }

    public void setWithstoodSpells(final int withstoodSpells) {
        this.withstoodSpells = withstoodSpells;
    }

    /**
     * Place a bid a player sent via a place bid command.
     *
     * @param type     the bid's type
     * @param priority the bid's priority, i.e., whether it is the first, second, or third bid
     * @return {@code true} if the bid could be placed or {@code false} if the bid or priority is
     * not available
     */
    public final boolean placeBid(final BidType type, final int priority) {
        // Bid priorities start at 1 but list indices start at 0, hence the -1
        if (this.bids.get(priority - 1).isEmpty() && checkBidType(type)) {
            this.bids.set(priority - 1, Optional.of(type));
            return true;
        }
        return false;
    }

    /**
     * Check whether the given bid type was already placed.
     *
     * @param type the bid type to check.
     * @return whether the bid is still available for placing, or not
     */
    private boolean checkBidType(final BidType type) {
        return getPlacedBidTypes().stream().noneMatch(placedBid -> placedBid == type);
    }

    /**
     * Check whether the player is still alive and finished with bidding.
     *
     * @return whether the player is finished with bidding
     */
    public final boolean finishedBidding() {
        return isAlive() && !hasToBid();
    }

    /**
     * Check whether the player still has to bid.
     *
     * @return whether the player still has to bid
     */
    public final boolean hasToBid() {
        return isAlive() && getPlacedBidTypes().size() != Model.BID_LIMIT;
    }

    /**
     * Get all placed bid types for this player.
     *
     * @return the list of placed bid types
     */
    public final List<BidType> getPlacedBidTypes() {
        return this.bids.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    /**
     * Clear the list of placed bids.
     */
    public final void clearBidTypes() {
        this.bids.clear();
        for (int i = 0; i < Model.BID_LIMIT; i++) {
            this.bids.add(Optional.empty());
        }
    }

    /**
     * Lock the bids of second and third priority.
     */
    public final void lockBidTypes() {
        // Bid priorities start at 1 but list indices start at 0, hence the off-by-one
        this.bids.get(1).ifPresent(this.lockedTypes::add);
        this.bids.get(2).ifPresent(this.lockedTypes::add);
    }

    /**
     * If Bidding-Spell was cast, bid to be locked is added to Map
     *
     * @param bidType bid to be locked
     * @param round round in which Bidding-Spell was cast
     */
    public final void lockWithSpell(final BidType bidType, final int round) {
        this.lockedBySpell.get(round).add(bidType);
    }

    /**
     * Unlock previously locked bid types.
     */
    public final void unlockBidTypes() {
        this.lockedTypes.clear();
    }

    /**
     * Check whether the given bid type is locked.
     *
     * @param type the bid type to check
     * @return whether the bid type is locked
     */
    public final boolean isLocked(final BidType type) {
        return this.lockedTypes.contains(type);
    }

    /**
     *
     * @param bidType bid to be checked on
     * @param round round to be checked
     * @return if type of bid is locked by a spell for this round
     */
    public final boolean isSpelledBid(final BidType bidType, final int round) {
        return this.lockedBySpell.get(round).contains(bidType);
    }

    /**
     * Get a list of currently locked bid types.
     *
     * @return the list of locked bid types
     */
    public final List<BidType> getLockedTypes() {
        return this.lockedTypes;
    }

    public final Map<Integer, List<BidType>> getLockedBySpell() {
        return this.lockedBySpell;
    }

    public int getEvilness() {
        return this.evilness;
    }

    public void changeEvilness(final int evilness) {
        this.evilness += evilness;
    }

    public int getImps() {
        return this.imps;
    }

    public void changeImps(final int imps) {
        this.imps += imps;
    }

    public int getGold() {
        return this.gold;
    }

    public void setGold(final int amount) {
        this.gold = amount;
    }

    public void changeGold(final int gold) {
        this.gold += gold;
    }

    public int getFood() {
        return this.food;
    }

    public void setFood(final int amount) {
        this.food = amount;
    }

    public void changeFood(final int food) {
        this.food += food;
    }

    public final List<Monster> getMonsters() {
        return this.monsters;
    }

    public final Optional<Monster> getMonster(final int monsterId) {
        return this.monsters.stream()
                .filter(monster -> monster.getId() == monsterId)
                .findFirst();
    }

    /**
     * Make all monsters available for being set on defense again.
     */
    public final void wakeUpMonsters() {
        this.monsters.forEach(monster -> monster.setUsed(false));
    }

    public final void addMonster(final Monster monster) {
        this.monsters.add(monster);
    }

    public List<Trap> getTraps() {
        return this.traps;
    }

    public final Optional<Trap> getTrap(final int trapId) {
        return this.traps.stream()
                .filter(trap -> trap.getId() == trapId)
                .findFirst();
    }

    public void addTrap(final Trap trap) {
        this.traps.add(trap);
    }

    /**
     * Get the number of tunnels the player is allowed to dig.
     *
     * @return the number of tunnels a player may dig
     */
    public final int getNumTunnelDigsAllowed() {
        return this.numTunnelDigsAllowed;
    }

    /**
     * Set the number of tunnels the player is allowed to dig.
     *
     * @param numTunnelDigsAllowed the number of tunnels a player may dig
     */
    public final void setNumTunnelDigsAllowed(final int numTunnelDigsAllowed) {
        this.numTunnelDigsAllowed = numTunnelDigsAllowed;
    }

    /**
     * Reduces the number of tunnels the player is allowed to dig by 1.
     */
    public final void digTunnel() {
        this.numTunnelDigsAllowed--;
    }

    public final Dungeon getDungeon() {
        return this.dungeon;
    }

    /**
     * Indicate that this player has left the game.
     */
    public void kill() {
        this.alive = false;
    }

    /**
     * Check whether this player still participates in the game.
     *
     * @return whether this player still participates in the game
     */
    public boolean isAlive() {
        return this.alive;
    }

    public int getScorePoints() {
        return this.scorePoints;
    }

    public void changeScorePoints(final int amount) {
        this.scorePoints += amount;
    }

}
