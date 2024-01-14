package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class SystemTest7 extends SystemTest6 {

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
    }

    public void combat2() throws TimeoutException, AssertionError {
        this.assertNextRound(1, 2);
        this.assertSetBattleGround(1);
        this.assertActNow(1);
        this.sendBattleGround(1, 1, 0);
        this.assertBattleGroundSet(1, 0, 1, 0);
        this.assertDefendYourself(1);
        this.assertActNow(1);
        this.sendEndTurn(1);

        this.assertSpellCast(1, 18, 0);

        this.assertSpellCast(1, 10, 0);
        this.assertRoomsBlocked(1, 0, 2);

        this.assertSpellCast(1, 17, 0);
        this.assertGoldChanged(1, -1, 0);

        this.assertAdventurerDamaged(1, 23, 1);
        this.assertAdventurerDamaged(1, 2, 1);

        this.assertTunnelConquered(1, 23, 1, 0);
        this.assertEvilnessChanged(1, -1, 0);
    }
}
