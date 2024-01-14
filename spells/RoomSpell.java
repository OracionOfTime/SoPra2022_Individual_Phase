package de.unisaarland.cs.se.selab.spells;

import de.unisaarland.cs.se.selab.ConnectionWrapper;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.commands.ActionResult;
import de.unisaarland.cs.se.selab.model.Model;
import de.unisaarland.cs.se.selab.model.Player;
import de.unisaarland.cs.se.selab.state.CombatState;
import de.unisaarland.cs.se.selab.state.State;

public class RoomSpell extends Spell {
    public RoomSpell(final int id, final SpellType valueOf,
                     final BidType valueOf1, final int slot) {
        super(id, valueOf, valueOf1, slot);
    }

    /**
     * If enough magic points, no counter spell cast and a room spell has not been already cast,
     * rooms are blocked for next year's given round
     */
    @Override
    public ActionResult castSpell(final Model model,
                                  final ConnectionWrapper connection,
                                  final Player player, final CombatState state) {
        final int originalCounterSpells = player.getNumberOfCounterSpells();
        if (player.getDungeon().getTotalMagicPoints() >= 3) {
            connection.sendSpellCast(this.getId(), player.getId());
            player.setWithstoodSpells(player.getWithstoodSpells() + 1);
            //check for countering
            final ActionResult action = castCounterSpell(model, player,
                    connection, State.Phase.COUNTERING_SPELLS);
            if (!player.isAlive()) {
                return action;
            }
            if (model.getYear() < model.getMaxYear()
                    && !(player.getDungeon().getGraph().getBlockedRound().get(model.getRound()))) {
                if (originalCounterSpells == player.getNumberOfCounterSpells()) {
                    connection.sendRoomsBlocked(player.getId(), model.getRound());
                    player.getDungeon().getGraph().setBlockedRooms(model.getRound(), true);
                }
            }
        }
        return ActionResult.PROCEED;
    }
}

