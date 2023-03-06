package sjakk;

// import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sjakk.pieces.*;

public class LegalMovesTest {
    private ChessBoard board;

    private void checkCollectionsEqual(Collection<Position> returnedLegal, Collection<Position> actualLegal,
            String piece) {
        assertTrue(actualLegal.containsAll(returnedLegal), piece + " getLegalMoves should return ALL legal moves.");
        assertTrue(returnedLegal.containsAll(actualLegal), piece + " getLegalMoves should return ONLY legal moves.");
    }

    @BeforeEach
    public void setUp() {
        board = new ChessBoard(true);
    }

    @Test
    public void testBishopEmptyBoard() {
        Bishop bishop = new Bishop(new Position(5, 4), board, PieceColor.WHITE);

        Collection<Position> actualLegal = new ArrayList<Position>();
        // Diagonal down right
        for (int i = 0; i < 6; i++) {
            // Skip the current position
            if (i == 3)
                continue;
            actualLegal.add(new Position(2 + i, 7 - i));
        }

        // Diagonal up right
        for (int i = 0; i < 7; i++) {
            // Skip the current position
            if (i == 4)
                continue;
            actualLegal.add(new Position(1 + i, i));
        }

        checkCollectionsEqual(bishop.getLegalMoves(), actualLegal, "Bishop");
    }

    @Test
    public void testBishopBlockingPiece() {
        Bishop bishop = new Bishop(new Position(6, 4), board, PieceColor.WHITE);
        Rook blockingPiece = new Rook(new Position(4, 6), board, PieceColor.WHITE);
        Rook capturePiece = new Rook(new Position(3, 1), board, PieceColor.BLACK);

        board.setPosition(new Position(4, 6), blockingPiece);
        board.setPosition(new Position(3, 1), capturePiece);

        Collection<Position> actualLegal = new ArrayList<Position>();
        // Diagonal down right
        for (int i = 0; i < 5; i++) {
            // Skip the current position
            if (i == 3)
                continue;
            actualLegal.add(new Position(3 + i, 1 + i));
        }
        actualLegal.add(new Position(5, 5));
        actualLegal.add(new Position(7, 3));

        checkCollectionsEqual(bishop.getLegalMoves(), actualLegal, "Bishop");
    }

    @Test
    public void testKnightEmptyBoard() {
        Knight knight = new Knight(new Position(5, 4), board, PieceColor.WHITE);

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(3, 3));
        actualLegal.add(new Position(3, 5));
        actualLegal.add(new Position(4, 2));
        actualLegal.add(new Position(4, 6));
        actualLegal.add(new Position(6, 2));
        actualLegal.add(new Position(6, 6));
        actualLegal.add(new Position(7, 3));
        actualLegal.add(new Position(7, 5));

