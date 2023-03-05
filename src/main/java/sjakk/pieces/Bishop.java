package sjakk.pieces;

import java.util.Collection;

import sjakk.ChessBoard;
import sjakk.PieceColor;
import sjakk.Position;

public class Bishop extends LinearPiece {

    public Bishop(Position position, ChessBoard board, PieceColor color) {
        super(position, board, color, "Bishop");
    }

    @Override
    public Collection<Position> getLegalMoves() {
        return getLinearMoves(
                new Position(1, 1),
                new Position(1, -1),
                new Position(-1, 1),
                new Position(-1, -1));
    }
}
