package de.unisaarland.cs.se.selab.state;

import de.unisaarland.cs.se.selab.ConnectionUtils;
import de.unisaarland.cs.se.selab.ConnectionWrapper;
import de.unisaarland.cs.se.selab.commands.ActionResult;
import de.unisaarland.cs.se.selab.model.Adventurer;
import de.unisaarland.cs.se.selab.model.DefensiveMeasure;
import de.unisaarland.cs.se.selab.model.Model;
import de.unisaarland.cs.se.selab.model.Monster;
import de.unisaarland.cs.se.selab.model.Player;
import de.unisaarland.cs.se.selab.model.dungeon.Coordinate;
import de.unisaarland.cs.se.selab.model.dungeon.Dungeon;
import de.unisaarland.cs.se.selab.model.dungeon.Tunnel;
import de.unisaarland.cs.se.selab.spells.Spell;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * This state handles the combat for all players.
 */
public final class CombatState extends State {

    private static final int EVILNESS_WHEN_CONQUERED = -1;
    private static final int FATIGUE_DAMAGE = 2;

    //checks if Buff Spell was cast
    private boolean bufferActive;

    public CombatState(final Model model, final ConnectionWrapper connection) {
        super(model, connection);
        this.bufferActive = false;
    }

    public void setBufferActive(final boolean bufferActive) {
        this.bufferActive = bufferActive;
    }

    @Override
    public State run() {
        for (final Player player : model.getPlayers()) {

            if (handleCombatForPlayer(player) == ActionResult.END_GAME) {
                return new EndGameState(this.model, this.connection);
            }
        }

        if (model.hasNextYear()) {
            return new BuildingState(this.model, this.connection);
        } else {
            evaluateTitles();
            return new EndGameState(this.model, this.connection);
        }
    }

    /**
     * Simulate all combat rounds for one player.
     *
     * @param player the player who does combat
     * @return a result that indicates how the game should continue
     */
    private ActionResult handleCombatForPlayer(final Player player) {
        //player.getDungeon().calculateTotalMagicPoints();
        while (model.hasNextRound()) {
            final Dungeon dungeon = player.getDungeon();
            if (player.isAlive() && dungeon.adventurersLeft()) {
                connection.sendNextRound(model.getRound());
                if (dungeon.isConquered()) {
                    prisonerFlees(player);
                } else {
                    if (fight(player) == ActionResult.END_GAME) {
                        return ActionResult.END_GAME;
                    }
                }
            }
        }

        //when combat ends and Arch mage was activated, 'linusArrived' attribute is set to false
        if (player.getDungeon().getLinusArrived()) {
            returnHomeLinus(player);
        }
        return ActionResult.PROCEED;
    }

    private void prisonerFlees(final Player player) {
        final Dungeon dungeon = player.getDungeon();
        if (dungeon.getNumImprisonedAdventurers() > 0) {
            connection.sendAdventurerFled(dungeon.freeAdventurer().getId());
            if (player.getEvilness() > Player.MIN_EVILNESS) {
                player.changeEvilness(EVILNESS_WHEN_CONQUERED);
                connection.sendEvilnessChanged(player.getId(), EVILNESS_WHEN_CONQUERED);
            }
        }
    }

    /**
     * Simulate a single combat round for a player.
     *
     * @param player the player who does combat
     * @return a result that indicates how the game should continue
     */
    private ActionResult fight(final Player player) {
        // Player can select a battleground.
        connection.sendSetBattleGround(player.getId());
        final ActionResult battlegroundResult = ConnectionUtils.executePlayerCommand(
                model, connection, Phase.SET_BATTLEGROUND, player);
        if (!player.isAlive()) {
            return battlegroundResult;
        }
        // Player can place monsters or traps.
        connection.sendDefendYourself(player.getId());
        final ActionResult defendResult =
                ConnectionUtils.executePlayerCommand(model, connection, Phase.COMBAT, player);
        if (!player.isAlive()) {
            return defendResult;
        }

        //calculate total magic points, in case an adventurer was imprisoned
        player.getDungeon().calculateTotalMagicPoints();
        final ActionResult action = executeSpells(model.getRound(), player);

        if (!player.isAlive()) {
            return action;
        }

        //if Structure spell with Conquer effect is cast, damaging of adventurers does not happen
        if (player.getDungeon().getBattleGround().get().isConquered()) {
            adventurersConquer(player);
            unBuffAdventurers(player);
            return ActionResult.PROCEED;
        }
        damageAdventurers(player);
        adventurersConquer(player);
        priestsHeal(player.getDungeon().getAllAdventurers());
        unBuffAdventurers(player);
        return ActionResult.PROCEED;
    }

