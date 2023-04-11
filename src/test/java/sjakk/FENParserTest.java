package sjakk;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import sjakk.utils.FENParser;
import sjakk.utils.IllegalFENException;

public class FENParserTest {

    private ChessBoard board;
    private static final String defaultFENString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Test
    public void testLegalRandomFischerRandom() {
        String fischerRandomFEN = FENParser.generateFischerRandomFEN();
        assertTrue(FENParser.legalFischerRandom(fischerRandomFEN),
                "Didn't generate a legal fischer random FEN-string.");
    }

    @Test
    public void testLegalFischerRandomTester() {
        String defaultFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        assertTrue(FENParser.legalFischerRandom(defaultFEN),
                "Default start position should be legal as Fischer Random.");

        String illegalFischerRandom = "rnbqbnrk/pppppppp/8/8/8/8/PPPPPPPP/RNBQBNRK w KQkq - 0 1";
        assertFalse(FENParser.legalFischerRandom(illegalFischerRandom),
                "King has to be between rooks.");

        String illegalFischerRandom2 = "rbnbqknr/pppppppp/8/8/8/8/PPPPPPPP/RBNBQKNR w KQkq - 0 1";
        assertFalse(FENParser.legalFischerRandom(illegalFischerRandom2),
                "Bishops has to be on opposite colored tiles.");

        String illegalFischerRandom3 = "rnbqkbnr/pppppppp/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        assertFalse(FENParser.legalFischerRandom(illegalFischerRandom3),
                "Board has to be 8 rows.");
    }

    @Test
    public void testChessboardFromFEN() {
        assertThrows(IllegalFENException.class,
                () -> FENParser.getBoardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"),
                "Should throw on missing turn.");

        assertThrows(IllegalFENException.class,
                () -> FENParser.getBoardFromFEN("rnbqkbnr/pppppppp/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"),
                "Should throw on illegal board.");

        assertDoesNotThrow(
                () -> board = FENParser.getBoardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"),
                "Should not throw on default start.");

        assertEquals(board.getFEN(), defaultFENString, "Should be the same FEN-string.");
    }
}
