package sjakk.pieces;

import java.util.Collection;

import sjakk.ChessBoard;
import sjakk.Color;
import sjakk.Position;

public class Queen extends LinearPiece {

    public Queen(Position position, ChessBoard board, Color color) {
        super(position, board, color);
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
