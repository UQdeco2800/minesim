package minesim.entities.items;

/**
 * The rope item type class represents a type of transportation item.
 * @author alana_clover
 *
 */
public class Rope extends Transportation {

    public Rope(int xpos, int ypos, int height, int width, String name, int stackLimit) {

        super(xpos, ypos, height, width, name, stackLimit);
        setName("Rope");
        setSpeed(1);
        setImageurl("TransportationImages/rope.png");
        setEntityGravity(false);
    }
}
