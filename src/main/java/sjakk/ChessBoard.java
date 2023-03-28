package sjakk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import sjakk.pieces.*;

public class ChessBoard implements Iterable<Piece> {
    private static Image allPiecesImg;
    private final static Map<String, Integer> imgColIdx = Map.of("King", 0, "Queen", 1, "Bishop", 2, "Knight", 3,
            "Rook", 4,
            "Pawn", 5);

    public static Image getAllPiecesImg() {
        return allPiecesImg;
    }

    public static int getPieceImageIndex(String pieceType) {
        return imgColIdx.get(pieceType);
    }

    private ArrayList<ArrayList<Color>> gridBackgroundColor;
    private ArrayList<ArrayList<Piece>> board;
    private ArrayList<String> moves = new ArrayList<String>();
    private Piece selectedPiece;
    private boolean test = false;
    private Piece lastMovedPiece;
    private Player white;
    private Player black;
    private Player turn;

    public ChessBoard(Player white, Player black) {
        this(false, white, black);
    }

    public ChessBoard(boolean test, Player white, Player black) {
        this.test = test;

        board = new ArrayList<ArrayList<Piece>>();
        for (int i = 0; i < 8; i++) {
            board.add(new ArrayList<Piece>());
            for (int j = 0; j < 8; j++) {
                board.get(i).add(null);
            }
        }
        this.white = white;
        this.black = black;

        if (white == null)
            this.white = new Player(PieceColor.WHITE);
        if (black == null)
            this.black = new Player(PieceColor.BLACK);

        turn = white;

        resetGridBackgroundMatrix();

        if (!test)
            allPiecesImg = new Image(getClass().getResource("pieces.png").toString());

    }

    public void resetGridBackgroundMatrix() {
        gridBackgroundColor = new ArrayList<ArrayList<Color>>();
        for (int i = 0; i < 8; i++) {
            gridBackgroundColor.add(new ArrayList<Color>());
            for (int j = 0; j < 8; j++) {
                gridBackgroundColor.get(i).add((i + j) % 2 == 0 ? Color.GRAY : Color.WHITE);
            }
        }
    }

    public boolean isTest() {
        return test;
    }

    /**
     * Handle a click on a position on the board. Possible results are:
     * <ol>
     * <li>
     * No piece is selected. Select the piece at the position if it exists.
     * </li>
     * <li>
     * A piece is selected. If the new position is a valid move for the selected
     * piece, move the piece.
     * </li>
     * <li>
     * A piece is selected. If the new position is not a valid move for the
     * selected piece, deselect the piece.
     * </li>
     * </ol>
     * 
     * @param position The position that was clicked.
     */
    public void positionPressed(Position position) {
        resetGridBackgroundMatrix();
        if (selectedPiece == null) {
            selectedPiece = getPosition(position);
            // If the selected piece is not the correct color, deselect it.
            if (selectedPiece != null && selectedPiece.getOwner() != turn) {
                selectedPiece = null;
                return;
            }
            if (selectedPiece != null) {
                setGridBackgroundColor(position.getX(), position.getY(), Color.YELLOW);
                selectedPiece.setLegalMovesBackground();
            }
            return;
        }

        if (turn != selectedPiece.getOwner())
            return;

        try {
            selectedPiece.move(position);
        } catch (Exception e) {
            System.out.println("Invalid move");
            selectedPiece = null;
            return;
        }

        selectedPiece = null;
        turn = (turn == white ? black : white);
    }

    public boolean isValidMove(Piece piece, Position position) {
        return piece.isValidMove(position);
    }

    public Piece getPosition(Position position) {
        if (position.getX() < 0 || position.getX() > 7 || position.getY() < 0 || position.getY() > 7)
            return null;
        return board.get(position.getY()).get(position.getX());
    }

    public void setPosition(Position position, Piece piece) {
        if (getPosition(position) == piece)
            return;
        board.get(position.getY()).set(position.getX(), piece);
    }

