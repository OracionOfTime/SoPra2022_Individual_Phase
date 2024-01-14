package junitests;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.model.Player;
import de.unisaarland.cs.se.selab.spells.BiddingSpell;
import de.unisaarland.cs.se.selab.spells.Resource;
import de.unisaarland.cs.se.selab.spells.RoomSpell;
import de.unisaarland.cs.se.selab.spells.Spell;
import de.unisaarland.cs.se.selab.spells.SpellType;
import de.unisaarland.cs.se.selab.state.BuildingState;
import de.unisaarland.cs.se.selab.state.bids.Bid;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TestActivatingSpells {

    Player player1 = new Player(1, "Chris", 3,
            3, 3, 5, null);

    Player player2 = new Player(2, "Lizzi", 3,
            3, 3, 5, null);

    Player player3 = new Player(3, "Ozzy", 3,
            3, 3, 5, null);

    Map<BidType, List<Bid>> biddingSquare = new EnumMap<>(BidType.class);

    Spell spell1 = new Resource(1, SpellType.RESOURCE, BidType.NICENESS, 1, 2, 0);
    Spell spell2 = new BiddingSpell(1, SpellType.BUFF, BidType.IMPS, 2, BidType.GOLD);
    Spell spell3 = new RoomSpell(1, SpellType.STRUCTURE, BidType.GOLD, 3);

    List<Spell> spells = new ArrayList<>();

    List<Bid> niceBids = new ArrayList<>();
    List<Bid> impsBids = new ArrayList<>();
    List<Bid> goldBids = new ArrayList<>();

    BuildingState buildingState = new BuildingState(null, null);

    @Test
    void checkActivation() {
        final Bid nice = Bid.createBid(BidType.NICENESS, player1, 1);
        niceBids.add(nice);

        final Bid imps1 = Bid.createBid(BidType.IMPS, player1, 1);
        final Bid imps2 = Bid.createBid(BidType.IMPS, player2, 2);
        impsBids.add(imps1);
        impsBids.add(imps2);

        final Bid gold1 = Bid.createBid(BidType.GOLD, player1, 1);
        final Bid gold2 = Bid.createBid(BidType.GOLD, player2, 2);
        final Bid gold3 = Bid.createBid(BidType.GOLD, player3, 3);
        goldBids.add(gold1);
        goldBids.add(gold2);
        goldBids.add(gold3);

        biddingSquare.put(BidType.NICENESS, niceBids);
        biddingSquare.put(BidType.IMPS, impsBids);
        biddingSquare.put(BidType.GOLD, goldBids);

        spells.add(spell1);
        spells.add(spell2);
        spells.add(spell3);

        buildingState.activateTriggers(spells, biddingSquare);
        final boolean triggered1 = biddingSquare.get(BidType.NICENESS).get(0).isTriggered();
        final boolean triggered2 = biddingSquare.get(BidType.IMPS).get(1).isTriggered();
        final boolean triggered3 = biddingSquare.get(BidType.GOLD).get(2).isTriggered();

        assert (triggered1);
        assert (triggered2);
        assert (triggered3);

        final int spellId1 = biddingSquare.get(BidType.NICENESS).get(0)
                .getSpell().get(0).get().getId();
        final int spellId2 = biddingSquare.get(BidType.IMPS).get(1).getSpell().get(0).get().getId();
        final int spellId3 = biddingSquare.get(BidType.GOLD).get(2).getSpell().get(0).get().getId();

        assert (spellId1 == spell1.getId());
        assert (spellId2 == spell2.getId());
        assert (spellId3 == spell3.getId());
    }
}
