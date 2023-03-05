package sjakk.pieces;

import java.util.ArrayList;
import java.util.Collection;

import sjakk.ChessBoard;
import sjakk.PieceColor;
import sjakk.Position;

public class Knight extends Piece {

    public Knight(Position position, ChessBoard board, PieceColor color) {
        super(position, board, color, "Knight");
    }

    @Override
    public boolean isValidMove(Position to) {
        if (!to.insideBoard())
            return false;
        if (board.getPosition(to) == null)
            return true;
        if (board.getPosition(to).getColor() != color)
            return true;
        return false;
    }

    @Override
    public Collection<Position> getLegalMoves() {
        Collection<Position> legalMoves = new ArrayList<Position>();

        int[] dX = { 2, 1, -1, -2, -2, -1, 1, 2 };
        int[] dY = { 1, 2, 2, 1, -1, -2, -2, -1 };
        for (int i = 0; i < dX.length; i++) {
            Position to = new Position(getX() + dX[i], getY() + dY[i]);
            if (isValidMove(to))
                legalMoves.add(to);
        }

        return legalMoves;
    }

}
