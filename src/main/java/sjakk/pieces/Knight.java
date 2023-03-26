package sjakk.pieces;

import java.util.ArrayList;
import java.util.Collection;

import sjakk.ChessBoard;
import sjakk.Player;
import sjakk.Position;

public class Knight extends Piece {

    public Knight(Position position, ChessBoard board, Player owner) {
        super(position, board, owner, "Knight");
    }

    @Override
    public boolean isValidMove(Position to) {
        return getLegalMoves().contains(to);
    }

    private boolean validOnBoard(Position to) {
        if (!to.insideBoard())
            return false;
        if (board.getPosition(to) == null)
            return true;
        if (board.getPosition(to).getOwner() != owner)
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
            if (messesUpcheck(to)) {
                continue;
            }
            if (validOnBoard(to))
                legalMoves.add(to);
        }

        return legalMoves;
    }

    @Override
    protected boolean threatening(Position position) {
        int[] dX = { 2, 1, -1, -2, -2, -1, 1, 2 };
        int[] dY = { 1, 2, 2, 1, -1, -2, -2, -1 };

        for (int i = 0; i < dX.length; i++) {
            Position to = new Position(getX() + dX[i], getY() + dY[i]);
            if (to.equals(position))
                return true;
        }
        return false;
    }
}
