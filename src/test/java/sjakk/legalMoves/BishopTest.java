package sjakk.legalMoves;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import sjakk.Position;
import sjakk.pieces.*;

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
        Bishop bishop = new Bishop(new Position(6, 4), board, whitePlayer);
        Rook blockingPiece = new Rook(new Position(4, 6), board, whitePlayer);
        Rook capturePiece = new Rook(new Position(3, 1), board, blackPlayer);

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
}
