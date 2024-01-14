package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class SystemTest15 extends SystemTest14 {

    @Override
    protected void run() throws TimeoutException, AssertionError {
        registrationPhase();
        biddingPhase1();
        evalPhase1();
        newRound2();
        biddingPhase2();
        evalPhase2();
        newRound3();
        biddingPhase3();
        evalPhase3();
        nextRound4();
        biddingPhase4();
        evalPhase4();
        combat1();
        combat2();
        combat3();
        combat4();
        newRound21();
        bidding21();
        eval21();
        bidding22();
        eval22();
        bid23();
        eval23();
        bid24();
        eval24();
        combat21();
    }

    public void combat21() throws TimeoutException, AssertionError {
        this.assertSetBattleGround(1);
        this.assertActNow(1);
        this.sendBattleGround(1, 2, 2);
        this.assertBattleGroundSet(1, 0, 2, 2);
        this.assertDefendYourself(1);
        this.assertActNow(1);
        this.sendEndTurn(1);

        this.assertSpellCast(1, 6, 0);
        this.assertFoodChanged(1, -5, 0);

        this.assertSpellCast(1, 13, 0);

        this.assertAdventurerDamaged(1, 0, 2);
        this.assertAdventurerDamaged(1, 18, 2);
        this.assertAdventurerDamaged(1, 11, 2);

        this.assertTunnelConquered(1, 0, 2, 2);
        this.assertEvilnessChanged(1, -1, 0);
        this.assertAdventurerHealed(1, 2, 11, 0);

        this.assertNextRound(1, 2);
    }
}
