package sjakk.pieces;

import java.util.List;

import sjakk.ChessBoard;
import sjakk.PieceColor;
import sjakk.Position;

public class Queen extends LinearPiece {

    public Queen(Position position, ChessBoard board, PieceColor color) {
        super(position, board, color, "Queen");

        super.setDirections(List.of(
                new Position(0, 1), // UP
                new Position(0, -1), // DOWN
                new Position(1, 0), // RIGHT
                new Position(-1, 0), // LEFT
                new Position(1, 1), // UP-RIGHT
                new Position(1, -1), // DOWN-RIGHT
                new Position(-1, 1), // UP-LEFT
                new Position(-1, -1) // DOWN-LEFT
        ));
    }
}
