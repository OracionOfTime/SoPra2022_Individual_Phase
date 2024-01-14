package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class SystemTest11 extends SystemTest10 {

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
    }

    public void bidding21() throws TimeoutException, AssertionError {

        this.sendPlaceBid(1, BidType.TUNNEL, 1);
        this.assertActionFailed(1);
        this.assertActNow(1);
        this.sendActivateRoom(1, 5);
        this.assertActionFailed(1);
        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.ROOM, 1);
        this.assertBidPlaced(1, BidType.ROOM, 0, 1);
        this.assertActNow(1);

        this.sendPlaceBid(1, BidType.TUNNEL, 2);
        this.assertActionFailed(1);
        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.NICENESS, 2);
        this.assertBidPlaced(1, BidType.NICENESS, 0, 2);
        this.assertActNow(1);

        this.sendPlaceBid(1, BidType.TUNNEL, 3);
        this.assertActionFailed(1);
        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.GOLD, 3);
        this.assertBidPlaced(1, BidType.GOLD, 0, 3);
    }

    public void eval21() throws TimeoutException, AssertionError {

        this.assertSpellUnlocked(1, 9, 0);
        this.assertEvilnessChanged(1, -1, 0);


        this.assertImpsChanged(1, -1, 0);

        this.assertSpellUnlocked(1, 6, 0);
        this.assertSpellUnlocked(1, 13, 0);


        this.assertBidRetrieved(1, BidType.ROOM, 0);
        this.assertImpsChanged(1, 1, 0);
        this.assertGoldChanged(1, 1, 0);
        this.assertAdventurerArrived(1, 0, 0);

        this.assertNextRound(1, 2);
        this.assertAdventurerDrawn(1, 18);

        this.assertMonsterDrawn(1, 19);
        this.assertMonsterDrawn(1, 12);
        this.assertMonsterDrawn(1, 5);

        this.assertRoomDrawn(1, 3);
        this.assertRoomDrawn(1, 7);

        this.assertSpellDrawn(1, 4);
        this.assertSpellDrawn(1, 22);
        this.assertSpellDrawn(1, 12);

        this.assertBiddingStarted(1);
    }
}
