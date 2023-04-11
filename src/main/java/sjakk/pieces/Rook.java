package sjakk.pieces;

import java.util.List;

import sjakk.ChessBoard;
import sjakk.Player;
import sjakk.Position;

/**
 * A Rook is a {@link Piece} that can move in a straight line horizontally and
 * vertically. It uses the {@link LinearPiece} class.
 * 
 * @see LinearPiece
 * @see Piece
 */
public class Rook extends LinearPiece {

    /**
     * Creates a new Rook with the given position, board, and owner.
     * 
     * @param position The position of the Rook.
     * @param board    The board the Rook is on.
     * @param owner    The owner of the Rook.
     */
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
