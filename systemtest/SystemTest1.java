package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;

public class SystemTest1 extends SystemTest {

    protected SystemTest1() {
        super(SystemTest1.class, false);
    }

    @Override
    protected String createConfig() {
        return Utils.loadResource(RegistrationTest.class, "MarcelisStupid.json");
    }

    @Override
    protected long createSeed() {
        return 42;
    }

    @Override
    protected Set<Integer> createSockets() {
        return Set.of(1);
    }

    @Override
    protected void run() throws TimeoutException, AssertionError {
        registrationPhase();
    }

    public void registrationPhase() throws TimeoutException, AssertionError {
        final String config = createConfig();
        this.sendRegister(1, "Player1");
        this.assertConfig(1, config);

        this.sendStartGame(1);
        this.assertGameStarted(1);
        this.assertPlayer(1, "Player1", 0);

        this.assertNextYear(1, 1);
        this.assertNextRound(1, 1);
        this.assertAdventurerDrawn(1, 29);
        this.assertMonsterDrawn(1, 23);
        this.assertMonsterDrawn(1, 13);
        this.assertMonsterDrawn(1, 9);
        this.assertRoomDrawn(1, 5);
        this.assertRoomDrawn(1, 4);

        this.assertSpellDrawn(1, 19);
        this.assertSpellDrawn(1, 23);
        this.assertSpellDrawn(1, 7);

        this.assertBiddingStarted(1);

        this.assertActNow(1);

        this.sendActivateRoom(1, 5);
        this.assertActionFailed(1);
        this.assertActNow(1);
    }
}
