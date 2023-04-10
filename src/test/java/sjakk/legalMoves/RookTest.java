package sjakk.legalMoves;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import sjakk.Position;
import sjakk.pieces.*;

public class RookTest extends PieceTest {
    @Test
    public void testRookEmptyBoard() {
        Rook rook = new Rook(new Position(0, 0), board, whitePlayer);

        Collection<Position> actualLegal = new ArrayList<Position>();
        for (int i = 1; i < 8; i++) {
            actualLegal.add(new Position(i, 0));
            actualLegal.add(new Position(0, i));
        }

        checkCollectionsEqual(rook.getLegalMoves(), actualLegal, "Rook");
    }

    @Test
    public void testRookBlockingPiece() {
        Piece blockingPiece = new Rook(new Position(3, 4), board, blackPlayer);
        board.setPosition(new Position(3, 4), blockingPiece);

        Rook rook = new Rook(new Position(4, 4), board, blackPlayer);

        Collection<Position> actualLegal = new ArrayList<Position>();
        for (int i = 0; i < 8; i++) {
            if (i == 4)
                continue;
            if (i > 4) {
                actualLegal.add(new Position(i, 4));
            }
            actualLegal.add(new Position(4, i));
        }

        checkCollectionsEqual(rook.getLegalMoves(), actualLegal, "Rook");

        assertThrows(IllegalArgumentException.class, () -> {
            rook.move(new Position(3, 4));
            rook.move(new Position(0, 0));
        });
    }

    @Test
    public void testRookCapturePiece() {
        Rook captureRook = new Rook(new Position(3, 4), board, whitePlayer);
        board.setPosition(new Position(3, 4), captureRook);

        Rook rook = new Rook(new Position(4, 4), board, blackPlayer);

        Collection<Position> actualLegal = new ArrayList<Position>();
        for (int i = 0; i < 8; i++) {
            if (i == 4)
                continue;
            if (i > 2) {
                actualLegal.add(new Position(i, 4));
            }
            actualLegal.add(new Position(4, i));
        }

        checkCollectionsEqual(rook.getLegalMoves(), actualLegal, "Rook");
    }
}
