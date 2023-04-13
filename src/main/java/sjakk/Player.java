package sjakk;

public class Player {

    /**
     * true = white,
     * false = black
     */
    private boolean color;
    private float time = 0.0f;
    private boolean hasTurn = false;
    private int dir;
    private boolean canCastleKingSide = true;
    private boolean canCastleQueenSide = true;

    public Player(PieceColor white) {
        switch (white) {
            case WHITE:
                this.color = true;
                break;
            case BLACK:
                this.color = false;
                break;
            default:
                throw new IllegalArgumentException("Invalid color: " + white);
        }
        // If white, dir = 1, else -1
        dir = this.color ? 1 : -1;
    }

    /**
     * @return the color (1 if white, 0 if black)
     */
    public boolean getColor() {
        return color;
    }

    public float getTime() {
        return time;
    }

    public void addTime(float time) {
        this.time += time;
    }

    public void toggleHasTurn() {
        hasTurn = !hasTurn;
    }

    public boolean hasTurn() {
        return hasTurn;
    }

    @Override
    public String toString() {
        // return "Player [color=" + color + ", time=" + time + ", hasTurn=" + hasTurn +
        // "]";
        return (color ? "White" : "Black");
    }

    public char toChar() {
        return (color ? 'w' : 'b');
    }

    public int getDir() {
        return dir;
    }

    public boolean isWhite() {
        return color;
    }

    public boolean canCastleKingSide() {
        return canCastleKingSide;
    }

    public boolean canCastleQueenSide() {
        return canCastleQueenSide;
    }

    public void setCastling(boolean value, boolean queenSide) {
        if (queenSide) {
            this.canCastleQueenSide = value;
        } else {
            this.canCastleKingSide = value;
        }
    }

    public void disableCastling() {
        setCastling(false, true);
        setCastling(false, false);
    }

    public String getCastlingRights() {
        String rights = "";
        if (canCastleKingSide) {
            rights += "k";
        }
        if (canCastleQueenSide) {
            rights += "q";
        }

        if (color)
            rights = rights.toUpperCase();

        return rights;
    }
}
