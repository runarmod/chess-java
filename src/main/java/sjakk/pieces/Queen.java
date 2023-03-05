package sjakk.pieces;

import java.util.Collection;

import sjakk.ChessBoard;
import sjakk.PieceColor;
import sjakk.Position;

public class Queen extends LinearPiece {

    public Queen(Position position, ChessBoard board, PieceColor color) {
        super(position, board, color, "Queen");
    }

    @Override
    public Collection<Position> getLegalMoves() {
        return getLinearMoves(
                new Position(0, 1),
                new Position(0, -1),
                new Position(1, 0),
                new Position(-1, 0),
                new Position(1, 1),
                new Position(1, -1),
                new Position(-1, 1),
                new Position(-1, -1));
    }

}
