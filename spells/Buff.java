package de.unisaarland.cs.se.selab.spells;

import de.unisaarland.cs.se.selab.ConnectionWrapper;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.commands.ActionResult;
import de.unisaarland.cs.se.selab.model.Adventurer;
import de.unisaarland.cs.se.selab.model.Model;
import de.unisaarland.cs.se.selab.model.Player;
import de.unisaarland.cs.se.selab.state.CombatState;
import de.unisaarland.cs.se.selab.state.State;

public class Buff extends Spell {

    private int healthPoints;
    private int healValue;
    private int defuseValue;

    public Buff(final int id, final SpellType valueOf, final BidType valueOf1,
                final int slot, final int healthPoints,
                final int healValue, final int defuseValue) {
        super(id, valueOf, valueOf1, slot);
        this.healthPoints = healthPoints;
        this.healValue = healValue;
        this.defuseValue = defuseValue;
    }

    /**
     * If enough magic points, no counter spell cast, adventurers' attributes are buffed :
     * -healValue is only applied to priests
     * -defuseValue is only applied to thieves
     */
    @Override
    public ActionResult castSpell(final Model model, final ConnectionWrapper connection,
                                  final Player player, final CombatState state) {
        final int originalCounterSpells = player.getNumberOfCounterSpells();
        if (player.getDungeon().getTotalMagicPoints() >= 2) {
            connection.sendSpellCast(this.getId(), player.getId());
            player.setWithstoodSpells(player.getWithstoodSpells() + 1);
            //check for countered spell
            final ActionResult action = castCounterSpell(model, player,
                    connection, State.Phase.COUNTERING_SPELLS);
            if (!player.isAlive()) {
                return action;
            }
            if (originalCounterSpells == player.getNumberOfCounterSpells()) {
                state.setBufferActive(true);
                for (final Adventurer adv : player.getDungeon().getAllAdventurers()) {
                    buffAdventurers(adv);
                }
            }
        }
        return ActionResult.PROCEED;
    }

    /**
     * checks if given adventurer is a priest or thief
     */
    private void buffAdventurers(final Adventurer adv) {
        adv.buffHealthPoints(this.healthPoints);
        if (adv.getHealValue() > 0) {
            adv.buffHealValue(this.healValue);
        }
        if (adv.getDefuseValue() > 0) {
            adv.buffDefuseValue(this.defuseValue);
        }
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(final int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getHealValue() {
        return healValue;
    }

    public void setHealValue(final int healValue) {
        this.healValue = healValue;
    }

    public int getDefuseValue() {
        return defuseValue;
    }

    public void setDefuseValue(final int defuseValue) {
        this.defuseValue = defuseValue;
    }
}
