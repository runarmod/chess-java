package sjakk.pieces;

import java.util.List;

import sjakk.ChessBoard;
import sjakk.PieceColor;
import sjakk.Position;

public class Rook extends LinearPiece {
    public Rook(Position position, ChessBoard board, PieceColor color) {
        super(position, board, color, "Rook");

        super.setDirections(List.of(
                new Position(0, 1), // UP
                new Position(0, -1), // DOWN
                new Position(1, 0), // RIGHT
                new Position(-1, 0) // LEFT
        ));
    }
}
