package sjakk;

/**
 * Represents a player in a chess game. A player has a color (white or black)
 * and turn. Castling rights are also stored.
 */
public class Player {

    private boolean white;
    private boolean hasTurn = false;
    private int dir;
    private boolean canCastleKingSide = true;
    private boolean canCastleQueenSide = true;

    /**
     * Creates a new player with the given color.
     */
    public Player(PieceColor color) {
        switch (color) {
            case WHITE:
                this.white = true;
                break;
            case BLACK:
                this.white = false;
                break;
            default:
                throw new IllegalArgumentException("Invalid color: " + color);
        }
        // White has direction 1 (up), black has direction -1 (down)
        dir = this.white ? 1 : -1;
    }

    /**
     * Toggles the turn of this player.
     */
    public void toggleHasTurn() {
        hasTurn = !hasTurn;
    }

    /**
     * Returns true if this player has the turn.
     * 
     * @return true if this player has the turn
     */
    public boolean hasTurn() {
        return hasTurn;
    }

    /**
     * Returns the color of this player as a string. ("White" or "Black")
     * 
     * @return the color as a string
     */
    @Override
    public String toString() {
        return (white ? "White" : "Black");
    }

    /**
     * Returns the color of this player as a character. ('w' or 'b')
     * 
     * @return the color as a character
     */
    public char toChar() {
        return (white ? 'w' : 'b');
    }

    /**
     * Returns the direction of this player. (1 if white, -1 if black)
     * 
     * @return the direction
     */
    public int getDir() {
        return dir;
    }

    /**
     * Returns the color of the player. (true if white, false if black)
     */
    public boolean isWhite() {
        return white;
    }

    /**
     * Returns true if this player can castle king side.
     * 
     * @return true if this player can castle king side
     */
    public boolean canCastleKingSide() {
        return canCastleKingSide;
    }

    /**
     * Returns true if this player can castle queen side.
     * 
     * @return true if this player can castle queen side
     */
    public boolean canCastleQueenSide() {
        return canCastleQueenSide;
    }

    /**
     * Sets the castling rights for this player.
     * 
     * @param value     true if the player can castle to the given side, false
     *                  otherwise
     * @param queenSide true if the player can castle queen side, false if king side
     */
    public void setCastling(boolean value, boolean queenSide) {
        if (queenSide) {
            this.canCastleQueenSide = value;
        } else {
            this.canCastleKingSide = value;
        }
    }

    /**
     * Disables all castling for this player.
     */
    public void disableCastling() {
        setCastling(false, true);
        setCastling(false, false);
    }

    /**
     * Returns the castling rights for this player as a string. ("", "k", "q" or "kq",
     * lowercase if black, upper case if white)
     * 
     * @return the castling rights as a string
     */
    public String getCastlingRights() {
        String rights = "";
        if (canCastleKingSide) {
            rights += "k";
        }
        if (canCastleQueenSide) {
            rights += "q";
        }

        if (white)
            rights = rights.toUpperCase();

        return rights;
    }
}
