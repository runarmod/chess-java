package sjakk.pieces;

import java.util.ArrayList;
import java.util.Collection;

import sjakk.ChessBoard;
import sjakk.PieceColor;
import sjakk.Position;

public class Pawn extends Piece {

    private boolean hasMoved = false;

    public Pawn(Position position, ChessBoard board, PieceColor color) {
        super(position, board, color, "Pawn");
    }

    @Override
    public boolean isValidMove(Position to) {
        // TODO: en passant
        if (pos.getX() != to.getX()) {
            return false;
        }
        if (board.getPosition(to) != null) {
            return false;
        }
        if (pos.getY() + color.getDir() == to.getY()) {
            return true;
        }
        if (!hasMoved && pos.getY() + 2 * color.getDir() == to.getY() && board.getPosition(to) == null) {
            return true;
        }
        return false;
    }

    @Override
    public Collection<Position> getLegalMoves() {
        Collection<Position> legalMoves = new ArrayList<>();

        Position[] testPostitions = { new Position(pos.getX(), pos.getY() + color.getDir()),
                new Position(pos.getX(), pos.getY() + 2 * color.getDir()) };

        for (Position p : testPostitions) {
            if (isValidMove(p)) {
                legalMoves.add(p);
            }
        }

        return legalMoves;
    }

    @Override
    public void move(Position to) {
        if (!isValidMove(to)) {
            throw new IllegalArgumentException("Illegal move");
        }
        board.move(this, to);
        this.pos = to;
        hasMoved = true;
    }
}
