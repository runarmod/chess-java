package sjakk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sjakk.pieces.*;

public class LegalMovesTest {
    private ChessBoard board;

    @BeforeEach
    public void setUp() {
        board = new ChessBoard();
    }

    @Test
    public void testBlockingPieces() {
        // Rook
        board.setPosition(new Position(3, 4), new Rook(new Position(3, 4), board, Color.BLACK));
        Rook rook2 = new Rook(new Position(4, 4), board, Color.BLACK);
        Collection<Position> returnedLegal2 = rook2.getLegalMoves();
        Collection<Position> actualLegal2 = new ArrayList<Position>();
        for (int i = 0; i < 8; i++) {
            if (i == 4)
                continue;
            if (i > 4) {
                actualLegal2.add(new Position(i, 4));
            }
            actualLegal2.add(new Position(4, i));
        }
        assertTrue(actualLegal2.containsAll(returnedLegal2), "Rook getLegalMoves should return ALL legal moves.");
        assertTrue(returnedLegal2.containsAll(actualLegal2), "Rook getLegalMoves should return ONLY legal moves.");

        // TODO: Add tests for Bishop, Queen, King, Knight, Pawn
    }

    @Test
    public void testRookEmptyBoard() {
        Rook rook = new Rook(new Position(0, 0), board, Color.WHITE);
        Collection<Position> returnedLegal = rook.getLegalMoves();
        Collection<Position> actualLegal = new ArrayList<Position>();
        for (int i = 1; i < 8; i++) {
            actualLegal.add(new Position(i, 0));
            actualLegal.add(new Position(0, i));
        }
        assertTrue(actualLegal.containsAll(returnedLegal), "Rook getLegalMoves should return ALL legal moves.");
        assertTrue(returnedLegal.containsAll(actualLegal), "Rook getLegalMoves should return ONLY legal moves.");

    }

    @Test
    public void testRookCapturePiece() {
        board.setPosition(new Position(3, 4), new Rook(new Position(3, 4), board, Color.WHITE));
        Rook rook2 = new Rook(new Position(4, 4), board, Color.BLACK);
        Collection<Position> returnedLegal2 = rook2.getLegalMoves();
        Collection<Position> actualLegal2 = new ArrayList<Position>();
        for (int i = 0; i < 8; i++) {
            if (i == 4)
                continue;
            if (i > 2) {
                actualLegal2.add(new Position(i, 4));
            }
            actualLegal2.add(new Position(4, i));
        }
        assertTrue(actualLegal2.containsAll(returnedLegal2), "Rook getLegalMoves should return ALL legal moves.");
        assertTrue(returnedLegal2.containsAll(actualLegal2), "Rook getLegalMoves should return ONLY legal moves.");
    }

    @Test
    public void testPawnFirstMove() {
        Pawn pawn = new Pawn(new Position(4, 1), board, Color.WHITE);
        Collection<Position> returnedLegal = pawn.getLegalMoves();
        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(4, 2));
        actualLegal.add(new Position(4, 3));
        assertTrue(actualLegal.containsAll(returnedLegal), "Pawn getLegalMoves should return ALL legal moves.");
        assertTrue(returnedLegal.containsAll(actualLegal), "Pawn getLegalMoves should return ONLY legal moves.");
    }

    @Test
    public void testPawnNotFirstMove() {
        Pawn pawn = new Pawn(new Position(4, 2), board, Color.WHITE);
        pawn.move(new Position(4, 3));
        Collection<Position> returnedLegal = pawn.getLegalMoves();
        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(4, 4));
        assertTrue(actualLegal.containsAll(returnedLegal), "Pawn getLegalMoves should return ALL legal moves.");
        assertTrue(returnedLegal.containsAll(actualLegal), "Pawn getLegalMoves should return ONLY legal moves.");
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

        Collection<Position> returnedLegal = pawn.getLegalMoves();
        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(4, 5));
        actualLegal.add(new Position(5, 5));
        assertTrue(actualLegal.containsAll(returnedLegal), "Pawn getLegalMoves should return ALL legal moves.");
        assertTrue(returnedLegal.containsAll(actualLegal), "Pawn getLegalMoves should return ONLY legal moves.");
    }

}
