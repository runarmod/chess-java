package sjakk.legalMoves;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import sjakk.Position;
import sjakk.pieces.*;

public class PawnTest extends PieceTest {
    @Test
    public void testPawnFirstMove() {
        Pawn pawn = new Pawn(new Position(4, 1), board, whitePlayer);

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(4, 2));
        actualLegal.add(new Position(4, 3));

        checkCollectionsEqual(pawn.getLegalMoves(), actualLegal, "Pawn");
    }

    @Test
    public void testPawnNotFirstMove() {
        Pawn pawn = new Pawn(new Position(4, 2), board, whitePlayer);
        pawn.move(new Position(4, 3));

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(4, 4));

        checkCollectionsEqual(pawn.getLegalMoves(), actualLegal, "Pawn");
    }

    @Test
    public void testPawnBlockingPieceFirstMove() {
        Pawn pawn = new Pawn(new Position(4, 1), board, whitePlayer);
        Piece blockingPiece = new Rook(new Position(4, 2), board, whitePlayer);

        Collection<Position> actualLegal = new ArrayList<Position>();

        checkCollectionsEqual(pawn.getLegalMoves(), actualLegal, "Pawn");
    }

    @Test
    public void testPawnTakePiece() {
        Pawn pawn = new Pawn(new Position(4, 3), board, whitePlayer);
        Piece blockingPiece = new Rook(new Position(5, 4), board, blackPlayer);

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(5, 4));
        actualLegal.add(new Position(4, 4));

        checkCollectionsEqual(pawn.getLegalMoves(), actualLegal, "Pawn");
    }

    @Test
    public void testPawnEnPassant() {
        Pawn pawn = new Pawn(new Position(4, 1), board, whitePlayer);
        Pawn tmp = new Pawn(new Position(0, 6), board, blackPlayer);
        Pawn toTake = new Pawn(new Position(5, 6), board, blackPlayer);
        pawn.move(new Position(4, 3));
        tmp.move(new Position(0, 4));
        pawn.move(new Position(4, 4));
        toTake.move(new Position(5, 4));

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(4, 5));
        actualLegal.add(new Position(5, 5));

        checkCollectionsEqual(pawn.getLegalMoves(), actualLegal, "Pawn");
    }
}