    /**
     * If spells can be cast in the given round, they are removed from the
     * spells, that can be used during combat and executes the given effect
     *
     * @param round current combat round
     * @param player current player
     * @return Action.PROCEED if player did not leave
     */
    private ActionResult executeSpells(final int round, final Player player) {
        ActionResult spellResult = ActionResult.PROCEED;
        while (player.getDungeon().getSpells().size() > 0
                && player.getDungeon().getSpells().peek() != null
                && player.getDungeon().getSpells().peek().getRound() == round) {
            final Spell spell = player.getDungeon().getSpells().poll();
            spellResult = spell.castSpell(model, connection, player, this);
            if (!player.isAlive()) {
                return spellResult;
            }
        }
        return spellResult;
    }

    /**
     * Called after each round of combat:
     * If Buff spell was cast in this round, buffed attributes are reset
     *
     * @param player current player
     */
    private void unBuffAdventurers(final Player player) {
        if (bufferActive) {
            this.setBufferActive(false);
            for (final Adventurer adv : player.getDungeon().getAllAdventurers()) {
                adv.unBuffHealthPoints();
                if (adv.getHealValue() > 0) {
                    adv.unBuffHealValue();
                    adv.setBuffedHealValue(0);

                }
                if (adv.getDefuseValue() > 0) {
                    adv.unBuffDefuseValue();
                    adv.setBuffedDefuseValue(0);
                }
            }
        }
    }

    private void priestsHeal(final Iterable<Adventurer> adventurers) {
        for (final Adventurer healer : adventurers) {
            int healValue = healer.getHealValue();
            for (final Adventurer target : adventurers) {
                final int healed = target.heal(healValue);
                healValue -= healed;
                if (healed > 0) {
                    connection.sendAdventurerHealed(healer.getId(), target.getId(), healed);
                }
            }
        }
    }

    public void adventurersConquer(final Player player) {
        final Dungeon dungeon = player.getDungeon();
        final Optional<Tunnel> battleGround = dungeon.getBattleGround();
        if (battleGround.isPresent()) {
            final Optional<Adventurer> attacker = dungeon.getAdventurer(1);
            if (attacker.isPresent()) {
                dungeon.conquer();
                final Coordinate coordinate = battleGround.get().getCoordinate();
                connection.sendTunnelConquered(attacker.get().getId(), coordinate);
                if (player.getEvilness() > Player.MIN_EVILNESS) {
                    player.changeEvilness(EVILNESS_WHEN_CONQUERED);
                    connection.sendEvilnessChanged(player.getId(), EVILNESS_WHEN_CONQUERED);
                }
            }
            battleGround.get().clearDefenders();
        }
    }

    private void damageAdventurers(final Player player) {
        final Dungeon dungeon = player.getDungeon();
        final Optional<Tunnel> battleGround = dungeon.getBattleGround();
        if (battleGround.isPresent()) {
            final int reduction = dungeon.getAllAdventurers()
                    .stream()
                    .map(Adventurer::getDefuseValue)
                    .reduce(0, Integer::sum);
            // Damage from trap if there is one.
            battleGround.get().getTrap()
                    .ifPresent(trap -> evaluateDefense(trap, player, reduction));
            // Damage from placed monsters.
            for (final Monster monster : battleGround.get().getMonsters()) {
                evaluateDefense(monster, player, 0);
            }
        }
        // Deal fatigue damage
        for (final Adventurer adventurer : dungeon.getAllAdventurers()) {

            // If Arch mage already arrived, the fatigue damage is set to 1
            if (player.getDungeon().getLinusArrived()) {
                hurtAdventurer(adventurer, player, 1);
            } else {
                hurtAdventurer(adventurer, player, CombatState.FATIGUE_DAMAGE);
            }
        }
    }

    private void evaluateDefense(final DefensiveMeasure defense, final Player player,
                                 final int totalReduction) {
        final Dungeon dungeon = player.getDungeon();
        switch (defense.getAttackStrategy()) {
            case BASIC -> {
                final int reducedDamage = Math.max(0, defense.getDamage() - totalReduction);
                final Optional<Adventurer> attacker = dungeon.getAdventurer(1);
                attacker.ifPresent(adventurer -> hurtAdventurer(adventurer, player, reducedDamage));
            }
            case MULTI -> {
                int alreadyReduced = 0;
                for (final Adventurer adventurer : dungeon.getAllAdventurers()) {
                    final int reductionLeft = totalReduction - alreadyReduced;
                    hurtAdventurer(adventurer, player,
                            Math.max(defense.getDamage() - reductionLeft, 0));
                    alreadyReduced += Math.min(defense.getDamage(), reductionLeft);
                }
            }
            case TARGETED -> {
                if (defense.hasTarget()) {
                    final int reducedDamage = Math.max(0, defense.getDamage() - totalReduction);
                    final Optional<Adventurer> attacker =
                            dungeon.getAdventurer(defense.getTarget());
                    attacker.ifPresent(adventurer -> hurtAdventurer(adventurer, player,
                            reducedDamage));
                }
            }
            default -> {
            }
        }
    }

