package junitests;

import de.unisaarland.cs.se.selab.model.Adventurer;
import de.unisaarland.cs.se.selab.model.dungeon.Dungeon;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class TestTotalMP {

    Adventurer adv1 = new Adventurer(1, 3, 5,
            1, 1, false, 2);
    Adventurer adv2 = new Adventurer(2, 1, 2,
            1, 0, true, 2);
    Adventurer adv3 = new Adventurer(3, 7,
            3, 0, 1, false, 0);

    List<Adventurer> remainingAdv = new ArrayList<>();

    Dungeon dungeon = new Dungeon();

    @Test
    void totalMpAllHealthy() {
        dungeon.addAdventurer(adv1);
        dungeon.addAdventurer(adv2);
        dungeon.addAdventurer(adv3);
        dungeon.calculateTotalMagicPoints();

        assert (dungeon.getTotalMagicPoints() == 4);
    }

    @Test
    void totalMPAllHealthyLinus() {
        dungeon.addAdventurer(adv1);
        dungeon.addAdventurer(adv2);
        dungeon.addAdventurer(adv3);
        dungeon.setLinusArrived(true);
        dungeon.calculateTotalMagicPoints();

        assert (dungeon.getTotalMagicPoints() == 7);
    }

    @Test
    void totalMPDamage() {
        dungeon.addAdventurer(adv1);
        dungeon.addAdventurer(adv2);
        dungeon.addAdventurer(adv3);

        adv1.damage(5);

        dungeon.calculateTotalMagicPoints();

        assert (dungeon.getTotalMagicPoints() == 2);
    }

    @Test
    void totalMPDamageLinus() {
        dungeon.addAdventurer(adv1);
        dungeon.addAdventurer(adv2);
        dungeon.addAdventurer(adv3);
        dungeon.setLinusArrived(true);

        adv1.damage(5);

        dungeon.calculateTotalMagicPoints();

        assert (dungeon.getTotalMagicPoints() == 5);
    }
}
