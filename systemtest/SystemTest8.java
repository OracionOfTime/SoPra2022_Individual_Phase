package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class SystemTest8 extends SystemTest7 {

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
    }

    public void combat3() throws TimeoutException, AssertionError {
        this.assertNextRound(1, 3);
        this.assertSetBattleGround(1);
        this.assertActNow(1);
        this.sendBattleGround(1, 1, 1);
        this.assertBattleGroundSet(1, 0, 1, 1);
        this.assertDefendYourself(1);
        this.assertActNow(1);
        this.sendTrap(1, 26);
        this.assertGoldChanged(1, -1, 0);
        this.assertTrapPlaced(1, 0, 26);
        this.assertActNow(1);
        this.sendMonster(1, 16);
        this.assertMonsterPlaced(1, 16, 0);
        this.assertActNow(1);
        this.sendEndTurn(1);

        this.assertSpellCast(1, 0, 0);
        this.assertRoomRemoved(1, 0, 5);

        this.assertSpellCast(1, 16, 0);
        this.assertRoomsBlocked(1, 0, 3);

        this.assertSpellCast(1, 24, 0);

        this.assertAdventurerDamaged(1, 23, 2);
        this.assertAdventurerDamaged(1, 23, 2);

        this.assertAdventurerDamaged(1, 23, 1);
        this.assertAdventurerDamaged(1, 2, 1);

        this.assertTunnelConquered(1, 23, 1, 1);
        this.assertEvilnessChanged(1, -1, 0);
        this.assertAdventurerHealed(1, 3, 23, 23);
    }
}