    /**
     * Disables castling for the player if the king or rook has moved. If king
     * moves, both sides are disabled. If rook moves, only the side the rook started
     * on is disabled.
     * 
     * @param piece       The piece that moved.
     * @param originalPos The position the piece moved from.
     */
    private void handleCastlingDisabling(Piece piece, Position originalPos) {
        if (piece instanceof King) {
            piece.getOwner().disableCastling();
            return;
        }
        if (!(piece instanceof Rook))
            return;

        if (piece.getMoveCount() != 0) {
            return;
        }

        if (piece.getX() == 7) {
            piece.getOwner().setCastlingKingSide(false);
        } else if (piece.getX() == 0) {
            piece.getOwner().setCastlingQueenSide(false);
        }
    }

    /**
     * If the piece is a pawn that has made an en passant, the pawn that was
     * captured is removed from the board.
     * 
     * @param piece The piece that moved.
     */
    private void handleEnPassantMove(Piece piece) {
        if (piece instanceof Pawn && ((Pawn) piece).getHasMadeEnPassant()) {
            System.out.println("Pawn has made an passant");
            Position pos = new Position(piece.getX(), piece.getY() - piece.getOwner().getDir());
            setPosition(pos, null);
        }
    }

    /**
     * Moves a piece to a new position. Updates the information on the board and for
     * the pieces.
     * 
     * @param piece The piece to move.
     * @param to    The position to move the piece to.
     */
    public void move(Piece piece, Position to) {
        Position originalPos = piece.getPos();

        moves.add(piece.getPos().toString() + to.toString());
        setPosition(piece.getPos(), null);
        setPosition(to, piece);
        piece.setPos(to);

        handleCastlingDisabling(piece, originalPos);
        handleEnPassantMove(piece);

        lastMovedPiece = piece;
        piece.addMovedCount();
    }

    /**
     * Checks if the player is in check.
     * 
     * @param player The player to check.
     * @return {@code true} if the player is in check, {@code false} otherwise.
     */
    public boolean inCheck(Player player) {
        for (Piece piece : this) {
            if (piece.getOwner() == player && piece instanceof King) {
                King king = (King) piece;
                return king.inCheck();
            }
        }
        return false;
    }

    /**
     * Gets the moves made in the game in string format (example {@code "e2e4
     * e7e5\n d2d3"}).
     */
    public String getMoves() {
        String movesString = "";
        for (int i = 0; i < moves.size(); i++) {
            if (i % 2 == 0) {
                int moveNr = (i / 2 + 1);
                String moveNrString = String.format("%2s", moveNr);
                movesString += moveNrString + ". ";
                movesString += moves.get(i);
            } else {
                movesString += "  | " + moves.get(i) + "\n";
            }
        }
        return movesString;
    }

    @Override
    public Iterator<Piece> iterator() {
        return new ChessBoardIterator(this);
    }

    public Color getGridBackgroundColor(int x, int y) {
        return gridBackgroundColor.get(y).get(x);
    }

    public void setGridBackgroundColor(int x, int y, Color color) {
        gridBackgroundColor.get(y).set(x, color);
    }

    public Piece getLastPieceMoved() {
        return lastMovedPiece;
    }

    @Override
    public String toString() {
        return "ChessBoard [board=" + board + "]";
    }

    public void setTurn(PieceColor color) {
        this.turn = (color == PieceColor.WHITE ? white : black);
    }

    public void setLastPieceMoved(Piece piece) {
        this.lastMovedPiece = piece;
    }

    public void disableCastling() {
        white.disableCastling();
        black.disableCastling();
    }

    public void setWhiteKingSideCastle(boolean b) {
        white.setCastlingKingSide(b);
    }

    public void setWhiteQueenSideCastle(boolean b) {
        white.setCastlingQueenSide(b);
    }

    public void setBlackKingSideCastle(boolean b) {
        black.setCastlingKingSide(b);
    }

    public void setBlackQueenSideCastle(boolean b) {
        black.setCastlingQueenSide(b);
    }

    public String getCastlingRights() {
        String out = "White: " + white.getCastlingRights();
        out += "\nBlack: " + black.getCastlingRights();
        return out;
    }
}
