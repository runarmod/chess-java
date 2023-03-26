package sjakk.pieces;

import java.util.List;

import sjakk.ChessBoard;
import sjakk.Player;
import sjakk.Position;

public class Rook extends LinearPiece {
    public Rook(Position position, ChessBoard board, Player owner) {
        super(position, board, owner, "Rook");

        super.setDirections(List.of(
                new Position(0, 1), // UP
                new Position(0, -1), // DOWN
                new Position(1, 0), // RIGHT
                new Position(-1, 0) // LEFT
        ));
    }
}
