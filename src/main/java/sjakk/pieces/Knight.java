package sjakk.pieces;

import java.util.ArrayList;
import java.util.Collection;

import sjakk.ChessBoard;
import sjakk.Player;
import sjakk.Position;

/**
 * A Knight is a {@link Piece} that can move in an L shape. It can move two
 * steps in one direction and one step in the other, and can jump over any
 * piece.
 * 
 * @see Piece
 */
public class Knight extends Piece {

    /**
     * The change in x for moves for the knight. The index of the array corresponds
     * to the same index in dY.
     */
    private final static int[] dX = { 2, 1, -1, -2, -2, -1, 1, 2 };

    /**
     * The change in y for moves for the knight. The index of the array corresponds
     * to the same index in dX.
     */
    private final static int[] dY = { 1, 2, 2, 1, -1, -2, -2, -1 };

    /**
     * Creates a new Knight on a position on a board for a player.
     * 
     * @param position The position of the Knight.
     * @param board    The board the Knight is on.
     * @param owner    The owner of the Knight.
     */
    public Knight(Position position, ChessBoard board, Player owner) {
        super(position, board, owner, "Knight");
    }

    @Override
    public boolean isValidMove(Position to) {
        return getLegalMoves().contains(to);
    }

    @Override
    public Collection<Position> getLegalMoves() {
        final Collection<Position> legalMoves = new ArrayList<Position>();

        for (int i = 0; i < dX.length; i++) {
            final Position to = new Position(getX() + dX[i], getY() + dY[i]);
            if (messesUpcheck(to)) {
                continue;
            }
            if (validOnBoard(to))
                legalMoves.add(to);
        }

        return legalMoves;
    }

    @Override
    protected boolean threatening(Position position) {
        for (int i = 0; i < dX.length; i++) {
            final Position to = new Position(getX() + dX[i], getY() + dY[i]);
            if (to.equals(position))
                return true;
        }
        return false;
    }

    /**
     * Returns whether or not a position is valid on the board. This takes in mind
     * if it is inside the board, if there is a piece on the given position (and
     * what player owns it).
     * 
     * @param to The position to check.
     * @return {@code true} if the position is valid, {@code false} otherwise.
     */
    private boolean validOnBoard(Position to) {
        if (!to.insideBoard())
            return false;
        if (board.getPosition(to) == null)
            return true;
        if (board.getPosition(to).getOwner() != owner)
            return true;
        return false;
    }
}
