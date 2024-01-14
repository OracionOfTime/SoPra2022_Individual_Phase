package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class SystemTest3 extends SystemTest2 {

    @Override
    protected void run() throws TimeoutException, AssertionError {
        registrationPhase();
        biddingPhase1();
        evalPhase1();
        newRound2();
        biddingPhase2();
        evalPhase2();
        newRound3();
    }

    public void biddingPhase2() throws TimeoutException, AssertionError {
        this.sendPlaceBid(1, BidType.IMPS, 1);
        this.assertBidPlaced(1, BidType.IMPS, 0, 1);
        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.MONSTER, 2);
        this.assertBidPlaced(1, BidType.MONSTER, 0, 2);
        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.GOLD, 3);
        this.assertBidPlaced(1, BidType.GOLD, 0, 3);
    }

    public void evalPhase2() throws TimeoutException, AssertionError {
        this.assertSpellUnlocked(1, 18, 0);
        this.assertImpsChanged(1, -2, 0);

        this.assertSpellUnlocked(1, 10, 0);
        this.assertFoodChanged(1, -1, 0);
        this.assertImpsChanged(1, 1, 0);

        this.assertSpellUnlocked(1, 17, 0);
        this.assertSelectMonster(1);
        this.assertActNow(1);
        this.sendHireMonster(1, 1);
        this.assertEvilnessChanged(1, 10, 0);
        this.assertMonsterHired(1, 1, 0);

        this.assertBidRetrieved(1, BidType.TUNNEL, 0);
        this.assertBidRetrieved(1, BidType.FOOD, 0);
        this.assertBidRetrieved(1, BidType.IMPS, 0);

        this.assertImpsChanged(1, 2, 0);
        this.assertGoldChanged(1, 2, 0);

        this.assertAdventurerArrived(1, 23, 0);

        this.assertNextRound(1, 3);
    }

    public void newRound3() throws TimeoutException, AssertionError {
        this.assertAdventurerDrawn(1, 2);

        this.assertMonsterDrawn(1, 14);
        this.assertMonsterDrawn(1, 3);
        this.assertMonsterDrawn(1, 20);

        this.assertRoomDrawn(1, 0);
        this.assertRoomDrawn(1, 10);

        this.assertSpellDrawn(1, 24);
        this.assertSpellDrawn(1, 16);
        this.assertSpellDrawn(1, 0);

        this.assertBiddingStarted(1);
        this.assertActNow(1);
    }
}
