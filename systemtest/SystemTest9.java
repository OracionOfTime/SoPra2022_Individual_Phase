package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class SystemTest9 extends SystemTest8 {

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
    }

    public void combat4() throws TimeoutException, AssertionError {
        this.assertNextRound(1, 4);
        this.assertSetBattleGround(1);
        this.assertActNow(1);
        this.sendBattleGround(1, 2, 1);
        this.assertBattleGroundSet(1, 0, 2, 1);
        this.assertDefendYourself(1);
        this.assertActNow(1);
        this.sendEndTurn(1);

        this.assertSpellCast(1, 11, 0);

        this.assertSpellCast(1, 20, 0);

        this.assertSpellCast(1, 2, 0);
        this.assertRoomRemoved(1, 0, 9);

        this.assertTunnelConquered(1, 23, 2, 1);
        this.assertEvilnessChanged(1, -1, 0);

        this.assertNextYear(1, 2);
        this.assertBidRetrieved(1, BidType.GOLD, 0);
        this.assertBidRetrieved(1, BidType.MONSTER, 0);
        this.assertNextRound(1, 1);
    }
}
