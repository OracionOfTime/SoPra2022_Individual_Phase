package de.unisaarland.cs.se.selab.commands;

import de.unisaarland.cs.se.selab.ConnectionWrapper;
import de.unisaarland.cs.se.selab.model.Model;
import de.unisaarland.cs.se.selab.model.Player;
import de.unisaarland.cs.se.selab.state.State;
import java.util.Set;

public class CounterSpellCommand extends PlayerCommand {

    public CounterSpellCommand(final int playerId) {
        super(playerId);
    }


    @Override
    public Set<State.Phase> inPhase() {
        return Set.of(State.Phase.COUNTERING_SPELLS);
    }

    /**
     * Decreases player's counter spell
     * Updates the number of counter spells used
     */
    @Override
    protected ActionResult run(final Model model, final ConnectionWrapper connection) {
        final Player player = model.getPlayerById(getId());
        player.getDungeon().setCounterSpells(player.getDungeon().getCounterSpells() - 1);
        player.setNumberOfCounterSpells(player.getNumberOfCounterSpells() + 1);
        connection.sendCounterSpellCast(player.getId());
        return ActionResult.PROCEED;
    }
}
