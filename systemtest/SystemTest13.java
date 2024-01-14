package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class SystemTest13 extends SystemTest12 {

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
    }

    public void bid23() throws TimeoutException, AssertionError {
        this.assertActNow(1);
        this.sendActivateRoom(1, 12);
        this.assertActionFailed(1);
        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.FOOD, 1);
        this.assertBidPlaced(1, BidType.FOOD, 0, 1);
        this.assertActNow(1);

        this.sendActivateRoom(1, 12);
        this.assertActionFailed(1);
        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.GOLD, 2);
        this.assertBidPlaced(1, BidType.GOLD, 0, 2);
        this.assertActNow(1);

        this.sendActivateRoom(1, 12);
        this.assertActionFailed(1);
        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.TRAP, 3);
        this.assertBidPlaced(1, BidType.TRAP, 0, 3);
    }

    public void eval23() throws TimeoutException, AssertionError {
        this.assertSpellUnlocked(1, 3, 0);

        this.assertSpellUnlocked(1, 5, 0);
        this.assertImpsChanged(1, -2, 0);

        this.assertSpellUnlocked(1, 8, 0);

        this.assertBidRetrieved(1, BidType.TUNNEL, 0);
        this.assertBidRetrieved(1, BidType.MONSTER, 0);
        this.assertBidRetrieved(1, BidType.FOOD, 0);

        this.assertImpsChanged(1, 2, 0);
        this.assertGoldChanged(1, 2, 0);

        this.assertAdventurerArrived(1, 11, 0);

        this.assertNextRound(1, 4);

        this.assertMonsterDrawn(1, 8);
        this.assertMonsterDrawn(1, 4);
        this.assertMonsterDrawn(1, 2);

        this.assertRoomDrawn(1, 1);
        this.assertRoomDrawn(1, 11);

        this.assertSpellDrawn(1, 14);
        this.assertSpellDrawn(1, 1);
        this.assertSpellDrawn(1, 15);

        this.assertBiddingStarted(1);

        this.assertActNow(1);
    }
}
