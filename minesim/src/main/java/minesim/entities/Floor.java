package minesim.entities;

public class Floor extends WorldEntity {
    public Floor(int xpos, int ypos, int height, int width) {
        super(xpos, ypos, height, width);
        setEntityGravity(Boolean.FALSE);
    }
}
