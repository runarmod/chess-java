package sjakk;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import sjakk.utils.FENParser;

public class ChessBoardMoveTest {
    private ChessBoard board;
    private static final String defaultFENString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Test
    public void testFirstPawnMove() {
        assertDoesNotThrow(
                () -> board = FENParser.getBoardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"),
                "Should not throw on default start.");

        assertEquals(board.getFEN(), defaultFENString, "Should be the same FEN-string.");

        assertFalse(board.getGameFinished(), "Game is not finished when it just started.");

        board.move(board.getPosition(new Position(0, 1)), new Position(0, 3));

        assertEquals("rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq - 0 1", board.getFEN(),
                "Game position is not correct after move.");
    }

    @Test
    public void testCastlingMove() {
        assertDoesNotThrow(
                () -> board = FENParser.getBoardFromFEN("7k/8/8/8/8/8/8/R3K3 w Q - 0 1"),
                "Should not throw on this FEN-string.");

        assertFalse(board.getGameFinished(), "Game is not finished.");

        board.getPosition(new Position(4, 0)).move(new Position(2, 0));
        System.out.println(board);
        assertEquals("7k/8/8/8/8/8/8/2KR4 b - - 1 1", board.getFEN(),
                "Game position is not correct after castling.");
    }

    @Test
    public void testEnPassant() {
        assertDoesNotThrow(() -> board = FENParser.getBoardFromFEN("7k/4p3/8/3P4/8/8/8/7K b - - 0 1"),
                "Should not throw on this FEN-string.");

        assertFalse(board.getGameFinished(), "Game is not finished.");

        // Black pawn moves two steps forward
        board.getPosition(new Position(4, 6)).move(new Position(4, 4));

        // White pawn makes en passant
        board.getPosition(new Position(3, 4)).move(new Position(4, 5));

        assertEquals("7k/8/4P3/8/8/8/8/7K b - - 0 2", board.getFEN(),
                "Game position is not correct after making en passant.");
    }
}
