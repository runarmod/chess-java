package sjakk.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sjakk.ChessBoard;
import sjakk.Player;
import sjakk.Position;

/**
 * A Pawn is a {@link Piece} that can move one step forward, capture only one
 * step diagonal forwards and two steps forward as first move. It can also make
 * an en passant.
 * 
 * @see Piece
 */
public class Pawn extends Piece {

    private boolean hasMoved = false;
    private boolean hasMadeAnPassant = false;

    /**
     * Creates a new pawn on a position on a board for a player.
     * 
     * @param position The position of the pawn.
     * @param board    The board the pawn is on.
     * @param owner    The owner of the pawn.
     */
    public Pawn(Position position, ChessBoard board, Player owner) {
        super(position, board, owner, "Pawn");
        if (getY() != (owner.isWhite() ? 1 : 6)) {
            hasMoved = true;
        }
    }

    @Override
    public boolean isValidMove(Position to) {
        if (getX() != to.getX()) {
            return isValidCaptureMove(to);
        }
        return isValidStraightMove(to);
    }

    @Override
    public Collection<Position> getLegalMoves() {
        final Collection<Position> legalMoves = new ArrayList<>();

        final Collection<Position> testPostitions = new ArrayList<>(List.of(
                new Position(pos.getX(), pos.getY() + owner.getDir()), // 1 forward
                new Position(pos.getX() + 1, pos.getY() + owner.getDir()), // 1 forward, 1 right
                new Position(pos.getX() - 1, pos.getY() + owner.getDir()) // 1 forward, 1 left
        ));

        if (!hasMoved) {
            testPostitions.add(new Position(pos.getX(), pos.getY() + 2 * owner.getDir())); // 2 forward
        }

        for (final Position p : testPostitions) {
            if (messesUpcheck(p))
                continue;
            if (isValidMove(p)) {
                legalMoves.add(p);
            }
        }

        return legalMoves;
    }

    @Override
    public void move(Position to) {
        if (!getLegalMoves().contains(to)) {
            throw new IllegalArgumentException("Illegal move");
        }
        if (moveIsEnPassant(to)) {
            this.hasMadeAnPassant = true;
        }
        board.move(this, to);
        this.pos = to;
        hasMoved = true;
    }

    /**
     * Set if the pawn has made an en passant.
     * 
     * @param hasMadeAnPassant {@code true} if the pawn has made an en passant,
     *                         {@code false} otherwise.
     */
    public void setHasMadeAnPassant(boolean hasMadeAnPassant) {
        this.hasMadeAnPassant = hasMadeAnPassant;
    }

    /**
     * Returns if the pawn has made an en passant.
     * 
     * @return {@code true} if the pawn has made an en passant, {@code false}
     *         otherwise.
     */
    public boolean getHasMadeEnPassant() {
        return hasMadeAnPassant;
    }

    @Override
    protected boolean threatening(Position position) {
        if (Math.abs(pos.getX() - position.getX()) == 1 && pos.getY() + owner.getDir() == position.getY()) {
            return true;
        }
        return false;
    }

    /**
     * Checks if a move is a valid straight-forward move.
     * 
     * @param to The position to move to.
     * @return {@code true} if the move is valid, {@code false} otherwise.
     */
    private boolean isValidStraightMove(Position to) {
        if (pos.getX() != to.getX()) {
            return false;
        }
        if (board.getPosition(to) != null) {
            return false;
        }
        if (pos.getY() + owner.getDir() == to.getY()) {
            return true;
        }
        if (!hasMoved && pos.getY() + 2 * owner.getDir() == to.getY()
                && board.getPosition(new Position(getX(), getY() + owner.getDir())) == null) {
            return true;
        }
        return false;
    }

    /**
     * Checks if a move is a valid capture move. This includes normal pawn-captures
     * aswell as en passant.
     * 
     * @param to The position to move to.
     * @return {@code true} if the move is valid, {@code false} otherwise.
     */
    private boolean isValidCaptureMove(Position to) {
        if (Math.abs(pos.getX() - to.getX()) != 1) {
            return false;
        }
        if (pos.getY() + owner.getDir() != to.getY()) {
            return false;
        }
        if (moveIsEnPassant(to)) {
            return true;
        }
        if (board.getPosition(to) == null) {
            return false;
        }
        if (board.getPosition(to).getOwner() == owner) {
            return false;
        }
        return true;
    }

    /**
     * Checks if a move is an en passant.
     * 
     * @param to The position to move to.
     * @return {@code true} if the move is an en passant, {@code false} otherwise.
     */
    private boolean moveIsEnPassant(Position to) {
        if (Math.abs(pos.getX() - to.getX()) != 1) {
            return false;
        }
        if (pos.getY() + owner.getDir() != to.getY()) {
            return false;
        }
        if (board.getPosition(to) != null) {
            return false;
        }
        final Piece possiblyTake = board.getPosition(new Position(to.getX(), to.getY() - owner.getDir()));
        if (!(possiblyTake instanceof Pawn)) {
            return false;
        }
        if (board.getLastPieceMoved() == possiblyTake && possiblyTake.getMoveCount() == 1) {
            return true;
        }
        return false;
    }
}
