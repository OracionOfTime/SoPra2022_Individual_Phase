package junitests;

import de.unisaarland.cs.se.selab.ConnectionWrapper;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.model.Adventurer;
import de.unisaarland.cs.se.selab.model.Player;
import de.unisaarland.cs.se.selab.model.dungeon.Dungeon;
import de.unisaarland.cs.se.selab.spells.Resource;
import de.unisaarland.cs.se.selab.spells.Spell;
import de.unisaarland.cs.se.selab.spells.SpellType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TestResource {

    ConnectionWrapper connection = Mockito.mock(ConnectionWrapper.class);

    Adventurer adv1 = new Adventurer(1, 3, 5,
            1, 1, false, 2);
    Adventurer adv2 = new Adventurer(2, 1, 2,
            1, 0, true, 2);
    Adventurer adv3 = new Adventurer(3, 7,
            3, 0, 1, false, 0);

    Spell resource = new Resource(1, SpellType.RESOURCE, BidType.GOLD,
            1, 3, 0);

    Dungeon dungeon = new Dungeon();

    Player player1 = new Player(1, "Lizzy", 5,
            4, 3, 5, dungeon);

    @Test
    void testRes() {
        dungeon.setCounterSpells(0);
        dungeon.addAdventurer(adv1);
        dungeon.addAdventurer(adv2);
        dungeon.addAdventurer(adv3);
        dungeon.calculateTotalMagicPoints();
        resource.castSpell(null, connection, player1, null);

        assert (player1.getFood() == 2);
    }
}
