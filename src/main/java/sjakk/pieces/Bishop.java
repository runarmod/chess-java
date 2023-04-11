package sjakk.pieces;

import java.util.List;

import sjakk.ChessBoard;
import sjakk.Player;
import sjakk.Position;

/**
 * A Bishop is a piece that can move in a straight line in any diagonal. It uses
 * the {@link LinearPiece} class.
 * 
 * @see LinearPiece
 */
public class Bishop extends LinearPiece {
    /**
     * Creates a new Bishop.
     * 
     * @param position The position of the Bishop.
     * @param board    The board the Bishop is on.
     * @param owner    The owner of the Bishop.
     */
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
