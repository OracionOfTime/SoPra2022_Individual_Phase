package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class SystemTest17 extends SystemTest16 {

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
        combat22();
        combat23();
    }

    public void combat23() throws TimeoutException, AssertionError {

        this.assertSetBattleGround(1);
        this.assertActNow(1);
        this.sendBattleGround(1, 3, 3);
        this.assertBattleGroundSet(1, 0, 3, 3);
        this.assertDefendYourself(1);

        this.assertActNow(1);
        this.sendEndTurn(1);

        this.assertSpellCast(1, 3, 0);

        this.assertSpellCast(1, 5, 0);
        this.assertGoldChanged(1, -1, 0);

        this.assertSpellCast(1, 8, 0);

        this.assertTunnelConquered(1, 18, 3, 3);
        this.assertEvilnessChanged(1, -1, 0);
        this.assertNextRound(1, 4);
        this.assertAdventurerFled(1, 29);
        this.assertEvilnessChanged(1, -1, 0);

        this.assertGameEnd(1, 0, 27);
    }
}
