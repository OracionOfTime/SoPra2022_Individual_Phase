package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class SystemTest14 extends SystemTest13 {

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
    }

    public void bid24() throws TimeoutException, AssertionError {
        this.sendActivateRoom(1, 3);
        this.assertImpsChanged(1, -3, 0);
        this.assertRoomActivated(1, 0, 3);
        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.ROOM, 1);
        this.assertBidPlaced(1, BidType.ROOM, 0, 1);
        this.assertActNow(1);

        this.sendPlaceBid(1, BidType.FOOD, 2);
        this.assertBidPlaced(1, BidType.FOOD, 0, 2);
        this.assertActNow(1);

        this.sendPlaceBid(1, BidType.IMPS, 3);
        this.assertBidPlaced(1, BidType.IMPS, 0, 3);
    }

    public void eval24() throws TimeoutException, AssertionError  {

        this.assertSpellUnlocked(1, 14, 0);
        this.assertSpellUnlocked(1, 1, 0);
        this.assertGoldChanged(1, -1, 0);
        this.assertFoodChanged(1, 2, 0);

        this.assertFoodChanged(1, -1, 0);
        this.assertImpsChanged(1, 1, 0);

        this.assertSpellUnlocked(1, 15, 0);
        this.assertGoldChanged(1, -1, 0);
        this.assertPlaceRoom(1);
        this.assertActNow(1);
        this.sendBuildRoom(1, 3, 3, 1);
        this.assertRoomBuilt(1, 0, 1, 3, 3);

        this.assertBidRetrieved(1, BidType.GOLD, 0);
        this.assertBidRetrieved(1, BidType.TRAP, 0);
        this.assertBidRetrieved(1, BidType.ROOM, 0);

        this.assertImpsChanged(1, 3, 0);
        this.assertGoldChanged(1, 1, 0);

        this.assertNextRound(1, 1);
    }
}
