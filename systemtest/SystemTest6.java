package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class SystemTest6 extends SystemTest5 {

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
    }

    public void combat1() throws TimeoutException, AssertionError {
        this.assertSetBattleGround(1);
        this.assertActNow(1);
        this.sendBattleGround(1, 0, 0);
        this.assertBattleGroundSet(1, 0, 0, 0);
        this.assertDefendYourself(1);
        this.assertActNow(1);
        this.sendMonster(1, 1);
        this.assertMonsterPlaced(1, 1, 0);
        this.assertActNow(1);
        this.sendEndTurn(1);

        this.assertSpellCast(1, 7, 0);
        this.assertCounterSpell(1);
        this.assertActNow(1);
        this.sendCastCounterSpell(1);
        this.assertCounterSpellCast(1, 0);

        this.assertSpellCast(1, 23, 0);
        this.assertBidTypeBlocked(1, 0, BidType.TUNNEL, 1);

        this.assertSpellCast(1, 19, 0);

        this.assertAdventurerImprisoned(1, 29);
        this.assertArchmageArrived(1, 0);
        this.assertMonsterRemoved(1, 1, 0);

        this.assertAdventurerDamaged(1, 23, 1);
        this.assertAdventurerDamaged(1, 2, 1);

        this.assertTunnelConquered(1, 23, 0, 0);
        this.assertEvilnessChanged(1, -1, 0);
    }
}
