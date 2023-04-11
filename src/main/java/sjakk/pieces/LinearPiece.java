package sjakk.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sjakk.ChessBoard;
import sjakk.Player;
import sjakk.Position;

/**
 * A linear piece is a {@link Piece} that can move in a straight line for any
 * length.
 * Here verical, horizontal, and diagonal.
 * 
 * @see Bishop
 * @see Rook
 * @see Queen
 */
public abstract class LinearPiece extends Piece {

    private Collection<Position> legalDirections = new ArrayList<Position>();

    /**
     * Creates a new LinearPiece at a given position on a board for a owner with a
     * name.
     * 
     * @param position The position of the piece.
     * @param board    The board the piece is on.
     * @param owner    The owner of the piece.
     * @param name     The name of the piece.
     * @see Piece
     */
    public LinearPiece(Position position, ChessBoard board, Player owner, String name) {
        super(position, board, owner, name);
    }

    @Override
    public Collection<Position> getLegalMoves() {
        final Collection<Position> legalMoves = new ArrayList<Position>();

        for (final Position tmpDir : legalDirections) {
            Position currentPosition = new Position(pos).add(tmpDir);

            while (currentPosition.insideBoard()) {
                if (board.getPosition(currentPosition) != null
                        && board.getPosition(currentPosition).getOwner() == this.owner) {
                    break;
                }

                if (messesUpcheck(currentPosition)) {
                    // If a move would put the king in check, it is not a legal move
                    // If the position had a piece on it, we can't check further
                    if (board.getPosition(currentPosition) != null) {
                        break;
                    }
                    currentPosition = currentPosition.add(tmpDir);
                    continue;
                }

                if (board.getPosition(currentPosition) == null) {
                    legalMoves.add(currentPosition);
                } else {
                    if (board.getPosition(currentPosition).getOwner() != this.owner) {
                        legalMoves.add(currentPosition);
                    }
                    break;
                }
                currentPosition = currentPosition.add(tmpDir);
            }
        }
        return legalMoves;
    }

    @Override
    protected boolean threatening(Position position) {
        for (final Position tmpDir : legalDirections) {
            Position currentPosition = new Position(pos).add(tmpDir);
            while (currentPosition.insideBoard()) {
                if (currentPosition.equals(position)) {
                    return true;
                }
                if (board.getPosition(currentPosition) != null) {
                    break;
                }
                currentPosition = currentPosition.add(tmpDir);
            }
        }
        return false;
    }

    /**
     * Sets the directions the piece can move in.
     * 
     * @param directions The directions the piece can move in.
     */
    protected void setDirections(List<Position> directions) {
        legalDirections = new ArrayList<Position>(directions);
    }
}
