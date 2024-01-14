package de.unisaarland.cs.se.selab.spells;

import de.unisaarland.cs.se.selab.ConnectionWrapper;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.commands.ActionResult;
import de.unisaarland.cs.se.selab.model.Model;
import de.unisaarland.cs.se.selab.model.Player;
import de.unisaarland.cs.se.selab.model.Trap;
import de.unisaarland.cs.se.selab.model.dungeon.Room;
import de.unisaarland.cs.se.selab.model.dungeon.Tunnel;
import de.unisaarland.cs.se.selab.state.CombatState;
import de.unisaarland.cs.se.selab.state.State;
import java.util.Optional;

public class Structural extends Spell {

    private final StructureEffect structureEffect;

    public Structural(final int id, final SpellType valueOf, final BidType valueOf1,
                      final int slot, final StructureEffect structureEffect) {
        super(id, valueOf, valueOf1, slot);
        this.structureEffect = structureEffect;
    }

    /**
     *If enough magic points, no counter spell cast, checkEffect method is called
     */
    @Override
    public ActionResult castSpell(final Model model, final ConnectionWrapper connection,
                                  final Player player, final CombatState state) {
        final int originalCounterSpells = player.getNumberOfCounterSpells();
        if (player.getDungeon().getTotalMagicPoints() >= 5) {
            connection.sendSpellCast(this.getId(), player.getId());
            player.setWithstoodSpells(player.getWithstoodSpells() + 1);
            //check for countered spell
            final ActionResult action = castCounterSpell(model, player,
                    connection, State.Phase.COUNTERING_SPELLS);
            if (!player.isAlive()) {
                return action;
            }
            if (originalCounterSpells == player.getNumberOfCounterSpells()) {
                checkEffect(structureEffect, player, connection);
            }
        }
        return ActionResult.PROCEED;
    }

    /**
     * Checks the Structure Effect and in case of:
     * - Conquer : if current battleground is not already conquered,
     *             laid out Traps should be returned to the current player
     * - Destroy: if there are rooms in the dungeon, gets the closest room,
     *            removes the first monster from the room, should there be 2
     *            and removes it
     */
    private void checkEffect(final StructureEffect structureEffect, final Player player,
                             final ConnectionWrapper connection) {
        if (structureEffect == StructureEffect.CONQUER
                && !(player.getDungeon().getBattleGround().get().isConquered())) {
            player.getDungeon().getBattleGround().get().conquer();
            if (player.getDungeon().getBattleGround().get().getTrap().isPresent()) {
                final Trap trap = player.getDungeon().getBattleGround().get().getTrap().get();
                player.getTraps().add(trap);
            }
        } else if (structureEffect == StructureEffect.DESTROY
                && !(player.getDungeon().getGraph().getRooms().isEmpty())) {
            final Optional<Tunnel> closestTunnel =
                    player.getDungeon().getGraph()
                            .getClosestRoom(player.getDungeon().getBattleGround().get());
            if (closestTunnel.isEmpty()) {
                return;
            }
            if (closestTunnel.get().getMonsters().size() == Room.MONSTER_LIMIT) {
                closestTunnel.get().getMonsters().remove(0);
            }
            connection.sendRoomRemoved(player.getId(), closestTunnel.get().getRoom().get().getId());
            closestTunnel.get().removeRoom();
        }
    }

    public StructureEffect getStructureEffect() {
        return structureEffect;
    }
}

