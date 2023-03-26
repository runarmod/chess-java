package sjakk.pieces;

import java.util.List;

import sjakk.ChessBoard;
import sjakk.Player;
import sjakk.Position;

public class Queen extends LinearPiece {

    public Queen(Position position, ChessBoard board, Player owner) {
        super(position, board, owner, "Queen");

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
