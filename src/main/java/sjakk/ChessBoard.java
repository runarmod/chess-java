package sjakk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javafx.scene.image.Image;
import sjakk.pieces.*;

public class ChessBoard implements Iterable<Piece> {
    private static Image allPiecesImg;
    private static Map<String, Integer> imgColIdx = Map.of("King", 0, "Queen", 1, "Bishop", 2, "Knight", 3, "Rook", 4,
            "Pawn", 5);

    public static Image getAllPiecesImg() {
        return allPiecesImg;
    }

    public static int getPieceImageIndex(String pieceType) {
        return imgColIdx.get(pieceType);
    }

    private ArrayList<ArrayList<Piece>> board;

    private ArrayList<String> moves = new ArrayList<String>();

    private Piece selectedPiece;

    private PieceColor turn = PieceColor.WHITE;

    private boolean test = false;

    public ChessBoard() {
        this(false);
    }

    public ChessBoard(boolean test) {
        this.test = test;

        board = new ArrayList<ArrayList<Piece>>();
        for (int i = 0; i < 8; i++) {
            board.add(new ArrayList<Piece>());
            for (int j = 0; j < 8; j++) {
                board.get(i).add(null);
            }
        }

        if (!test)
            allPiecesImg = new Image(getClass().getResource("pieces.png").toString());

    }

    public boolean isTest() {
        return test;
    }

    /**
     * Handle a click on a position on the board. Possible results are:
     * <ol>
     * <li>
     * No piece is selected. Select the piece at the position if it exists.
     * </li>
     * <li>
     * A piece is selected. If the new position is a valid move for the selected
     * piece, move the piece.
     * </li>
     * <li>
     * A piece is selected. If the new position is not a valid move for the
     * selected piece, deselect the piece.
     * </li>
     * </ol>
     * 
     * @param position The position that was clicked.
     */
    public void positionPressed(Position position) {
        if (selectedPiece == null) {
            selectedPiece = getPosition(position);
            if (selectedPiece != null && selectedPiece.getColor() != turn) {
                selectedPiece = null;
            }
            return;
        }

        if (turn != selectedPiece.getColor())
            return;

        try {
            selectedPiece.move(position);
        } catch (Exception e) {
            System.out.println("Invalid move");
            selectedPiece = null;
            return;
        }
        selectedPiece = null;
        turn = (turn == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE);
    }

    /**
     * Sets up the board with the default chess setup.
     * TODO: Do this using PGN or FEN.
     */
    public void initializeDefaultSetup() {
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
