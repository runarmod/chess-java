package sjakk.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sjakk.ChessBoard;
import sjakk.Color;
import sjakk.Position;

public class Rook extends Piece {

    public Rook(Position position, ChessBoard board, Color color) {
        super(position, board, color);
    }

    @Override
    public Collection<Position> getLegalMoves() {
        Collection<Position> legalMoves = new ArrayList<Position>();

        for (Position tmpDir : List.of(
                new Position(0, 1),
                new Position(0, -1),
                new Position(1, 0),
                new Position(-1, 0))) {

            Position currentPosition = new Position(pos).add(tmpDir);
            while (currentPosition.insideBoard()) {
                if (board.getPosition(currentPosition) == null) {
                    legalMoves.add(currentPosition);
                } else {
                    if (board.getPosition(currentPosition).getColor() != color) {
                        legalMoves.add(currentPosition);
                    }
                    break;
                }
                currentPosition = currentPosition.add(tmpDir);
            }
        }
        return legalMoves;
    }

    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        Rook rook = new Rook(new Position(0, 0), board, Color.WHITE);
        System.out.println(rook.getLegalMoves());

        Rook rook2 = new Rook(new Position(4, 4), board, Color.BLACK);
        System.out.println(rook2.getLegalMoves());
    }
}
