package sjakk.pieces;

import java.util.ArrayList;
import java.util.Collection;

import sjakk.ChessBoard;
import sjakk.PieceColor;
import sjakk.Position;

public class Pawn extends Piece {

    private boolean hasMoved = false;
    private boolean hasMadeAnPassant = false;

    public Pawn(Position position, ChessBoard board, PieceColor color) {
        super(position, board, color, "Pawn");
    }

    @Override
    public boolean isValidMove(Position to) {
        if (getX() != to.getX()) {
            return isValidCaptureMove(to);
        }
        return isValidStraightMove(to);
    }

    @Override
    public Collection<Position> getLegalMoves() {
        Collection<Position> legalMoves = new ArrayList<>();

        Position[] testPostitions = {
                new Position(pos.getX(), pos.getY() + color.getDir()), // 1 forward
                new Position(pos.getX(), pos.getY() + 2 * color.getDir()), // 2 forward
                new Position(pos.getX() + 1, pos.getY() + color.getDir()), // 1 forward, 1 right
                new Position(pos.getX() - 1, pos.getY() + color.getDir()) // 1 forward, 1 left
        };

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
        if (moveIsEnPassant(to)) {
            this.hasMadeAnPassant = true;
        }
        board.move(this, to);
        this.pos = to;
        hasMoved = true;
    }

    public void setHasMadeAnPassant(boolean hasMadeAnPassant) {
        this.hasMadeAnPassant = hasMadeAnPassant;
    }

    public boolean getHasMadeAnPassant() {
        return hasMadeAnPassant;
    }

    private boolean isValidStraightMove(Position to) {
        if (pos.getX() != to.getX()) {
            return false;
        }
        if (board.getPosition(to) != null) {
            return false;
        }
        if (pos.getY() + color.getDir() == to.getY()) {
            return true;
        }
        if (!hasMoved && pos.getY() + 2 * color.getDir() == to.getY()
                && board.getPosition(new Position(getX(), getY() + color.getDir())) == null) {
            return true;
        }
        return false;
    }

    private boolean isValidCaptureMove(Position to) {
        if (Math.abs(pos.getX() - to.getX()) != 1) {
            return false;
        }
        if (pos.getY() + color.getDir() != to.getY()) {
            return false;
        }
        if (moveIsEnPassant(to)) {
            return true;
        }
        if (board.getPosition(to) == null) {
            return false;
        }
        if (board.getPosition(to).getColor() == color) {
            return false;
        }
        return true;
    }

    private boolean moveIsEnPassant(Position to) {
        if (Math.abs(pos.getX() - to.getX()) != 1) {
            return false;
        }
        if (pos.getY() + color.getDir() != to.getY()) {
            return false;
        }
        if (board.getPosition(to) != null) {
            return false;
        }
        Piece possiblyTake = board.getPosition(new Position(to.getX(), to.getY() - color.getDir()));
        if (!(possiblyTake instanceof Pawn)) {
            return false;
        }
        if (board.getLastPieceMoved() == possiblyTake && possiblyTake.getMoveCount() == 1) {
            return true;
        }
        return false;
    }
}
