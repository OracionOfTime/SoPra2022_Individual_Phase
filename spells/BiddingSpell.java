package de.unisaarland.cs.se.selab.spells;

import de.unisaarland.cs.se.selab.ConnectionWrapper;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.commands.ActionResult;
import de.unisaarland.cs.se.selab.model.Model;
import de.unisaarland.cs.se.selab.model.Player;
import de.unisaarland.cs.se.selab.state.CombatState;
import de.unisaarland.cs.se.selab.state.State;

public class BiddingSpell extends Spell {

    private BidType lockedBid;

    public BiddingSpell(final int id, final SpellType valueOf,
                        final BidType valueOf1, final int slot, final BidType valueOf2) {
        super(id, valueOf, valueOf1, slot);
        this.lockedBid = valueOf2;
    }

    /**
     *If enough magic points, no counter spell cast,
     * not final year or this.bidtype not already locked by spell,
     * locks this.bidtype for next year
     */
    @Override
    public ActionResult castSpell(final Model model, final ConnectionWrapper connection,
                                  final Player player, final CombatState state) {
        final int originalCounterSpells = player.getDungeon().getCounterSpells();
        if (player.getDungeon().getTotalMagicPoints() >= 4) {
            connection.sendSpellCast(this.getId(), player.getId());
            player.setWithstoodSpells(player.getWithstoodSpells() + 1);
            //check for countered spell
            final ActionResult action = castCounterSpell(model, player,
                    connection, State.Phase.COUNTERING_SPELLS);
            if (!player.isAlive()) {
                return action;
            }
            if (model.getYear() < model.getMaxYear() && !(checkForLockedBids(player, model))) {
                if (originalCounterSpells == player.getDungeon().getCounterSpells()) {
                    connection.sendBidTypeBlocked(player.getId(), this.lockedBid, model.getRound());
                    player.lockWithSpell(this.lockedBid, model.getRound());
                }
            }
        }
        return ActionResult.PROCEED;
    }

    private boolean checkForLockedBids(final Player player, final Model model) {
        return player.getLockedBySpell().get(model.getRound())
                .contains(this.lockedBid);
    }

    public BidType getLockedBid() {
        return lockedBid;
    }

    public void setLockedBid(final BidType lockedBid) {
        this.lockedBid = lockedBid;
    }
}
