package sjakk.legalMoves;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import sjakk.Position;
import sjakk.pieces.*;
import sjakk.utils.FENParser;

public class KingTest extends PieceTest {
    @Test
    public void testKingEmptyBoard() {
        King king = new King(new Position(5, 4), board, whitePlayer);
        board.disableCastling();

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(4, 3));
        actualLegal.add(new Position(4, 4));
        actualLegal.add(new Position(4, 5));
        actualLegal.add(new Position(5, 3));
        actualLegal.add(new Position(5, 5));
        actualLegal.add(new Position(6, 3));
        actualLegal.add(new Position(6, 4));
        actualLegal.add(new Position(6, 5));

        checkCollectionsEqual(king.getLegalMoves(), actualLegal, "King");
    }

    @Test
    public void testKing() {
        assertDoesNotThrow(() -> board = FENParser.getBoardFromFEN("8/5Nn1/6K1/8/8/4n3/8/8 w KQkq - 0 1"),
                "Should not throw on legal FEN-string.");
        Piece king = board.getPosition(new Position(6, 5));

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(5, 5));
        actualLegal.add(new Position(6, 4));
        actualLegal.add(new Position(6, 6));
        actualLegal.add(new Position(7, 5));
        actualLegal.add(new Position(7, 6));

        checkCollectionsEqual(king.getLegalMoves(), actualLegal, "King");
    }

    @Test
    public void testKingCastling() {
        assertDoesNotThrow(() -> board = FENParser.getBoardFromFEN("k7/8/8/8/8/8/8/R3K2R w KQ - 0 1"),
                "Should not throw on legal FEN-string.");

        Piece king = board.getPosition(new Position(4, 0));
        Piece rookQueenSide = board.getPosition(new Position(0, 0));

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(2, 0));
        actualLegal.add(new Position(3, 0));
        actualLegal.add(new Position(5, 0));
        actualLegal.add(new Position(6, 0));

        actualLegal.add(new Position(3, 1));
        actualLegal.add(new Position(4, 1));
        actualLegal.add(new Position(5, 1));

        checkCollectionsEqual(king.getLegalMoves(), actualLegal, "King");
        king.move(new Position(2, 0));

        assertEquals(board.getPosition(new Position(0, 0)), null);
        assertEquals(board.getPosition(new Position(3, 0)), rookQueenSide);
    }

    @Test
    public void testKingCastlingPassThroughCheck() {
        assertDoesNotThrow(() -> board = FENParser.getBoardFromFEN("3r3k/8/8/8/8/8/8/R3K2R w KQ - 0 1"),
                "Should not throw on legal FEN-string.");
        Piece king = board.getPosition(new Position(4, 0));

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(5, 0));
        actualLegal.add(new Position(6, 0));

        actualLegal.add(new Position(4, 1));
        actualLegal.add(new Position(5, 1));

        checkCollectionsEqual(king.getLegalMoves(), actualLegal, "King");
    }
}
