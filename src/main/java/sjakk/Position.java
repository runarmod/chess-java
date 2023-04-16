package sjakk;

import javafx.scene.input.MouseEvent;

/**
 * Represents a position on the chess board. A position is defined by an x and y
 * coordinate. The x coordinate is the column, and the y coordinate is the row.
 * The lower left corner is (0, 0), and the top right corner is (7, 7).
 */
public class Position {
    private int x;
    private int y;

    /**
     * Creates a new position with the given x and y coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a new position with the same coordinates as the given position.
     * 
     * @param position the position to copy
     */
    public Position(Position position) {
        this.x = position.getX();
        this.y = position.getY();
    }

    /**
     * Creates a new position from the given mouse event. The x and y coordinates
     * are calculated from the x and y coordinates of the mouse event based on the
     * grid-square sizes.
     * 
     * @param event the mouse event
     */
    public Position(MouseEvent event) {
        this((int) event.getX() / 50, 7 - (int) event.getY() / 50);
    }

    /**
     * Creates a new position from the given string. The string must be in the
     * format "a1", "b2", etc. x- and y-positions 0, 1, 2,... corresponds to a, b,
     * c,..., and 1, 2, 3,..., respectively.
     * 
     * @param position the position string
     */
    public Position(String position) {
        if (position.length() != 2) {
            throw new IllegalArgumentException("Invalid position string: " + position);
        }

        char x = position.charAt(0);
        char y = position.charAt(1);

        if (x < 'a' || x > 'h' || y < '1' || y > '8') {
            throw new IllegalArgumentException("Invalid position string: " + position);
        }

        this.x = x - 'a';
        this.y = y - '1';
    }

    /**
     * Returns the x coordinate of this position.
     * 
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of this position.
     * 
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns true if this position is inside the board.
     * 
     * @return true if this position is inside the board
     */
    public boolean insideBoard() {
        return getX() >= 0 && getX() < 8 && getY() >= 0 && getY() < 8;
    }

    /**
     * Returns a new position that is the sum of this position and the given
     * position.
     * 
     * @param position the position to add
     * @return the sum of this position and the given position
     */
    public Position add(Position position) {
        return new Position(getX() + position.getX(), getY() + position.getY());
    }

    /**
     * Return the string representation of this position. The string is in the
     * format "a1", "b2", etc. x- and y-positions 0, 1, 2,... corresponds to a, b,
     * c,..., and 1, 2, 3,..., respectively.
     */
    @Override
    public String toString() {
        return String.format("%c%d", getX() + 'a', getY() + 1);
    }

    @Override
    public int hashCode() {
        return getY() * 8 + getX();
    }

    @Override
    public boolean equals(Object obj) {
        return hashCode() == obj.hashCode();
    }
}
