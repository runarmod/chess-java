package sjakk.pieces;

import java.util.Collection;

import sjakk.ChessBoard;
import sjakk.PieceColor;
import sjakk.Position;

public class Rook extends LinearPiece {

    public Rook(Position position, ChessBoard board, PieceColor color) {
        super(position, board, color, "Rook");
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
        Rook rook = new Rook(new Position(0, 0), board, PieceColor.WHITE);
        System.out.println(rook.getLegalMoves());

        Rook rook2 = new Rook(new Position(4, 4), board, PieceColor.BLACK);
        System.out.println(rook2.getLegalMoves());
    }
}