    private void hurtAdventurer(final Adventurer adventurer, final Player player,
                                final int damage) {
        final int effectiveDamage = adventurer.damage(damage);
        if (effectiveDamage > 0) {
            if (adventurer.isDefeated()) {
                player.getDungeon().imprisonAdventurer(adventurer);
                connection.sendAdventurerImprisoned(adventurer.getId());
                //player.getDungeon().calculateTotalMagicPoints();

                //if imprisoned adventurer had magic points and linus did not already arrive,
                //it is checked if he arrives
                if (adventurer.getMagicPoints() > 0 && (!(player.getDungeon().getLinusArrived()))) {
                    linusArrival(player);
                } else if (!(player.getDungeon().adventurersLeft())) {
                    returnHomeLinus(player);
                }
            } else {
                connection.sendAdventurerDamaged(adventurer.getId(), effectiveDamage);
            }
        }
    }

    /**
     * If Arch mage arrives, his effects are executed
     *
     * @param player current player
     */
    private void linusArrival(final Player player) {
        if ((model.getRandom().nextInt(model.getMaxYear() + 15)
                < (player.getEvilness() + model.getYear()))
                && player.getDungeon().adventurersLeft()) {
            connection.sendArchmageArrived(player.getId());
            player.setWithstoodMage(player.getWithstoodMage() + 1);
            player.getDungeon().setLinusArrived(true);
            castArchMageSpell(player);
        }
    }

    /**
     * If player has any monsters, one of them is removed, their damage is set to 0,
     * in case of MULTI attack strategy and the total magic points are increased
     *
     * @param player current player
     */
    private void castArchMageSpell(final Player player) {
        if (player.getMonsters().size() > 0) {
            final int monster = model.getRandom().nextInt(player.getMonsters().size());
            final Monster removedMonster = player.getMonsters().get(monster);
            player.getMonsters().remove(removedMonster);
            removedMonster.setDamage(0);
            connection.sendMonsterRemoved(player.getId(), removedMonster.getId());
        }
        //player.getDungeon().setTotalMagicPoints(player.getDungeon().getTotalMagicPoints() + 3);
    }

    /**
     * After round ends or all adventurers are imprisoned, 'linusArrived' is set to false
     *
     * @param player current player
     */
    public void returnHomeLinus(final Player player) {
        player.getDungeon().setLinusArrived(false);
    }

    /**
     * Calculates player scores and awards titles at the end of a game.
     */
    private void evaluateTitles() {
        final List<Player> players = model.getPlayers();

        // Scoring individual points
        for (final Player player : players) {
            // 1 point per monster.
            player.changeScorePoints(player.getMonsters().size());
            // 2 points per room.
            player.changeScorePoints(2 * player.getDungeon().getGraph().getNumRooms());
            // -2 points per conquered tunnel! tile.
            player.changeScorePoints(
                    -2 * (int) player.getDungeon().getGraph().stream()
                            .filter(tunnel -> tunnel.isConquered() && !tunnel.isRoom())
                            .count());
            // 2 points per imprisoned adventurer.
            player.changeScorePoints(2 * player.getDungeon().getNumImprisonedAdventurers());
        }

        // Define all titles.
        final List<Function<Player, Integer>> titles = new ArrayList<>();      // The Lord of ...
        titles.add(Player::getEvilness);                                       // Dark Deeds
        titles.add(player -> player.getDungeon().getGraph().getNumRooms());    // Halls
        titles.add(player -> player.getDungeon().getGraph().getNumTunnels());  // Tunnels
        titles.add(player -> player.getMonsters().size());                     // Monsters
        titles.add(Player::getImps);                                           // Imps
        titles.add(player -> player.getGold() + player.getFood());             // Riches
        titles.add(player -> player.getDungeon().getNumUnconqueredTiles());    // Battle
        titles.add(Player::getWithstoodSpells);                                // Magic Proof
        titles.add(Player::getWithstoodMage);                                  // Penguin Visit
        titles.add(Player::getNumberOfCounterSpells);                          // Counter Strike

        // Evaluate all titles.
        for (final Function<Player, Integer> title : titles) {
            processTitle(players, title);
        }

        // Find winner.
        final Optional<Player> optWinner =
                players.stream().max(Comparator.comparing(Player::getScorePoints));

        if (optWinner.isPresent()) {
            final int highestScore = optWinner.get().getScorePoints();
            for (final Player player : model.getPlayers()) {
                if (player.getScorePoints() == highestScore) {
                    connection.sendGameEnd(player.getId(), highestScore);
                }
            }
        }
    }

    private void processTitle(final Collection<Player> players,
                              final Function<Player, Integer> title) {
        final Optional<Player> optWinner = players.stream()
                .max(Comparator.comparing(title));
        if (optWinner.isPresent()) {
            final int highestScore = title.apply(optWinner.get());
            int amountOfWinners = 0;
            for (final Player player : players) {
                if (title.apply(player) == highestScore) {
                    amountOfWinners++;
                    player.changeScorePoints(2);
                }
            }
            if (amountOfWinners == 1) {
                optWinner.get().changeScorePoints(1);
            }
        }
    }
}
