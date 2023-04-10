package sjakk.legalMoves;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;

import sjakk.ChessBoard;
import sjakk.PieceColor;
import sjakk.Player;
import sjakk.Position;

public abstract class PieceTest {
    protected ChessBoard board;
    protected Player whitePlayer;
    protected Player blackPlayer;

    @BeforeEach
    public void setUp() {
        whitePlayer = new Player(PieceColor.WHITE);
        blackPlayer = new Player(PieceColor.BLACK);
        board = new ChessBoard(true, whitePlayer, blackPlayer);
    }

    protected static void checkCollectionsEqual(Collection<Position> returnedLegal, Collection<Position> actualLegal,
            String piece) {
        assertTrue(actualLegal.containsAll(returnedLegal), piece + " getLegalMoves should return ALL legal moves.");
        assertTrue(returnedLegal.containsAll(actualLegal), piece + " getLegalMoves should return ONLY legal moves.");
    }
}
