package sjakk.pieces;

import java.util.Collection;

import sjakk.ChessBoard;
import sjakk.Color;
import sjakk.Position;

public class Bishop extends LinearPiece {

    public Bishop(Position position, ChessBoard board, Color color) {
        super(position, board, color);
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
