package sjakk.pieces;

import java.util.ArrayList;
import java.util.Collection;

import sjakk.ChessBoard;
import sjakk.Player;
import sjakk.Position;

/**
 * A King is a {@link Piece} that can move one step in any direction. It can
 * also castle towards a rook.
 */
public class King extends Piece {

    /**
     * Creates a new king on a position on a board for a player.
     * 
     * @param position The position of the king.
     * @param board    The board the king is on.
     * @param owner    The owner of the king.
     */
    public King(Position position, ChessBoard board, Player owner) {
        super(position, board, owner, "King");
    }

    /**
     * Returns whether or not the king is in check.
     * 
     * @return {@code true} if the king is in check, {@code false} otherwise.
     */
    public boolean inCheck() {
        return positionIsInCheck(this.pos);
    }

    /**
     * Returns whether or not a position is in check for the king.
     * 
     * @param position The position to check.
     * @return {@code true} if the position is in check, {@code false} otherwise.
     */
    private boolean positionIsInCheck(Position position) {
        for (final Piece piece : board) {
            if (piece.getOwner() != owner && piece.threatening(position)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether or not the king can castle towards the queen side.
     * 
     * @return {@code true} if the king can castle towards the queen side,
     *         {@code false} otherwise.
     */
    private boolean canCastleQueen() {
        if (!owner.canCastleQueenSide()) {
            return false;
        }

        // Check if king is in check
        if (inCheck()) {
            return false;
        }

        // Check if king is in check after moving one step
        if (positionIsInCheck(new Position(pos.getX() - 1, pos.getY()))) {
            return false;
        }

        // Check if king is in check after moving two steps
        if (positionIsInCheck(new Position(pos.getX() - 2, pos.getY()))) {
            return false;
        }

        // Check if there are pieces between the king and the rook (in this case 3
        // moves)
        for (int i = pos.getX() - 1; i > pos.getX() - 4; i--) {
            if (board.getPosition(new Position(i, pos.getY())) != null) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns whether or not the king can castle towards the king side.
     * 
     * @return {@code true} if the king can castle towards the king side,
     *         {@code false} otherwise.
     */
    private boolean canCastleKing() {
        if (!owner.canCastleKingSide()) {
            return false;
        }

        // Check if king is in check
        if (inCheck()) {
            return false;
        }

        // Check if king is in check after moving one step
        if (positionIsInCheck(new Position(pos.getX() + 1, pos.getY()))) {
            return false;
        }

        // Check if king is in check after moving two steps
        if (positionIsInCheck(new Position(pos.getX() + 2, pos.getY()))) {
            return false;
        }

        // Check if there are pieces between the king and the rook (in this case 2
        // moves)
        for (int i = pos.getX() + 1; i < pos.getX() + 3; i++) {
            if (board.getPosition(new Position(i, pos.getY())) != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a collection of positions that the king can castle towards. This
     * would be either the position towards the kingside rook, queenside rook,
     * neither, or both.
     * 
     * @return A collection of positions that the king can castle towards.
     */
    private Collection<Position> getCastlingMoves() {
        final Collection<Position> legalMoves = new ArrayList<Position>();

        if (canCastleQueen()) {
            legalMoves.add(new Position(pos.getX() - 2, pos.getY()));
        }

        if (canCastleKing()) {
            legalMoves.add(new Position(pos.getX() + 2, pos.getY()));
        }

        return legalMoves;
    }

    @Override
    public Collection<Position> getLegalMoves() {
        final Collection<Position> legalMoves = new ArrayList<Position>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                final Position p = new Position(getX() + i, getY() + j);
                if (positionIsInCheck(p)) {
                    continue;
                }
                // Has to be inside board
                if (!p.insideBoard()) {
                    continue;
                }

                // Can't move to a position with a piece of the same color
                if (board.getPosition(p) != null && board.getPosition(p).getOwner() == owner) {
                    continue;
                }

                final Piece newPosPiece = board.getPosition(p);

                board.setPosition(p, this);
                board.setPosition(pos, null);

                final boolean posInCheck = positionIsInCheck(p);
                board.setPosition(p, newPosPiece);
                board.setPosition(pos, this);

                if (!posInCheck)
                    legalMoves.add(p);
            }
        }

        legalMoves.addAll(getCastlingMoves());
        return legalMoves;
    }

    @Override
    protected boolean threatening(Position position) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                final Position p = new Position(getX() + i, getY() + j);
                if (p.equals(position)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void move(Position to) throws IllegalArgumentException {
        if (!isValidMove(to)) {
            throw new IllegalArgumentException("Illegal move");
        }

        // Castling
        final int y = pos.getY();
        if (to.getX() == pos.getX() - 2) {
            // Castling queen side
            board.move(board.getPosition(new Position(0, y)), new Position(3, y), true);
        } else if (to.getX() == pos.getX() + 2) {
            // Castling king side
            board.move(board.getPosition(new Position(7, y)), new Position(5, y), true);
        }

        // Move the king himself
        board.move(this, to, true);
    }
}
