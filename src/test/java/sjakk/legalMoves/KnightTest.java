package sjakk.legalMoves;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import sjakk.Position;
import sjakk.pieces.*;
import sjakk.utils.FENParser;

public class KnightTest extends PieceTest {

    @Test
    public void testKnightEmptyBoard() {
        Knight knight = new Knight(new Position(5, 4), board, whitePlayer);

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
        assertDoesNotThrow(() -> board = FENParser.getBoardFromFEN("7r/8/6N1/8/5R2/8/8/K1k5 w - - 0 1"),
                "Should not throw on legal FEN-string.");
        Piece knight = board.getPosition(new Position(6, 5));

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(7, 7));
        actualLegal.add(new Position(5, 7));
        actualLegal.add(new Position(4, 6));
        actualLegal.add(new Position(4, 4));
        actualLegal.add(new Position(7, 3));

        checkCollectionsEqual(knight.getLegalMoves(), actualLegal, "Knight");
    }
}
