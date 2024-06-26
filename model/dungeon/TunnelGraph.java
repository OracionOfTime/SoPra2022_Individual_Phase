package de.unisaarland.cs.se.selab.model.dungeon;

import de.unisaarland.cs.se.selab.model.Model;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Graph representation of the dungeon's tunnels and rooms.
 */
public class TunnelGraph {

    private static final int ENTRY_X = 0;
    private static final int ENTRY_Y = 0;

    //map of size MAX_ROUNDS, checking when rooms can(not) be activated
    private final  Map<Integer, Boolean> blockedRooms;
    private final Map<Coordinate, Tunnel> coordinateTunnelMap;
    private final Map<Tunnel, Coordinate> tunnelCoordinateMap;
    private final Tunnel entryPoints;

    public TunnelGraph() {
        final Coordinate coordinate = new Coordinate(ENTRY_X, ENTRY_Y);
        final Tunnel entryPoint = new Tunnel(coordinate, false);
        this.coordinateTunnelMap = new HashMap<>();
        this.coordinateTunnelMap.put(coordinate, entryPoint);
        this.tunnelCoordinateMap = new HashMap<>();
        this.tunnelCoordinateMap.put(entryPoint, coordinate);
        this.entryPoints = entryPoint;
        this.blockedRooms = initializeRoomMap();
    }

    private Map<Integer, Boolean> initializeRoomMap() {
        final Map<Integer, Boolean> mappi = new HashMap<>(Model.MAX_ROUNDS);
        for (int i = 1; i <= Model.MAX_ROUNDS; i++) {
            mappi.put(i, false);

        }
        return mappi;
    }

    /**
     * After each bidding phase all values are set to false
     */
    public void clearBlockedRooms() {
        for (int i = 1; i <= Model.MAX_ROUNDS; i++) {
            this.blockedRooms.put(i, false);
        }
    }

    public Map<Integer, Boolean> getBlockedRound() {
        return this.blockedRooms;
    }

    public void setBlockedRooms(final int currentRound, final boolean blocked) {
        this.blockedRooms.put(currentRound, blocked);
    }

    public Optional<Tunnel> getTunnel(final Coordinate coordinate) {
        return Optional.ofNullable(this.coordinateTunnelMap.get(coordinate));
    }

    /**
     * Get all rooms of the dungeon sorted by their id.
     *
     * @return the sorted list of rooms
     */
    public List<Room> getRooms() {
        return this.stream()
                .map(Tunnel::getRoom)
                .filter(Optional::isPresent).map(Optional::get)
                .sorted(Comparator.comparing(Room::getId))
                .toList();
    }

    public int getNumRooms() {
        return (int) this.stream().filter(Tunnel::isRoom).count();
    }

    public int getNumTunnels() {
        return this.coordinateTunnelMap.size() - getNumRooms();
    }

    public List<Tunnel> getNeighbours(final Coordinate coordinate) {
        return coordinate.getNeighbours().stream()
                .map(this.coordinateTunnelMap::get)
                .filter(Objects::nonNull)
                .toList();
    }

    public void addTunnel(final Tunnel tunnel) {
        this.coordinateTunnelMap.put(tunnel.getCoordinate(), tunnel);
        this.tunnelCoordinateMap.put(tunnel, tunnel.getCoordinate());
    }

    /**
     * Create a stream with all tunnels in the dungeon.
     *
     * @return a stream of all tunnels in the dungeon
     */
    public Stream<Tunnel> stream() {
        return this.tunnelCoordinateMap.keySet().stream();
    }

    /**
     * Check whether the given tunnel matches the conditions for being the next battleground.
     * <p>
     * The conditions are:
     * 1. the tunnel is unconquered
     * 2. there is no other tunnel with a shorter distance to the entrance (0, 0)
     * <p>
     * This function performs an optimized BFS through the graph that exploits the fact that in each
     * step, the worklist ({@code depthLevel}) contains all nodes with the same distance from the
     * origin.
     * That means, we can exit the loop as soon as we find one unconquered tile and then only need
     * to check whether the given tile is contained in the worklist.
     * </p>
     *
     * @param tunnel the tunnel to check
     * @return whether the tunnel is a valid battleground
     */
    public boolean isClosestUnconqueredTile(final Tunnel tunnel) {
        // Provided invalid tunnel tile or no unconquered tiles are left at all.
        if (!this.tunnelCoordinateMap.containsKey(tunnel)
                || this.stream().allMatch(Tunnel::isConquered)) {
            return false;
        }

        final Set<Tunnel> visited = new HashSet<>();
        Set<Tunnel> depthLevel = new HashSet<>(List.of(this.entryPoints));
        while (!depthLevel.isEmpty()) {
            visited.addAll(depthLevel);
            final Set<Tunnel> unconqueredTunnels =
                    depthLevel.stream().filter(t -> !t.isConquered()).collect(Collectors.toSet());

            if (!unconqueredTunnels.isEmpty()) {
                return unconqueredTunnels.contains(tunnel);
            }

            // Next level contains all unvisited neighbors of the current level
            depthLevel = depthLevel.stream()
                    .flatMap(node -> getNeighbours(node.getCoordinate()).stream())
                    .filter(node -> !visited.contains(node))
                    .collect(Collectors.toSet());
        }
        return false;
    }

    /**
     * Checks for all closest rooms using the algorithm from
     * isClosestUnconqueredTile, starting from the current battleground
     * and orders them by ID, such that the closest room with the
     * lowest ID is returned
     *
     * @param startingPoint is the current battleground
     * @return the closest room
     */
    public Optional<Tunnel> getClosestRoom(final Tunnel startingPoint) {
        final Set<Tunnel> visited = new HashSet<>();
        Set<Tunnel> depthLevel = new HashSet<>(List.of(startingPoint));
        while (!depthLevel.isEmpty()) {
            visited.addAll(depthLevel);
            final List<Tunnel> closestRooms =
                    depthLevel.stream().filter(Tunnel::isRoom).toList();

            if (!closestRooms.isEmpty()) {
                final Room nearestRoom = closestRooms.stream().map(Tunnel::getRoom)
                        .filter(Optional::isPresent).map(Optional::get)
                        .min(Comparator.comparing(Room::getId))
                        .orElseThrow();
                return closestRooms.stream()
                        .filter(t -> t.getRoom().orElseThrow().getId() == nearestRoom.getId())
                        .findFirst();
            }
            depthLevel = depthLevel.stream()
                    .flatMap(node -> getNeighbours(node.getCoordinate()).stream())
                    .filter(node -> !visited.contains(node))
                    .collect(Collectors.toSet());
        }
        return Optional.empty();
    }

    public <T> List<T> map(final Function<Tunnel, T> function) {
        final List<T> result = new ArrayList<>(this.tunnelCoordinateMap.size());
        for (final Tunnel tunnel : this.tunnelCoordinateMap.keySet()) {
            result.add(function.apply(tunnel));
        }
        return result;
    }

}
