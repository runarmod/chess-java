package sjakk;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import sjakk.pieces.Pawn;
import sjakk.pieces.Queen;
import sjakk.utils.FENParser;

public class ChessBoardMoveTest {
        private ChessBoard board;
        private static final String defaultFENString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        @Test
        public void testFirstPawnMove() {
                assertDoesNotThrow(() -> board = FENParser.getBoardFromDefaultFEN(),
                                "Should not throw on default start.");

                assertEquals(board.getFEN(), defaultFENString, "Should be the same FEN-string.");

                assertFalse(board.getGameFinished(), "Game is not finished when it just started.");

                board.move(board.getPosition(new Position(0, 1)), new Position(0, 3));

                assertEquals("rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq a3 0 1", board.getFEN(),
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

        @Test
        public void testPromotion() {
                assertDoesNotThrow(() -> board = FENParser.getBoardFromFEN("8/p7/P7/6p1/4p2p/2pk4/5p2/3K4 w - - 0 44"),
                                "Should not throw on this FEN-string.");

                assertFalse(board.getGameFinished(), "Game is not finished.");

                // White king moves out of the way
                board.getPosition(new Position(3, 0)).move(new Position(2, 0));
                assertFalse(board.getGameFinished(), "Game is not finished.");

                // Black pawn moves to end of board and upgrades to queen
                board.getPosition(new Position(5, 1)).move(new Position(5, 0));
                assertFalse(board.getGameFinished(), "Game is not finished.");
                Pawn upgradablePawn = board.getUpgradablePawn();
                board.promotePawn(upgradablePawn, new Queen(new Position(5, 0), board, upgradablePawn.getOwner()));

                // Game ends in checkmate
                assertTrue(board.getGameFinished(), "Game is finished with checkmate after promotion.");
        }
}
