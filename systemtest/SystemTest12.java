package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class SystemTest12 extends SystemTest11 {

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
    }

    public void bidding22() throws TimeoutException, AssertionError {
        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.ROOM, 1);
        this.assertBidPlaced(1, BidType.ROOM, 0, 1);

        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.TUNNEL, 2);
        this.assertBidPlaced(1, BidType.TUNNEL, 0, 2);

        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.MONSTER, 3);
        this.assertBidPlaced(1, BidType.MONSTER, 0, 3);
    }

    public void eval22() throws TimeoutException, AssertionError {
        this.assertSpellUnlocked(1, 4, 0);
        this.assertSpellUnlocked(1, 22, 0);
        this.assertDigTunnel(1);
        this.assertActNow(1);
        this.sendDigTunnel(1, 2, 3);
        this.assertImpsChanged(1, -1, 0);
        this.assertTunnelDug(1, 0, 2, 3);
        this.assertActNow(1);
        this.sendDigTunnel(1, 3, 3);
        this.assertImpsChanged(1, -1, 0);
        this.assertTunnelDug(1, 0, 3, 3);

        this.assertSelectMonster(1);
        this.assertActNow(1);
        this.sendHireMonster(1, 5);
        this.assertFoodChanged(1, -1, 0);
        this.assertEvilnessChanged(1, 3, 0);
        this.assertMonsterHired(1, 5, 0);

        this.assertSpellUnlocked(1, 12, 0);
        this.assertGoldChanged(1, -1, 0);
        this.assertPlaceRoom(1);
        this.assertActNow(1);
        this.sendActivateRoom(1, 5);
        this.assertActionFailed(1);
        this.assertActNow(1);
        this.sendBuildRoom(1, 2, 2, 3);
        this.assertRoomBuilt(1, 0, 3, 2, 2);

        this.assertBidRetrieved(1, BidType.NICENESS, 0);
        this.assertBidRetrieved(1, BidType.GOLD, 0);
        this.assertBidRetrieved(1, BidType.ROOM, 0);

        this.assertImpsChanged(1, 2, 0);
        this.assertAdventurerArrived(1, 18, 0);
        this.assertNextRound(1, 3);

        this.assertAdventurerDrawn(1, 11);

        this.assertMonsterDrawn(1, 15);
        this.assertMonsterDrawn(1, 10);
        this.assertMonsterDrawn(1, 17);

        this.assertRoomDrawn(1, 14);
        this.assertRoomDrawn(1, 13);

        this.assertSpellDrawn(1, 5);
        this.assertSpellDrawn(1, 3);
        this.assertSpellDrawn(1, 8);

        this.assertBiddingStarted(1);
    }
}
