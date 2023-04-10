package sjakk.legalMoves;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import sjakk.Position;
import sjakk.pieces.*;

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
        Knight knight = new Knight(new Position(6, 5), board, whitePlayer);
        Piece blockingPiece = new Rook(new Position(5, 3), board, whitePlayer);
        Piece capturePiece = new Rook(new Position(7, 7), board, blackPlayer);

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(7, 7));
        actualLegal.add(new Position(5, 7));
        actualLegal.add(new Position(4, 6));
        actualLegal.add(new Position(4, 4));
        actualLegal.add(new Position(7, 3));

        checkCollectionsEqual(knight.getLegalMoves(), actualLegal, "Knight");
    }
}
