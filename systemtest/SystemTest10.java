package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class SystemTest10 extends SystemTest9 {

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
    }

    public void newRound21() throws TimeoutException, AssertionError {
        this.assertAdventurerDrawn(1, 0);

        this.assertMonsterDrawn(1, 0);
        this.assertMonsterDrawn(1, 18);
        this.assertMonsterDrawn(1, 21);

        this.assertRoomDrawn(1, 12);
        this.assertRoomDrawn(1, 6);

        this.assertSpellDrawn(1, 9);
        this.assertSpellDrawn(1, 6);
        this.assertSpellDrawn(1, 13);

        this.assertBiddingStarted(1);
        this.assertActNow(1);
    }
}
