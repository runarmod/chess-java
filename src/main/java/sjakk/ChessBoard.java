package sjakk;

import java.util.ArrayList;

import sjakk.pieces.Piece;

public class ChessBoard {
    private ArrayList<ArrayList<Piece>> board;

    public ChessBoard() {
        board = new ArrayList<ArrayList<Piece>>();
        for (int i = 0; i < 8; i++) {
            board.add(new ArrayList<Piece>());
            for (int j = 0; j < 8; j++) {
                board.get(i).add(null);
            }
        }
    }

    public boolean isLegalMove(Piece piece, Position position) {
        return piece.isValidMove(position);
    }

    public Piece getPosition(Position position) {
        return board.get(position.getY()).get(position.getX());
    }

    public void setPosition(Position position, Piece piece) {
        board.get(position.getY()).set(position.getX(), piece);
    }

    public void move(Piece piece, Position to) {
        setPosition(piece.getPos(), null);
        setPosition(to, piece);
    }
}
