package sjakk.pieces;

import java.util.ArrayList;
import java.util.Collection;

import sjakk.ChessBoard;
import sjakk.Player;
import sjakk.Position;

public class King extends Piece {

    public King(Position position, ChessBoard board, Player owner) {
        super(position, board, owner, "King");
    }

    public boolean inCheck() {
        return positionIsInCheck(this.pos);
    }

    private boolean positionIsInCheck(Position position) {
        for (Piece piece : board) {
            if (piece.getOwner() != owner && piece.threatening(position)) {
                return true;
            }
        }
        return false;
    }

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

    private Collection<Position> getCastlingMoves() {
        Collection<Position> legalMoves = new ArrayList<Position>();

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
        Collection<Position> legalMoves = new ArrayList<Position>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                Position p = new Position(getX() + i, getY() + j);
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

                Piece newPosPiece = board.getPosition(p);

                board.setPosition(p, this);
                board.setPosition(pos, null);

                boolean posInCheck = positionIsInCheck(p);
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
                Position p = new Position(getX() + i, getY() + j);
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
        if (to.getX() == pos.getX() - 2) {
            // Casteling queen side
            board.move(board.getPosition(new Position(0, pos.getY())), new Position(3, pos.getY()));
        } else if (to.getX() == pos.getX() + 2) {
            // Casteling king side
            board.move(board.getPosition(new Position(7, pos.getY())), new Position(5, pos.getY()));
        }

        board.move(this, to);
    }
}
