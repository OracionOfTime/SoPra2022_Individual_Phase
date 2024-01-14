package de.unisaarland.cs.se.selab.model.dungeon;

import de.unisaarland.cs.se.selab.model.Adventurer;
import de.unisaarland.cs.se.selab.spells.Spell;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

/**
 * This holds all dungeon-related data.
 */
public class Dungeon {

    private final List<Adventurer> queuingAdventurers;
    private final List<Adventurer> prison;
    private final TunnelGraph graph;
    private final Queue<Spell> spells;
    private Tunnel battleGround;
    private int supervisingImps;

    //checks if Arch mage arrived
    private boolean linusArrived;

    //counter spells used in combat
    private int counterSpells;

    //magic points of all active adventurers
    private int totalMagicPoints;

    public Dungeon() {
        this.queuingAdventurers = new ArrayList<>();
        this.prison = new ArrayList<>();
        this.graph = new TunnelGraph();
        this.spells = new LinkedList<>();
        this.linusArrived = false;
        this.counterSpells = 0;
        this.totalMagicPoints = 0;
    }


    public int getTotalMagicPoints() {
        return totalMagicPoints;
    }

    public void setTotalMagicPoints(final int totalMagicPoints) {

        this.totalMagicPoints = totalMagicPoints;
    }

    public void addSpell(final Spell spell) {
        this.spells.add(spell);
    }

    public Queue<Spell> getSpells() {
        return this.spells;
    }

    public int getCounterSpells() {
        return this.counterSpells;
    }

    public void setCounterSpells(final int spells) {
        this.counterSpells = spells;
    }

    public boolean getLinusArrived() {
        return this.linusArrived;
    }

    public void setLinusArrived(final boolean linusArrived) {
        this.linusArrived = linusArrived;
    }

    public Optional<Tunnel> getBattleGround() {
        return Optional.ofNullable(this.battleGround);
    }

    public void setBattleGround(final Tunnel battleGround) {
        this.battleGround = battleGround;
    }

    public TunnelGraph getGraph() {
        return this.graph;
    }

    public void addAdventurer(final Adventurer adventurer) {
        if (adventurer.isCharging()) {
            this.queuingAdventurers.add(0, adventurer);
        } else {
            this.queuingAdventurers.add(adventurer);
        }
    }

    /**
     * Calculates all magic points
     * (+3 if Arch mage active)
     */
    public void calculateTotalMagicPoints() {
        int add = 0;
        if (this.getLinusArrived()) {
            add += 3;
        }
        this.totalMagicPoints = this.getAllAdventurers()
                    .stream()
                    .map(Adventurer::getMagicPoints)
                    .reduce(0, Integer::sum);
        this.totalMagicPoints += add;
    }

    private void removeDefeatedAdventurers() {
        final List<Adventurer> defeatedAdventurers = this.queuingAdventurers
                .stream()
                .filter(Adventurer::isDefeated)
                .toList();
        this.queuingAdventurers.removeAll(defeatedAdventurers);
    }

    /**
     * Clear all adventurers from the dungeon.
     */
    public void clearAdventurers() {
        this.queuingAdventurers.clear();
    }

    /**
     * Get the adventurer at the given position.
     *
     * @param target the adventurer's position
     * @return the selected adventurer if available
     */
    public Optional<Adventurer> getAdventurer(final int target) {
        if (getAllAdventurers().size() < target) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(getAllAdventurers().get(target - 1));
        }
    }

    /**
     * Get all adventurers that are still alive in the dungeon.
     *
     * @return a list of alive adventurers
     */
    public List<Adventurer> getAllAdventurers() {
        removeDefeatedAdventurers();
        return new ArrayList<>(this.queuingAdventurers);
    }

    /**
     * Check whether there are any adventurers left in the dungeon.
     *
     * @return whether there are any adventurers left
     */
    public boolean adventurersLeft() {
        return !getAllAdventurers().isEmpty();
    }

    /**
     * Get the number of conquered tiles (rooms and tunnels).
     *
     * @return the number of conquered tiles
     */
    public final int getNumUnconqueredTiles() {
        return (int) this.graph.stream().filter(tunnel -> !tunnel.isConquered()).count();
    }

    /**
     * Check whether all tunnels and rooms in the dungeon are conquered.
     *
     * @return whether all tunnels and rooms are conquered
     */
    public final boolean isConquered() {
        return getNumUnconqueredTiles() == 0;
    }

    /**
     * Conquer the current battleground.
     */
    public final void conquer() {
        if (this.battleGround != null) {
            this.battleGround.conquer();
        }
    }

    /**
     * Assign an imp for supervision.
     */
    public void addSupervision() {
        this.supervisingImps++;
    }

    /**
     * Return all supervising imps from supervision.
     *
     * @return the number of imps that were supervising
     */
    private int returnSupervision() {
        final int supervisors = this.supervisingImps;
        this.supervisingImps = 0;
        return supervisors;
    }

    /**
     * Return all mining imps from their jobs.
     *
     * @return the number of imps returning from mining jobs
     */
    public int returnMiners() {
        return returnSupervision() + this.graph
                .map(Tunnel::returnMiners)
                .stream()
                .reduce(0, Integer::sum);
    }

    /**
     * Imprison the given adventurer.
     *
     * @param adventurer the defeated adventurer
     */
    public void imprisonAdventurer(final Adventurer adventurer) {
        this.prison.add(adventurer);
    }

    /**
     * Free the first adventurer from prison.
     *
     * @return the freed adventurer
     */
    public Adventurer freeAdventurer() {
        return this.prison.remove(0);
    }

    /**
     * Get all imprisoned adventurers.
     *
     * @return a list of imprisoned adventurers
     */
    public List<Adventurer> getPrison() {
        return this.prison;
    }

    /**
     * Get the number of imprisoned adventurers.
     *
     * @return the number of imprisoned adventurers
     */
    public int getNumImprisonedAdventurers() {
        return this.prison.size();
    }

}
