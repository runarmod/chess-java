package sjakk.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sjakk.ChessBoard;
import sjakk.Color;
import sjakk.Position;

public class Rook extends LinearPiece {

    public Rook(Position position, ChessBoard board, Color color) {
        super(position, board, color);
    }

    @Override
    public Collection<Position> getLegalMoves() {
        Collection<Position> defaultMoves = getLinearMoves(
                new Position(0, 1),
                new Position(0, -1),
                new Position(1, 0),
                new Position(-1, 0));
        return defaultMoves;
    }

    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        Rook rook = new Rook(new Position(0, 0), board, Color.WHITE);
        System.out.println(rook.getLegalMoves());

        Rook rook2 = new Rook(new Position(4, 4), board, Color.BLACK);
        System.out.println(rook2.getLegalMoves());
    }
}
