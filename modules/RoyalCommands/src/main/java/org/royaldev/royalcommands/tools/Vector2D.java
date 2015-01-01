package org.royaldev.royalcommands.tools;

/**
 * A two-dimsensional vector.
 */
public class Vector2D {

    private final int x, y;

    public Vector2D(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the X-coordinate.
     *
     * @return int
     */
    public int getX() {
        return this.x;
    }

    /**
     * Gets the Y-coordinate.
     *
     * @return int
     */
    public int getY() {
        return this.y;
    }
}
