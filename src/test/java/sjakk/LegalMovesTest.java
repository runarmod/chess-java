package sjakk;

// import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        assertTrue(actualLegal.containsAll(returnedLegal), "Pawn getLegalMoves should return ALL legal moves.");
        assertTrue(returnedLegal.containsAll(actualLegal), "Pawn getLegalMoves should return ONLY legal moves.");
    }

    @BeforeEach
    public void setUp() {
        board = new ChessBoard();
    }

    @Test
    public void testRookEmptyBoard() {
        Rook rook = new Rook(new Position(0, 0), board, Color.WHITE);

        Collection<Position> actualLegal = new ArrayList<Position>();
        for (int i = 1; i < 8; i++) {
            actualLegal.add(new Position(i, 0));
            actualLegal.add(new Position(0, i));
        }

        checkCollectionsEqual(rook.getLegalMoves(), actualLegal, "Rook");
    }

    @Test
    public void testRookBlockingPiece() {
        Piece blockingPiece = new Rook(new Position(3, 4), board, Color.BLACK);
        board.setPosition(new Position(3, 4), blockingPiece);

        Rook rook = new Rook(new Position(4, 4), board, Color.BLACK);

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
        Rook captureRook = new Rook(new Position(3, 4), board, Color.WHITE);
        board.setPosition(new Position(3, 4), captureRook);
        
        Rook rook = new Rook(new Position(4, 4), board, Color.BLACK);

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
    public void testBishopEmptyBoard() {
        Bishop bishop = new Bishop(new Position(5, 4), board, Color.WHITE);

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
        Bishop bishop = new Bishop(new Position(6, 4), board, Color.WHITE);
        Rook blockingPiece = new Rook(new Position(4, 6), board, Color.WHITE);
        Rook capturePiece = new Rook(new Position(3, 1), board, Color.BLACK);

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
    public void testPawnFirstMove() {
        Pawn pawn = new Pawn(new Position(4, 1), board, Color.WHITE);

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(4, 2));
        actualLegal.add(new Position(4, 3));

        checkCollectionsEqual(pawn.getLegalMoves(), actualLegal, "Pawn");
    }

    @Test
    public void testPawnNotFirstMove() {
        Pawn pawn = new Pawn(new Position(4, 2), board, Color.WHITE);
        pawn.move(new Position(4, 3));

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(4, 4));

        checkCollectionsEqual(pawn.getLegalMoves(), actualLegal, "Pawn");
    }

    @Test
    public void testPawnEnPassant() {
        Pawn pawn = new Pawn(new Position(4, 1), board, Color.WHITE);
        Pawn tmp = new Pawn(new Position(0, 6), board, Color.BLACK);
        Pawn toTake = new Pawn(new Position(5, 6), board, Color.BLACK);
        pawn.move(new Position(4, 3));
        tmp.move(new Position(0, 4));
        pawn.move(new Position(4, 4));
        toTake.move(new Position(5, 4));

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(4, 5));
        actualLegal.add(new Position(5, 5));

        checkCollectionsEqual(pawn.getLegalMoves(), actualLegal, "Pawn");
    }

}
