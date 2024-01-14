package de.unisaarland.cs.se.selab.spells;

import de.unisaarland.cs.se.selab.ConnectionUtils;
import de.unisaarland.cs.se.selab.ConnectionWrapper;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.commands.ActionResult;
import de.unisaarland.cs.se.selab.model.Model;
import de.unisaarland.cs.se.selab.model.Player;
import de.unisaarland.cs.se.selab.state.CombatState;
import de.unisaarland.cs.se.selab.state.State;

/**
 * Parent class for all types of spells, which is being extended by them
 * Has all shared attributes of every spell type
 */
public abstract class Spell {

    private int round;
    private int id;
    private SpellType spellType;
    private BidType bidType;
    private int slot;

    public Spell(final int id, final SpellType spellType, final BidType bidType, final int slot) {
        this.id = id;
        this.spellType = spellType;
        this.bidType = bidType;
        this.slot = slot;
        this.round = 0;
    }

    public int getRound() {
        return round;
    }

    public void setRound(final int round) {
        this.round = round;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public SpellType getSpellType() {
        return spellType;
    }

    public void setSpellType(final SpellType spellType) {
        this.spellType = spellType;
    }

    public BidType getBidType() {
        return bidType;
    }

    public void setBidType(final BidType bidType) {
        this.bidType = bidType;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(final int slot) {
        this.slot = slot;
    }

    /**
     * Default implementation of castSpell function, used in all extending classes
     * When called, it casts the effect of the respective spell
     */
    public ActionResult castSpell(final Model model,
                                  final ConnectionWrapper connectionWrapper,
                                  final Player player, final CombatState state) {
        return ActionResult.PROCEED;
    }

    /**
     * If player has enough counter spells, they are asked for countering
     */
    public ActionResult castCounterSpell(final Model model, final Player player,
                                         final ConnectionWrapper connection,
                                         final State.Phase state) {
        if (player.getDungeon().getCounterSpells() >= 1) {
            connection.sendCounterSpell(player.getId());
            final ActionResult counterSpellResult =
                    ConnectionUtils.executePlayerCommand(model, connection,
                    state, player);
            if (!player.isAlive()) {
                return counterSpellResult;
            }
        }
        return ActionResult.PROCEED;
    }
}

