package junitests;

import de.unisaarland.cs.se.selab.model.Adventurer;
import org.junit.jupiter.api.Test;

class TestUnBuff {

    Adventurer adv1 = new Adventurer(1, 3, 5,
            1, 1, false, 2);
    Adventurer adv2 = new Adventurer(2, 1, 2,
            1, 0, true, 2);
    Adventurer adv3 = new Adventurer(3, 7,
            3, 0, 1, false, 0);

    @Test
    void buffHealthPointsBeyondMax() {
        adv1.setCurrentHealthPoints(adv1.getMaxHealthPoints() - 3);
        adv1.buffHealthPoints(7);

        assert (adv1.getCurrentHealthPoints() == 9);
    }

    @Test
    void buffHPBeyondMaxAndUnBuff() {
        adv2.setCurrentHealthPoints(0);
        adv2.buffHealthPoints(9);
        adv2.unBuffHealthPoints();

        assert (adv2.getCurrentHealthPoints() == 2);
    }

    @Test
    void buffAndUnBuffHP() {
        adv3.setCurrentHealthPoints(1);
        adv3.buffHealthPoints(1);
        adv3.unBuffHealthPoints();

        assert (adv3.getCurrentHealthPoints() == 2);
    }

    @Test
    void buffHV() {
        adv1.buffHealValue(2);
        adv1.buffHealValue(2);

        assert (adv1.getHealValue() == 5);
    }

    @Test
    void unBuffHV() {
        adv2.buffHealValue(2);
        adv2.buffHealValue(5);

        adv2.unBuffHealValue();

        assert (adv2.getHealValue() == 1);
    }

    @Test
    void buffDV() {
        adv1.buffDefuseValue(2);
        adv1.buffDefuseValue(2);

        assert (adv1.getDefuseValue() == 5);
    }

    @Test
    void unBuffDV() {
        adv3.buffDefuseValue(5);
        adv3.buffDefuseValue(5);

        adv3.unBuffDefuseValue();

        assert (adv3.getDefuseValue() == 1);
    }
}
