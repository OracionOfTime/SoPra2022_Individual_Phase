package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class SystemTest16 extends SystemTest15 {

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
    }

    public void combat22() throws TimeoutException, AssertionError {

        this.assertSetBattleGround(1);
        this.assertActNow(1);
        this.sendBattleGround(1, 2, 3);
        this.assertBattleGroundSet(1, 0, 2, 3);
        this.assertDefendYourself(1);
        this.assertActNow(1);

        this.sendMonsterTargeted(1, 5, 1);
        this.assertMonsterPlaced(1, 5, 0);
        this.assertActNow(1);
        this.sendEndTurn(1);

        this.assertSpellCast(1, 4, 0);

        this.assertSpellCast(1, 12, 0);

        this.assertAdventurerImprisoned(1, 0);
        this.assertArchmageArrived(1, 0);
        this.assertMonsterRemoved(1, 16, 0);

        this.assertAdventurerDamaged(1, 18, 1);
        this.assertAdventurerDamaged(1, 11, 1);

        this.assertTunnelConquered(1, 18, 2, 3);
        this.assertEvilnessChanged(1, -1, 0);
        this.assertAdventurerHealed(1, 2, 11, 18);

        this.assertNextRound(1, 3);
    }
}
