package sjakk.pieces;

import java.util.ArrayList;
import java.util.Collection;

import sjakk.ChessBoard;
import sjakk.PieceColor;
import sjakk.Position;

public abstract class LinearPiece extends Piece {
    
        public LinearPiece(Position position, ChessBoard board, PieceColor color, String name) {
            super(position, board, color, name);
        }

        public Collection<Position> getLinearMoves(Position... legalDirections) {
            Collection<Position> legalMoves = new ArrayList<Position>();
    
            for (Position tmpDir : legalDirections) {
    
                Position currentPosition = new Position(pos).add(tmpDir);
                while (currentPosition.insideBoard()) {
                    if (board.getPosition(currentPosition) == null) {
                        legalMoves.add(currentPosition);
                    } else {
                        if (board.getPosition(currentPosition).getColor() != color) {
                            legalMoves.add(currentPosition);
                        }
                        break;
                    }
                    currentPosition = currentPosition.add(tmpDir);
                }
            }
            return legalMoves;
        }
}