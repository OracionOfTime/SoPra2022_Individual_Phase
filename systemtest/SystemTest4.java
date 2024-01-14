package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class SystemTest4 extends SystemTest3 {

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
    }

    public void biddingPhase3() throws TimeoutException, AssertionError {
        this.sendPlaceBid(1, BidType.TRAP, 1);
        this.assertBidPlaced(1, BidType.TRAP, 0, 1);
        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.TUNNEL, 2);
        this.assertBidPlaced(1, BidType.TUNNEL, 0, 2);
        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.FOOD, 3);
        this.assertBidPlaced(1, BidType.FOOD, 0, 3);
    }

    public void evalPhase3() throws TimeoutException, AssertionError {
        this.assertSpellUnlocked(1, 0, 0);
        this.assertGoldChanged(1, -1, 0);
        this.assertFoodChanged(1, 2, 0);

        this.assertSpellUnlocked(1, 16, 0);
        this.assertDigTunnel(1);
        this.assertActNow(1);
        this.sendDigTunnel(1, 2, 1);
        this.assertImpsChanged(1, -1, 0);
        this.assertTunnelDug(1, 0, 2, 1);
        this.assertActNow(1);
        this.sendDigTunnel(1, 2, 2);
        this.assertImpsChanged(1, -1, 0);
        this.assertTunnelDug(1, 0, 2, 2);
        this.assertCounterSpellFound(1, 0);

        this.assertSpellUnlocked(1, 24, 0);
        this.assertGoldChanged(1, -1, 0);
        this.assertTrapAcquired(1, 0, 26);

        this.assertBidRetrieved(1, BidType.MONSTER, 0);
        this.assertBidRetrieved(1, BidType.GOLD, 0);
        this.assertBidRetrieved(1, BidType.TRAP, 0);

        this.assertImpsChanged(1, 2, 0);

        this.assertAdventurerArrived(1, 2, 0);
    }

    public void nextRound4() throws TimeoutException, AssertionError {
        this.assertNextRound(1, 4);
        this.assertMonsterDrawn(1, 6);
        this.assertMonsterDrawn(1, 11);
        this.assertMonsterDrawn(1, 16);

        this.assertRoomDrawn(1, 2);
        this.assertRoomDrawn(1, 9);

        this.assertSpellDrawn(1, 2);
        this.assertSpellDrawn(1, 11);
        this.assertSpellDrawn(1, 20);

        this.assertBiddingStarted(1);
        this.assertActNow(1);
    }
}
