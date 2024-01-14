package junitests;

import de.unisaarland.cs.se.selab.model.dungeon.Coordinate;
import de.unisaarland.cs.se.selab.model.dungeon.Room;
import de.unisaarland.cs.se.selab.model.dungeon.Tunnel;
import de.unisaarland.cs.se.selab.model.dungeon.TunnelGraph;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class TestClosestRoom {

    Coordinate coordinate00 = new Coordinate(0, 0);
    Coordinate coordinate10 = new Coordinate(1, 0);
    Coordinate coordinate11 = new Coordinate(1, 1);
    Coordinate coordinate12 = new Coordinate(1, 2);
    Coordinate coordinate22 = new Coordinate(2, 2);
    Coordinate coordinate32 = new Coordinate(3, 2);
    Coordinate coordinate33 = new Coordinate(3, 3);
    Coordinate coordinate34 = new Coordinate(3, 4);
    Coordinate coordinate24 = new Coordinate(2, 4);

    Room room1 = new Room(1, 0, null, null);
    Room room2 = new Room(2, 0, null, null);
    Room room3 = new Room(3, 0, null, null);
    Room room4 = new Room(4, 0, null, null);

    Tunnel tunnel00 = new Tunnel(coordinate00, false);
    Tunnel tunnel10 = new Tunnel(coordinate10, false);
    Tunnel tunnel11 = new Tunnel(coordinate11, false);
    Tunnel tunnel12 = new Tunnel(coordinate12, false);
    Tunnel tunnel22 = new Tunnel(coordinate22, false);
    Tunnel tunnel32 = new Tunnel(coordinate32, false);
    Tunnel tunnel33 = new Tunnel(coordinate33, false);
    Tunnel tunnel34 = new Tunnel(coordinate34, false);
    Tunnel tunnel24 = new Tunnel(coordinate24, false);

    TunnelGraph tunnelGraph = new TunnelGraph();

    @Test
    void closestRoomNormal2() {
        tunnel11.buildRoom(room1);
        tunnel24.buildRoom(room2);


        tunnelGraph.addTunnel(tunnel00);
        tunnelGraph.addTunnel(tunnel10);
        tunnelGraph.addTunnel(tunnel11);
        tunnelGraph.addTunnel(tunnel12);
        tunnelGraph.addTunnel(tunnel22);
        tunnelGraph.addTunnel(tunnel32);
        tunnelGraph.addTunnel(tunnel33);
        tunnelGraph.addTunnel(tunnel34);
        tunnelGraph.addTunnel(tunnel24);

        assert (tunnelGraph.getClosestRoom(tunnel22).get().equals(tunnel11));
    }

    @Test
    void closestRoomNormal1() {
        tunnel11.buildRoom(room1);

        tunnelGraph.addTunnel(tunnel00);
        tunnelGraph.addTunnel(tunnel10);
        tunnelGraph.addTunnel(tunnel11);
        tunnelGraph.addTunnel(tunnel12);
        tunnelGraph.addTunnel(tunnel22);
        tunnelGraph.addTunnel(tunnel32);
        tunnelGraph.addTunnel(tunnel33);
        tunnelGraph.addTunnel(tunnel34);
        tunnelGraph.addTunnel(tunnel24);

        assert (tunnelGraph.getClosestRoom(tunnel22).get().equals(tunnel11));
    }

    @Test
    void closestRoom2SameDistance() {
        tunnel11.buildRoom(room4);
        tunnel33.buildRoom(room3);

        tunnelGraph.addTunnel(tunnel00);
        tunnelGraph.addTunnel(tunnel10);
        tunnelGraph.addTunnel(tunnel11);
        tunnelGraph.addTunnel(tunnel12);
        tunnelGraph.addTunnel(tunnel22);
        tunnelGraph.addTunnel(tunnel32);
        tunnelGraph.addTunnel(tunnel33);
        tunnelGraph.addTunnel(tunnel34);
        tunnelGraph.addTunnel(tunnel24);

        assert (tunnelGraph.getClosestRoom(tunnel22).get().equals(tunnel33));
    }

    @Test
    void closesRoomNormal0() {
        tunnelGraph.addTunnel(tunnel00);
        tunnelGraph.addTunnel(tunnel10);
        tunnelGraph.addTunnel(tunnel11);
        tunnelGraph.addTunnel(tunnel12);
        tunnelGraph.addTunnel(tunnel22);
        tunnelGraph.addTunnel(tunnel32);
        tunnelGraph.addTunnel(tunnel33);
        tunnelGraph.addTunnel(tunnel34);
        tunnelGraph.addTunnel(tunnel24);

        assert (tunnelGraph.getClosestRoom(tunnel22).equals(Optional.empty()));
    }

    @Test
    void closestRoomNormal3() {
        tunnel10.buildRoom(room4);
        tunnel34.buildRoom(room3);
        tunnel32.buildRoom(room1);

        tunnelGraph.addTunnel(tunnel00);
        tunnelGraph.addTunnel(tunnel10);
        tunnelGraph.addTunnel(tunnel11);
        tunnelGraph.addTunnel(tunnel12);
        tunnelGraph.addTunnel(tunnel22);
        tunnelGraph.addTunnel(tunnel32);
        tunnelGraph.addTunnel(tunnel33);
        tunnelGraph.addTunnel(tunnel34);
        tunnelGraph.addTunnel(tunnel24);

        assert (tunnelGraph.getClosestRoom(tunnel32).get().equals(tunnel32));
    }

    @Test
    void closestRoomNormal4() {
        tunnel00.buildRoom(room4);
        tunnel11.buildRoom(room2);
        tunnel33.buildRoom(room3);
        tunnel24.buildRoom(room1);

        tunnelGraph.addTunnel(tunnel00);
        tunnelGraph.addTunnel(tunnel10);
        tunnelGraph.addTunnel(tunnel11);
        tunnelGraph.addTunnel(tunnel12);
        tunnelGraph.addTunnel(tunnel22);
        tunnelGraph.addTunnel(tunnel32);
        tunnelGraph.addTunnel(tunnel33);
        tunnelGraph.addTunnel(tunnel34);
        tunnelGraph.addTunnel(tunnel24);

        assert (tunnelGraph.getClosestRoom(tunnel22).get().equals(tunnel11));
    }
}
