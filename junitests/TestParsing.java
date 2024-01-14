package junitests;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.config.ConfigParser;
import de.unisaarland.cs.se.selab.config.ModelBuilder;
import de.unisaarland.cs.se.selab.config.ModelBuilderInterface;
import de.unisaarland.cs.se.selab.config.ModelValidator;
import de.unisaarland.cs.se.selab.model.Model;
import de.unisaarland.cs.se.selab.spells.Spell;
import de.unisaarland.cs.se.selab.spells.SpellType;
import de.unisaarland.cs.se.selab.systemtest.RegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.List;
import org.junit.jupiter.api.Test;


class TestParsing {

    String config = Utils.loadResource(RegistrationTest.class, "configuration.json");

    @Test
    void first3SpellsParsing() {
        final ModelBuilderInterface<Model> builder = new ModelValidator<>(new ModelBuilder());
        builder.setSeed(42);

        final Model model = ConfigParser.parse(config, builder);
        model.shuffleCards();

        model.drawSpell();
        model.drawSpell();
        model.drawSpell();

        final List<Spell> spell = model.getAvailableSpells();
        final Spell spell1 = spell.get(0);
        final SpellType  spellType1 = spell1.getSpellType();
        final BidType bidType1 = spell1.getBidType();
        final int slot1 = spell1.getSlot();
        assert (spell1.getSlot() == slot1);
        assert (spell1.getSpellType() == spellType1);
        assert (spell1.getBidType() == bidType1);

        final Spell spell2 = spell.get(1);
        final SpellType  spellType2 = spell2.getSpellType();
        final BidType bidType2 = spell2.getBidType();
        final int slot2 = spell2.getSlot();
        assert (spell2.getSlot() == slot2);
        assert (spell2.getSpellType() == spellType2);
        assert (spell2.getBidType() == bidType2);

        final Spell spell3 = spell.get(2);
        final SpellType  spellType3 = spell3.getSpellType();
        final BidType bidType3 = spell3.getBidType();
        final int slot3 = spell3.getSlot();
        assert (spell3.getSlot() == slot3);
        assert (spell3.getSpellType() == spellType3);
        assert (spell3.getBidType() == bidType3);

        final int size = spell.size();
        assert (size == 3);

        assert (spell1.getId() == 19);
        assert (spell2.getId() == 23);
        assert (spell3.getId() == 7);
    }
}
