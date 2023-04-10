package sjakk.legalMoves;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import sjakk.Position;
import sjakk.pieces.*;

public class KingTest extends PieceTest {
    @Test
    public void testKingEmptyBoard() {
        King king = new King(new Position(5, 4), board, whitePlayer);
        board.disableCastling();

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(4, 3));
        actualLegal.add(new Position(4, 4));
        actualLegal.add(new Position(4, 5));
        actualLegal.add(new Position(5, 3));
        actualLegal.add(new Position(5, 5));
        actualLegal.add(new Position(6, 3));
        actualLegal.add(new Position(6, 4));
        actualLegal.add(new Position(6, 5));

        checkCollectionsEqual(king.getLegalMoves(), actualLegal, "King");
    }

    @Test
    public void testKing() {
        King king = new King(new Position(6, 5), board, whitePlayer);
        Piece blockingPiece = new Knight(new Position(5, 6), board, whitePlayer);
        Piece captureAndThreateningPiece = new Knight(new Position(6, 6), board, blackPlayer);
        Piece threateningPiece = new Knight(new Position(4, 2), board, blackPlayer);

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(5, 5));
        actualLegal.add(new Position(6, 4));
        actualLegal.add(new Position(6, 6));
        actualLegal.add(new Position(7, 5));
        actualLegal.add(new Position(7, 6));

        checkCollectionsEqual(king.getLegalMoves(), actualLegal, "King");
    }

    @Test
    public void testKingCastling() {
        King king = new King(new Position(4, 0), board, whitePlayer);
        Rook rookQueenSide = new Rook(new Position(0, 0), board, whitePlayer);
        Rook rookKingSide = new Rook(new Position(7, 0), board, whitePlayer);

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(2, 0));
        actualLegal.add(new Position(3, 0));
        actualLegal.add(new Position(5, 0));
        actualLegal.add(new Position(6, 0));

        actualLegal.add(new Position(3, 1));
        actualLegal.add(new Position(4, 1));
        actualLegal.add(new Position(5, 1));

        checkCollectionsEqual(king.getLegalMoves(), actualLegal, "King");
        king.move(new Position(2, 0));

        assertEquals(board.getPosition(new Position(0, 0)), null);
        assertEquals(board.getPosition(new Position(3, 0)), rookQueenSide);
    }

    @Test
    public void testKingCastlingPassThroughCheck() {
        King king = new King(new Position(4, 0), board, whitePlayer);
        Rook rookKingSide = new Rook(new Position(7, 0), board, whitePlayer);
        Rook rookQueenSide = new Rook(new Position(0, 0), board, whitePlayer);

        Rook threateningRook = new Rook(new Position(3, 7), board, blackPlayer);

        Collection<Position> actualLegal = new ArrayList<Position>();
        actualLegal.add(new Position(5, 0));
        actualLegal.add(new Position(6, 0));

        actualLegal.add(new Position(4, 1));
        actualLegal.add(new Position(5, 1));

        checkCollectionsEqual(king.getLegalMoves(), actualLegal, "King");
    }
}
