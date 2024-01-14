package de.unisaarland.cs.se.selab.systemtest.spellall;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.RegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import de.unisaarland.cs.se.selab.systemtest.oneplayer.OnePlayer;

public class SpellAll extends OnePlayer {
    @Override
    public String createConfig() {
        return Utils.loadResource(RegistrationTest.class, "config_spelltest.json");
    }

    @Override
    public void run() throws TimeoutException {
        registerAndBid();
        bidAndEval();
        bidSecondRound();
        bidThirdRound();
        bidForthRound();
        combatBidBlocked();
        combatConquer();
        combatDestroy();
        combatStructural();
        bidAgain1();
    }

    public void combatBidBlocked() throws TimeoutException {
        this.combatStart();
        this.assertBidTypeBlocked(1, 0, BidType.FOOD, 1);
        combatSpell1923();
        this.assertBidTypeBlocked(1, 0, BidType.NICENESS, 1);
        adventureCDH(0, 0);
        this.assertNextRound(1, 2);
        this.assertSetBattleGround(1);
        this.assertActNow(1);
        this.sendBattleGround(1, 0, 1);
    }

    public void combatConquer() throws TimeoutException {
        this.assertBattleGroundSet(1, 0, 0, 1);
        this.assertDefendYourself(1);
        this.assertActNow(1);
        this.sendTrap(1, 26);
        this.assertTrapPlaced(1, 0, 26);
        this.assertActNow(1);
        this.sendMonster(1, 23);
        this.assertMonsterPlaced(1, 23, 0);
        spellWithDefend(18);
        spellCast(10);
        spellCast(17);
        this.assertFoodChanged(1, -4, 0);
        this.assertGoldChanged(1, -4, 0);
        this.assertTunnelConquered(1, 29, 0, 1);
        this.assertEvilnessChanged(1, -1, 0);
        this.assertNextRound(1, 3);
        this.assertSetBattleGround(1);
        this.assertActNow(1);
        this.sendBattleGround(1, 1, 1);
    }

    public void combatDestroy() throws TimeoutException {
        this.assertBattleGroundSet(1, 0, 1, 1);
        this.assertDefendYourself(1);
        this.assertActNow(1);
        this.sendMonster(1, 23);
        this.assertActionFailed(1);
        this.assertActNow(1);
        this.sendTrap(1, 26);
        this.assertGoldChanged(1, -1, 0);
        this.assertTrapPlaced(1, 0, 26);
        spellWithDefend(16);
        this.assertRoomRemoved(1, 0, 4);
        spellCast(24);
        this.assertTunnelConquered(1, 29, 1, 1);
        this.assertEvilnessChanged(1, -1, 0);
        this.assertNextRound(1, 4);
        this.assertSetBattleGround(1);
        this.assertActNow(1);
        this.sendBattleGround(1, 2, 1);
    }

    public void combatStructural() throws TimeoutException {
        this.assertBattleGroundSet(1, 0, 2, 1);
        this.assertDefendYourself(1);
        this.assertActNow(1);
        this.sendTrap(1, 26);
        this.assertTrapPlaced(1, 0, 26);
        spellWithDefend(20);
        this.assertRoomRemoved(1, 0, 0);
        spellCast(11);
        spellCast(2);
        this.assertTunnelConquered(1, 29, 2, 1);
        this.assertEvilnessChanged(1, -1, 0);
        this.biddingNextYear();
    }

    public void bidAgain1() throws TimeoutException {
        this.sendPlaceBid(1, BidType.FOOD, 1);
        this.assertActionFailed(1);
        this.assertActNow(1);
        this.sendPlaceBid(1, BidType.NICENESS, 1);
        this.assertActionFailed(1);
        this.assertActNow(1);
        this.sendLeave(1);
    }
}
