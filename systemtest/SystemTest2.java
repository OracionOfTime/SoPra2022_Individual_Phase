package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class SystemTest2 extends SystemTest1 {

    @Override
    protected void run() throws TimeoutException, AssertionError {
        registrationPhase();
        biddingPhase1();
        evalPhase1();
        newRound2();
    }

    public void biddingPhase1() throws TimeoutException, AssertionError {
        this.sendPlaceBid(1, BidType.ROOM, 1);
        this.assertBidPlaced(1, BidType.ROOM, 0, 1);
        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.TUNNEL, 2);
        this.assertBidPlaced(1, BidType.TUNNEL, 0, 2);
        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.FOOD, 3);
        this.assertBidPlaced(1, BidType.FOOD, 0, 3);
    }

    public void evalPhase1() throws TimeoutException, AssertionError {

        this.assertSpellUnlocked(1, 7, 0);
        this.assertGoldChanged(1, -1, 0);
        this.assertFoodChanged(1, 2, 0);

        this.assertSpellUnlocked(1, 23, 0);
        this.assertDigTunnel(1);
        this.assertActNow(1);
        this.sendDigTunnel(1, 1, 0);
        this.assertImpsChanged(1, -1, 0);
        this.assertTunnelDug(1, 0, 1, 0);
        this.assertActNow(1);
        this.sendDigTunnel(1, 1, 1);
        this.assertImpsChanged(1, -1, 0);
        this.assertTunnelDug(1, 0, 1, 1);

        this.assertSpellUnlocked(1, 19, 0);
        this.assertGoldChanged(1, -1, 0);
        this.assertPlaceRoom(1);
        this.assertActNow(1);
        this.sendBuildRoom(1, 1, 1, 5);
        this.assertRoomBuilt(1, 0, 5, 1, 1);

        this.assertBidRetrieved(1, BidType.ROOM, 0);
        this.assertImpsChanged(1, 2, 0);

        this.assertAdventurerArrived(1, 29, 0);

        this.assertNextRound(1, 2);
    }

    public void newRound2() throws TimeoutException, AssertionError {
        this.assertAdventurerDrawn(1, 23);

        this.assertMonsterDrawn(1, 7);
        this.assertMonsterDrawn(1, 22);
        this.assertMonsterDrawn(1, 1);

        this.assertRoomDrawn(1, 8);
        this.assertRoomDrawn(1, 15);

        this.assertSpellDrawn(1, 10);
        this.assertSpellDrawn(1, 18);
        this.assertSpellDrawn(1, 17);

        this.assertBiddingStarted(1);
        this.assertActNow(1);
    }
}
