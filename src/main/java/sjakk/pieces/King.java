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

}
