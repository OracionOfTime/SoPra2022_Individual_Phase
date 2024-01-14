package de.unisaarland.cs.se.selab.systemtest;


import de.unisaarland.cs.se.selab.systemtest.api.SystemTestManager;


final class SystemTestsRegistration {

    private SystemTestsRegistration() {
        // empty
    }

    static void registerSystemTests(final SystemTestManager manager) {
        manager.registerTest(new RegistrationTest());
        manager.registerTest(new EmptyConfigTest());
        manager.registerTest(new SystemTest1());
        manager.registerTest(new SystemTest2());
        manager.registerTest(new SystemTest3());
        manager.registerTest(new SystemTest4());
        manager.registerTest(new SystemTest5());
        manager.registerTest(new SystemTest6());
        manager.registerTest(new SystemTest7());
        manager.registerTest(new SystemTest8());
        manager.registerTest(new SystemTest9());
        manager.registerTest(new SystemTest10());
        manager.registerTest(new SystemTest11());
        manager.registerTest(new SystemTest12());
        manager.registerTest(new SystemTest13());
        manager.registerTest(new SystemTest14());
        manager.registerTest(new SystemTest15());
        manager.registerTest(new SystemTest16());
        manager.registerTest(new SystemTest17());
    }
}
