package sjakk;

import java.util.ArrayList;
import java.util.Iterator;

import sjakk.pieces.*;

public class ChessBoard implements Iterable<Piece> {
    private ArrayList<ArrayList<Piece>> board;
    private ArrayList<String> moves = new ArrayList<String>();

    public ChessBoard() {
        board = new ArrayList<ArrayList<Piece>>();
        for (int i = 0; i < 8; i++) {
            board.add(new ArrayList<Piece>());
            for (int j = 0; j < 8; j++) {
                board.get(i).add(null);
            }
        }
        initializeDefaultSetup();
    }

    private void initializeDefaultSetup() {
        for (int i = 0; i < 8; i++) {
            new Pawn(new Position(i, 1), this, PieceColor.WHITE);
            new Pawn(new Position(i, 6), this, PieceColor.BLACK);
        }

        new Rook(new Position(0, 0), this, PieceColor.WHITE);
        new Rook(new Position(7, 0), this, PieceColor.WHITE);

        new Rook(new Position(0, 7), this, PieceColor.BLACK);
        new Rook(new Position(7, 7), this, PieceColor.BLACK);

        new Knight(new Position(1, 0), this, PieceColor.WHITE);
        new Knight(new Position(6, 0), this, PieceColor.WHITE);

        new Knight(new Position(1, 7), this, PieceColor.BLACK);
        new Knight(new Position(6, 7), this, PieceColor.BLACK);

        new Bishop(new Position(2, 0), this, PieceColor.WHITE);
        new Bishop(new Position(5, 0), this, PieceColor.WHITE);

        new Bishop(new Position(2, 7), this, PieceColor.BLACK);
        new Bishop(new Position(5, 7), this, PieceColor.BLACK);

        new Queen(new Position(3, 0), this, PieceColor.WHITE);

        new Queen(new Position(3, 7), this, PieceColor.BLACK);

        new King(new Position(4, 0), this, PieceColor.WHITE);

        new King(new Position(4, 7), this, PieceColor.BLACK);

    }

    public boolean isValidMove(Piece piece, Position position) {
        return piece.isValidMove(position);
    }

    public Piece getPosition(Position position) {
        return board.get(position.getY()).get(position.getX());
    }

    public void setPosition(Position position, Piece piece) {
        if (getPosition(position) == piece)
            return;
        board.get(position.getY()).set(position.getX(), piece);
    }

    public void move(Piece piece, Position to) {
        moves.add(piece.getPos().toString() + to.toString());
        setPosition(piece.getPos(), null);
        setPosition(to, piece);
        piece.setPos(to);
    }

    public String getMoves() {
        String movesString = "    WHITE | BLACK\n";
        for (int i = 0; i < moves.size(); i++) {
            if (i % 2 == 0) {
                int moveNr = (i / 2 + 1);
                String moveNrString = String.format("%2s", moveNr);
                movesString += moveNrString + ". ";
                movesString += moves.get(i);
            } else {
                movesString += "  | " + moves.get(i) + "\n";
            }
        }
        return movesString;
    }

    @Override
    public Iterator<Piece> iterator() {
        return new ChessBoardIterator(this);
    }
}
