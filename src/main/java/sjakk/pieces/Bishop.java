package sjakk.pieces;

import java.util.List;

import sjakk.ChessBoard;
import sjakk.Player;
import sjakk.Position;

public class Bishop extends LinearPiece {
    public Bishop(Position position, ChessBoard board, Player owner) {
        super(position, board, owner, "Bishop");

        super.setDirections(List.of(
                new Position(1, 1), // UP-RIGHT
                new Position(1, -1), // DOWN-RIGHT
                new Position(-1, 1), // UP-LEFT
                new Position(-1, -1) // DOWN-LEFT
        ));
    }
}
