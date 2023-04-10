package sjakk.legalMoves;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import sjakk.Position;
import sjakk.pieces.*;

public class QueenTest extends PieceTest {
    @Test
    public void testQueenEmptyBoard() {
        Queen queen = new Queen(new Position(4, 4), board, whitePlayer);

        Collection<Position> actualLegal = new ArrayList<Position>();
        for (int i = 0; i < 8; i++) {
            if (i == 4)
                continue;

            // Horizontal
            actualLegal.add(new Position(i, 4));

            // Vertical
            actualLegal.add(new Position(4, i));

            // Diagonal up right
            actualLegal.add(new Position(i, i));

            // Diagonal down right
            if (i == 0)
                continue;
            actualLegal.add(new Position(i, 8 - i));
        }

        checkCollectionsEqual(queen.getLegalMoves(), actualLegal, "Queen");
    }

    @Test
    public void testQueenBlockingPiece() {
        Queen queen = new Queen(new Position(4, 4), board, whitePlayer);
        Piece blockingPiece = new Rook(new Position(4, 6), board, whitePlayer);
        Piece capturePiece = new Rook(new Position(2, 2), board, blackPlayer);

        Collection<Position> actualLegal = new ArrayList<Position>();

        // Horizontal
        for (int i = 0; i < 8; i++) {
            if (i == 4)
                continue;
            actualLegal.add(new Position(i, 4));
        }

        // Vertical
        for (int i = 5; i >= 0; i--) {
            if (i == 4)
                continue;
            actualLegal.add(new Position(4, i));
        }

        // Down right
        for (int i = 1; i < 8; i++) {
            if (i == 4)
                continue;
            actualLegal.add(new Position(8 - i, i));
        }

        // Up right
        for (int i = 2; i < 8; i++) {
            if (i == 4)
                continue;
            actualLegal.add(new Position(i, i));
        }

        checkCollectionsEqual(queen.getLegalMoves(), actualLegal, "Queen");
    }
}
