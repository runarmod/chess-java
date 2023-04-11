package sjakk.legalMoves;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import sjakk.Position;
import sjakk.pieces.*;
import sjakk.utils.FENParser;

public class BishopTest extends PieceTest {
    @Test
    public void testBishopEmptyBoard() {
        Bishop bishop = new Bishop(new Position(5, 4), board, whitePlayer);

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
        assertDoesNotThrow(() -> board = FENParser.getBoardFromFEN("8/4R3/8/6B1/8/8/3r4/K1k5 w - - 0 1"),
                "Should not throw on legal FEN-string.");
        Piece bishop = board.getPosition(new Position(6, 4));

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
}
