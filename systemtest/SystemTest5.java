package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class SystemTest5 extends SystemTest4 {

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
    }

    public void biddingPhase4() throws TimeoutException, AssertionError {
        this.sendPlaceBid(1, BidType.ROOM, 1);
        this.assertBidPlaced(1, BidType.ROOM, 0, 1);
        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.GOLD, 2);
        this.assertBidPlaced(1, BidType.GOLD, 0, 2);
        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.MONSTER, 3);
        this.assertBidPlaced(1, BidType.MONSTER, 0, 3);
    }

    public void evalPhase4() throws TimeoutException, AssertionError {
        this.assertSpellUnlocked(1, 11, 0);
        this.assertImpsChanged(1, -2, 0);

        this.assertSpellUnlocked(1, 20, 0);
        this.assertSelectMonster(1);
        this.assertActNow(1);
        this.sendHireMonster(1, 16);
        this.assertFoodChanged(1, -1, 0);
        this.assertMonsterHired(1, 16, 0);

        this.assertSpellUnlocked(1, 2, 0);
        this.assertGoldChanged(1, -1, 0);
        this.assertPlaceRoom(1);
        this.assertActNow(1);
        this.sendBuildRoom(1, 2, 2, 9);
        this.assertRoomBuilt(1, 0, 9, 2, 2);

        this.assertBidRetrieved(1, BidType.TUNNEL, 0);
        this.assertBidRetrieved(1, BidType.FOOD, 0);
        this.assertBidRetrieved(1, BidType.ROOM, 0);

        this.assertImpsChanged(1, 2, 0);
        this.assertGoldChanged(1, 2, 0);

        this.assertNextRound(1, 1);
    }
}
