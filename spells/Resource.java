package de.unisaarland.cs.se.selab.spells;

import de.unisaarland.cs.se.selab.ConnectionWrapper;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.commands.ActionResult;
import de.unisaarland.cs.se.selab.model.Model;
import de.unisaarland.cs.se.selab.model.Player;
import de.unisaarland.cs.se.selab.state.CombatState;
import de.unisaarland.cs.se.selab.state.State;

public class Resource extends Spell {

    private int food;
    private int gold;

    public Resource(final int id, final SpellType valueOf, final BidType valueOf1,
                    final int slot, final int food, final int gold) {
        super(id, valueOf, valueOf1, slot);
        this.food = food;
        this.gold = gold;
    }

    /**
     * If enough magic points, and player's food or gold is not already 0, no counter spell cast,
     * food and/or gold is reduced
     */
    @Override
    public ActionResult castSpell(final Model model, final ConnectionWrapper connection,
                                  final Player player, final CombatState state) {
        final int originalCounterSpells = player.getDungeon().getCounterSpells();
        if (player.getDungeon().getTotalMagicPoints() >= 1) {
            connection.sendSpellCast(this.getId(), player.getId());
            player.setWithstoodSpells(player.getWithstoodSpells() + 1);
            //check for countered spell
            final ActionResult action = castCounterSpell(model, player,
                    connection, State.Phase.COUNTERING_SPELLS);
            if (!player.isAlive()) {
                return action;
            }
            if (originalCounterSpells == player.getDungeon().getCounterSpells()) {
                if (this.food > 0 && player.getFood() > 0) {
                    connection.sendFoodChanged(player.getId(),
                            -Math.min(this.food, player.getFood()));
                    assignFood(player);
                }
                if (this.gold > 0 && player.getGold() > 0) {
                    connection.sendGoldChanged(player.getId(),
                            -Math.min(this.gold, player.getGold()));
                    assignGold(player);
                }
            }
        }
        return ActionResult.PROCEED;
    }

    private void assignFood(final Player player) {
        if (player.getFood() - this.food < 0) {
            player.setFood(0);
        } else {
            player.setFood(player.getFood() - this.food);
        }
    }

    private void assignGold(final Player player) {
        if (player.getGold() - this.gold < 0) {
            player.setGold(0);
        } else {
            player.setGold(player.getGold() - this.gold);
        }
    }

    public int getFood() {
        return food;
    }

    public void setFood(final int food) {
        this.food = food;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(final int gold) {
        this.gold = gold;
    }
}
