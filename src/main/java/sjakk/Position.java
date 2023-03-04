package sjakk;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position position) {
        this.x = position.getX();
        this.y = position.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean insideBoard() {
        return getX() >= 0 && getX() < 8 && getY() >= 0 && getY() < 8;
    }

    public Position add(Position position) {
        return new Position(getX() + position.getX(), getY() + position.getY());
    }

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
