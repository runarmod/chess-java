package sjakk.pieces;

import java.util.Collection;

import sjakk.ChessBoard;
import sjakk.Color;
import sjakk.Position;

public abstract class Piece {
    protected Position pos;
    protected ChessBoard board;
    protected Color color;

    public Piece(Position position, ChessBoard board, Color color) {
        this.pos = position;
        this.board = board;
        this.color = color;
    }

    public abstract Collection<Position> getLegalMoves();

    public int getX() {
        return pos.getX();
    }

    public int getY() {
        return pos.getY();
    }

    public boolean isValidMove(Position to) {
        return getLegalMoves().contains(to);
    }

    public Color getColor() {
        return color;
    }

    public Position getPos() {
        return new Position(pos);
    }

    public void move(Position to) {
        if (!isValidMove(to)) {
            throw new IllegalArgumentException("Illegal move");
        }
        board.move(this, to);
        this.pos = to;
    }

}
