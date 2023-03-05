package sjakk.pieces;

import java.util.Collection;

import sjakk.ChessBoard;
import sjakk.PieceColor;
import sjakk.Position;

public class King extends Piece {

    public King(Position position, ChessBoard board, PieceColor color) {
        super(position, board, color, "King");
    }

    @Override
    public Collection<Position> getLegalMoves() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLegalMoves'");
    }

}
