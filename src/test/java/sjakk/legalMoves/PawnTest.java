package sjakk.legalMoves;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import sjakk.Position;
import sjakk.pieces.*;
import sjakk.utils.FENParser;

public class PawnTest extends PieceTest {
    @Test
    public void testPawnFirstMove() {
        Pawn pawn = new Pawn(new Position(4, 1), board, whitePlayer);

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(4, 2));
        actualLegal.add(new Position(4, 3));

        checkCollectionsEqual(pawn.getLegalMoves(), actualLegal, "Pawn");
    }

    @Test
    public void testPawnNotFirstMove() {
        assertDoesNotThrow(() -> board = FENParser.getBoardFromFEN("8/8/8/8/8/4P3/8/k1K5 b - - 0 1"),
                "Should not throw on legal FEN-string.");

        Piece pawn = board.getPosition(new Position(4, 2));
        assertDoesNotThrow(() -> pawn.move(new Position(4, 3)), "Move is legal, so it should not be thrown.");

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(4, 4));

        checkCollectionsEqual(pawn.getLegalMoves(), actualLegal, "Pawn");
    }

    @Test
    public void testPawnBlockingPieceFirstMove() {
        assertDoesNotThrow(() -> board = FENParser.getBoardFromFEN("8/8/8/8/8/4R3/4P3/k1K5 w - - 0 1"),
                "Should not throw on legal FEN-string.");
        Piece pawn = board.getPosition(new Position(4, 1));

        Collection<Position> actualLegal = new ArrayList<Position>();

        checkCollectionsEqual(pawn.getLegalMoves(), actualLegal, "Pawn");
    }

    @Test
    public void testPawnTakePiece() {
        assertDoesNotThrow(() -> board = FENParser.getBoardFromFEN("8/8/8/5r2/4P3/8/8/K1k5 w - - 0 1"),
                "Should not throw on legal FEN-string.");
        Piece pawn = new Pawn(new Position(4, 3), board, whitePlayer);

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(5, 4));
        actualLegal.add(new Position(4, 4));

        checkCollectionsEqual(pawn.getLegalMoves(), actualLegal, "Pawn");
    }

    @Test
    public void testPawnEnPassant() {
        Pawn pawn = new Pawn(new Position(4, 1), board, whitePlayer);
        Pawn tmp = new Pawn(new Position(0, 6), board, blackPlayer);
        Pawn toTake = new Pawn(new Position(5, 6), board, blackPlayer);
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
