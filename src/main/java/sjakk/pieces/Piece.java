package sjakk.pieces;

import java.util.Collection;

import sjakk.ChessBoard;
import sjakk.Color;
import sjakk.Position;

public abstract class Piece {
    protected Position pos;
    protected ChessBoard board;
    protected Color color;

    /**
     * Creates a new piece at the given position on the given board. The pice is
     * automatically places on the board if its not already in position.
     * 
     * @param position the position of the piece
     * @param board    the board the piece is on
     * @param color    the color of the piece
     */
    public Piece(Position position, ChessBoard board, Color color) {
        this.pos = position;
        this.board = board;
        if (board.getPosition(position) != this) {
            board.setPosition(position, this);
        }
        this.color = color;
    }

    /**
     * Returns a collection of all legal moves for this piece.
     * 
     * @return a collection of all legal moves for this piece
     */
    public abstract Collection<Position> getLegalMoves();

    /**
     * Returns the x coordinate of the piece.
     * 
     * @return the x coordinate of the piece
     */
    public int getX() {
        return pos.getX();
    }

    /**
     * Returns the y coordinate of the piece.
     * 
     * @return the y coordinate of the piece
     */
    public int getY() {
        return pos.getY();
    }

    /**
     * Returns whether or not the given position is a legal move for this piece.
     * 
     * @param to the position to check
     * @return true if the given position is a current legal move for this piece
     */
    public boolean isValidMove(Position to) {
        return getLegalMoves().contains(to);
    }

    /**
     * Returns the color of the piece.
     * 
     * @return the color of the piece
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the position of the piece.
     * 
     * @return the position of the piece
     */
    public Position getPos() {
        return new Position(pos);
    }

    /**
     * Sets the position of the piece.
     * 
     * @param pos the new position of the piece
     */
    public void setPos(Position pos) {
        this.pos = pos;
    }

    /**
     * Moves the piece to the given position on the board.
     * 
     * @throws IllegalArgumentException if the move is not legal
     * @param to the position to move to
     */
    public void move(Position to) {
        if (!isValidMove(to)) {
            throw new IllegalArgumentException("Illegal move");
        }
        board.move(this, to);
    }

}