        checkCollectionsEqual(knight.getLegalMoves(), actualLegal, "Knight");
        assertFalse(knight.isValidMove(new Position(5, 4)));
        assertFalse(knight.isValidMove(new Position(5, 5)));
        assertFalse(knight.isValidMove(new Position(5, 6)));
    }

    @Test
    public void testKnightBlockingPiece() {
        Knight knight = new Knight(new Position(6, 5), board, PieceColor.WHITE);
        Piece blockingPiece = new Rook(new Position(5, 3), board, PieceColor.WHITE);
        Piece capturePiece = new Rook(new Position(7, 7), board, PieceColor.BLACK);

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(7, 7));
        actualLegal.add(new Position(5, 7));
        actualLegal.add(new Position(4, 6));
        actualLegal.add(new Position(4, 4));
        actualLegal.add(new Position(7, 3));

        checkCollectionsEqual(knight.getLegalMoves(), actualLegal, "Knight");
    }

    @Test
    public void testPawnFirstMove() {
        Pawn pawn = new Pawn(new Position(4, 1), board, PieceColor.WHITE);

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(4, 2));
        actualLegal.add(new Position(4, 3));

        checkCollectionsEqual(pawn.getLegalMoves(), actualLegal, "Pawn");
    }

    @Test
    public void testPawnNotFirstMove() {
        Pawn pawn = new Pawn(new Position(4, 2), board, PieceColor.WHITE);
        pawn.move(new Position(4, 3));

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(4, 4));

        checkCollectionsEqual(pawn.getLegalMoves(), actualLegal, "Pawn");
    }

    @Test
    public void testPawnBlockingPieceFirstMove() {
        Pawn pawn = new Pawn(new Position(4, 1), board, PieceColor.WHITE);
        Piece blockingPiece = new Rook(new Position(4, 2), board, PieceColor.WHITE);

        Collection<Position> actualLegal = new ArrayList<Position>();

        checkCollectionsEqual(pawn.getLegalMoves(), actualLegal, "Pawn");
    }

    @Test
    public void testPawnTakePiece() {
        Pawn pawn = new Pawn(new Position(4, 3), board, PieceColor.WHITE);
        Piece blockingPiece = new Rook(new Position(5, 4), board, PieceColor.BLACK);

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(5, 4));
        actualLegal.add(new Position(4, 4));
        actualLegal.add(new Position(4, 5));

        checkCollectionsEqual(pawn.getLegalMoves(), actualLegal, "Pawn");
    }

    @Test
    public void testPawnEnPassant() {
        Pawn pawn = new Pawn(new Position(4, 1), board, PieceColor.WHITE);
        Pawn tmp = new Pawn(new Position(0, 6), board, PieceColor.BLACK);
        Pawn toTake = new Pawn(new Position(5, 6), board, PieceColor.BLACK);
        pawn.move(new Position(4, 3));
        tmp.move(new Position(0, 4));
        pawn.move(new Position(4, 4));
        toTake.move(new Position(5, 4));

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(4, 5));
        actualLegal.add(new Position(5, 5));

        checkCollectionsEqual(pawn.getLegalMoves(), actualLegal, "Pawn");
    }

    @Test
    public void testRookEmptyBoard() {
        Rook rook = new Rook(new Position(0, 0), board, PieceColor.WHITE);

        Collection<Position> actualLegal = new ArrayList<Position>();
        for (int i = 1; i < 8; i++) {
            actualLegal.add(new Position(i, 0));
            actualLegal.add(new Position(0, i));
        }

        checkCollectionsEqual(rook.getLegalMoves(), actualLegal, "Rook");
    }

    @Test
    public void testRookBlockingPiece() {
        Piece blockingPiece = new Rook(new Position(3, 4), board, PieceColor.BLACK);
        board.setPosition(new Position(3, 4), blockingPiece);

        Rook rook = new Rook(new Position(4, 4), board, PieceColor.BLACK);

        Collection<Position> actualLegal = new ArrayList<Position>();
        for (int i = 0; i < 8; i++) {
            if (i == 4)
                continue;
            if (i > 4) {
                actualLegal.add(new Position(i, 4));
            }
            actualLegal.add(new Position(4, i));
        }

        checkCollectionsEqual(rook.getLegalMoves(), actualLegal, "Rook");

        assertThrows(IllegalArgumentException.class, () -> {
            rook.move(new Position(3, 4));
            rook.move(new Position(0, 0));
        });
    }

    @Test
    public void testRookCapturePiece() {
        Rook captureRook = new Rook(new Position(3, 4), board, PieceColor.WHITE);
        board.setPosition(new Position(3, 4), captureRook);

        Rook rook = new Rook(new Position(4, 4), board, PieceColor.BLACK);

        Collection<Position> actualLegal = new ArrayList<Position>();
        for (int i = 0; i < 8; i++) {
            if (i == 4)
                continue;
            if (i > 2) {
                actualLegal.add(new Position(i, 4));
            }
            actualLegal.add(new Position(4, i));
        }

        checkCollectionsEqual(rook.getLegalMoves(), actualLegal, "Rook");
    }

    @Test
    public void testQueenEmptyBoard() {
        Queen queen = new Queen(new Position(4, 4), board, PieceColor.WHITE);

        Collection<Position> actualLegal = new ArrayList<Position>();
        for (int i = 0; i < 8; i++) {
            if (i == 4)
                continue;

            // Horizontal
            actualLegal.add(new Position(i, 4));

            // Vertical
            actualLegal.add(new Position(4, i));

            // Diagonal up right
            actualLegal.add(new Position(i, i));

            // Diagonal down right
            if (i == 0)
                continue;
            actualLegal.add(new Position(i, 8 - i));
        }

        checkCollectionsEqual(queen.getLegalMoves(), actualLegal, "Queen");
    }

    @Test
    public void testQueenBlockingPiece() {
        Queen queen = new Queen(new Position(4, 4), board, PieceColor.WHITE);
        Piece blockingPiece = new Rook(new Position(4, 6), board, PieceColor.WHITE);
        Piece capturePiece = new Rook(new Position(2, 2), board, PieceColor.BLACK);

        Collection<Position> actualLegal = new ArrayList<Position>();

        // Horizontal
        for (int i = 0; i < 8; i++) {
            if (i == 4)
                continue;
            actualLegal.add(new Position(i, 4));
        }

        // Vertical
        for (int i = 5; i >= 0; i--) {
            if (i == 4)
                continue;
            actualLegal.add(new Position(4, i));
        }

        // Down right
        for (int i = 1; i < 8; i++) {
            if (i == 4)
                continue;
            actualLegal.add(new Position(8 - i, i));
        }

        // Up right
        for (int i = 2; i < 8; i++) {
            if (i == 4)
                continue;
            actualLegal.add(new Position(i, i));
        }

        checkCollectionsEqual(queen.getLegalMoves(), actualLegal, "Queen");

    }
}
