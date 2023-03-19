package sjakk.pieces;

import java.util.ArrayList;
import java.util.Collection;

import sjakk.ChessBoard;
import sjakk.PieceColor;
import sjakk.Position;

public class King extends Piece {

    public King(Position position, ChessBoard board, PieceColor color) {
        super(position, board, color, "King");
    }

    public boolean inCheck() {
        return positionIsInCheck(this.pos);
    }

    private boolean positionIsInCheck(Position position) {
        for (Piece piece : board) {
            if (piece.getColor() != color && piece.threatening(position)) {
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
                if (!p.insideBoard()) {
                    continue;
                }
                if (board.getPosition(p) != null && board.getPosition(p).getColor() == color) {
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
