package junitests;

import de.unisaarland.cs.se.selab.ConnectionWrapper;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.model.Adventurer;
import de.unisaarland.cs.se.selab.model.Player;
import de.unisaarland.cs.se.selab.model.dungeon.Dungeon;
import de.unisaarland.cs.se.selab.spells.Buff;
import de.unisaarland.cs.se.selab.spells.Spell;
import de.unisaarland.cs.se.selab.spells.SpellType;
import de.unisaarland.cs.se.selab.state.CombatState;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TestBuff {

    ConnectionWrapper connection = Mockito.mock(ConnectionWrapper.class);

    Adventurer adv1 = new Adventurer(1, 3, 5,
            1, 1, false, 2);
    Adventurer adv2 = new Adventurer(2, 1, 2,
            1, 0, true, 2);
    Adventurer adv3 = new Adventurer(3, 7,
            3, 0, 1, false, 0);

    Spell buff = new Buff(1, SpellType.BUFF, BidType.GOLD,
            1, 2, 0, 0);

    Dungeon dungeon = new Dungeon();

    Player player1 = new Player(1, "Lizzy", 5,
            4, 3, 5, dungeon);

    CombatState state = new CombatState(null, connection);

    @Test
    void testBuff() {
        dungeon.setCounterSpells(0);
        dungeon.addAdventurer(adv1);
        dungeon.addAdventurer(adv2);
        dungeon.addAdventurer(adv3);
        dungeon.calculateTotalMagicPoints();
        buff.castSpell(null, connection, player1, state);

        final List<Adventurer> adventurers = dungeon.getAllAdventurers();
        final Adventurer a1 = adventurers.get(0);
        final Adventurer a2 = adventurers.get(1);
        final Adventurer a3 = adventurers.get(2);

        assert (a1.getCurrentHealthPoints() == 4);
        assert (a2.getCurrentHealthPoints() == 7);
        assert (a3.getCurrentHealthPoints() == 5);
    }
}
