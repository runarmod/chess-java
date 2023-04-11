package sjakk.pieces;

import java.util.List;

import sjakk.ChessBoard;
import sjakk.Player;
import sjakk.Position;

/**
 * A Queen is a {@link Piece} that can move in a straight line in any direction.
 * It uses the {@link LinearPiece} class with all directions available.
 * 
 * @see LinearPiece
 * @see Piece
 */
public class Queen extends LinearPiece {

    /**
     * Creates a new Queen with the given position, board, and owner.
     * 
     * @param position The position of the Queen.
     * @param board    The board the Queen is on.
     * @param owner    The owner of the Queen.
     */
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
