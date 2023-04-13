package sjakk;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import sjakk.controllers.UpgradePawnController;
import sjakk.pieces.*;
import sjakk.utils.FENParser;
import sjakk.utils.PopUp;

/**
 * This class represents the chess board. It contains a 2D array of pieces, and
 * have all sorts methods to use.
 * 
 * @author Runar Saur Modahl
 * @version 1.0
 * @see Piece
 * @see Player
 */
public class ChessBoard implements Iterable<Piece> {

    private ArrayList<ArrayList<Piece>> board;
    private ArrayList<String> moves = new ArrayList<String>();
    private Piece selectedPiece;
    private Piece lastMovedPiece;
    private Player white;
    private Player black;
    private Player turn;
    private int halfMoves = 0;
    private int fullMoves = 1;
    private boolean gameFinished = false;
    private String gameMessage = "";

    public ChessBoard(Player white, Player black) {
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

        if (originalPos.getX() == 7) {
            piece.getOwner().setCastlingKingSide(false);
        } else if (originalPos.getX() == 0) {
            piece.getOwner().setCastlingQueenSide(false);
        }
    }

    /**
     * If the piece is a pawn that has made an en passant, the pawn that was
     * captured is removed from the board.
     * 
     * @param piece The piece that moved.
     * @return whether an en passant was made.
     */
    private boolean handleEnPassantMove(Piece piece) {
        if (piece instanceof Pawn && ((Pawn) piece).getHasMadeEnPassant()) {
            System.out.println("Pawn has made an passant");
            Position pos = new Position(piece.getX(), piece.getY() - piece.getOwner().getDir());
            setPosition(pos, null);
            return true;
        }
        return false;
    }

    /**
     * Moves a piece to a new position. Updates the information on the board and for
     * the pieces.
     * 
     * @param piece    The piece to move.
     * @param to       The position to move the piece to.
     * @param isCastle Whether the move is a castle move. If it is a castle-move,
     *                 the move count, list and turn is only modified if it is the
     *                 king which moves.
     */
    public void move(Piece piece, Position to, boolean isCastle) {
        if (!piece.isValidMove(to) && !isCastle)
            throw new IllegalArgumentException("Invalid move");

        Position originalPos = piece.getPos();
        boolean pieceWasCaptured = (getPosition(to) != null);

        setPosition(piece.getPos(), null);
        setPosition(to, piece);
        piece.setPos(to);

        handleCastlingDisabling(piece, originalPos);

        pieceWasCaptured |= handleEnPassantMove(piece);

        lastMovedPiece = piece;
        piece.addMovedCount();

        if (!isCastle || piece instanceof King) {
            moves.add(piece.getPos().toString() + to.toString());
            handleHalfMove(piece);
            handleFullMove(piece, pieceWasCaptured);
            turn = (turn == white ? black : white);
        }

        if (piece instanceof Pawn && (to.getY() == 0 || to.getY() == 7)) {
            promotePawn((Pawn) piece);
        }

        checkGameFinished();
    }

    /**
     * Moves a piece. Calls the other move-method with castleflag false.
     * 
     * @param piece The piece to move.
     * @param to    The position to move the piece to.
     * @see #move(Piece, Position, boolean)
     */
    public void move(Piece piece, Position to) {
        move(piece, to, false);
    }

    private void promotePawn(Pawn piece) {
        Piece newPiece;
        try {
            URL url = getClass().getResource("/sjakk/UpgradePawn.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(new UpgradePawnController());

            Node node = loader.load();
            PopUp popUp = new PopUp("Promote pawn", false);
            popUp.addNode(node);
            popUp.display();
            switch (UpgradePawnController.getUpgradeChoice()) {
                case "rook":
                    newPiece = new Rook(piece.getPos(), this, piece.getOwner());
                    break;
                case "bishop":
                    newPiece = new Bishop(piece.getPos(), this, piece.getOwner());
                    break;
                case "knight":
                    newPiece = new Knight(piece.getPos(), this, piece.getOwner());
                    break;
                case "queen":
                default:
                    newPiece = new Queen(piece.getPos(), this, piece.getOwner());
                    break;
            }

        } catch (IOException e) {
            System.out.println("Could not load fxml file, using queen as default promotion");
            newPiece = new Queen(piece.getPos(), this, piece.getOwner());
        }
        setPosition(piece.getPos(), newPiece);
    }

    private void handleHalfMove(Piece piece) {
        if (!piece.getOwner().isWhite()) {
            fullMoves++;
        }
    }

    private void handleFullMove(Piece piece, boolean pieceWasCaptured) {
        if (!(piece instanceof Pawn || pieceWasCaptured)) {
            halfMoves++;
        } else {
            halfMoves = 0;
        }
    }

    private void checkGameFinished() {
        if (gameFinished)
            return;

        if (inCheckmate(turn)) {
            System.out.println("Checkmate");
            gameFinished = true;
            gameMessage = turn + " got checkmated.";
        } else if (inStalemate(turn)) {
            System.out.println("Stalemate");
            gameFinished = true;
            gameMessage = turn + " got stalemated. Draw!";
        } else if (inDraw()) {
            System.out.println("Draw");
            gameFinished = true;
            gameMessage = "The game resultet in draw.";
        }
    }

    private boolean inDraw() {
        if (halfMoves >= 50)
            return true;

        return false;
    }

    private boolean inStalemate(Player player) {
        if (inCheck(player))
            return false;
        for (Piece piece : this) {
            if (piece.getOwner() == player) {
                for (Position pos : piece.getLegalMoves()) {
                    if (piece.isValidMove(pos))
                        return false;
                }
            }
        }

        return true;
    }

    private boolean inCheckmate(Player player) {
        if (!inCheck(player))
            return false;
        for (Piece piece : this) {
            if (piece.getOwner() == player) {
                for (Position pos : piece.getLegalMoves()) {
                    if (piece.isValidMove(pos))
                        return false;
                }
            }
        }
        return true;
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

    public Piece getLastPieceMoved() {
        return lastMovedPiece;
    }

    @Override
    public String toString() {
        return "ChessBoard [board=" + FENParser.generateFEN(this) + "]";
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
        String rights = white.getCastlingRights() + black.getCastlingRights();
        if (rights.length() == 0)
            rights = "-";
        return rights;
    }

    public String getFEN() {
        return FENParser.generateFEN(this);
    }

    public Player getPlayerTurn() {
        return turn;
    }

    public void setHalfMoves(int moves) {
        halfMoves = moves;
    }

    public int getHalfMoves() {
        return halfMoves;
    }

    public void setFullMoves(int moves) {
        fullMoves = moves;
    }

    public int getFullMoves() {
        return fullMoves;
    }

    public boolean getGameFinished() {
        return gameFinished;
    }

    public String getGameMessage() {
        return gameMessage;
    }

    public void setSelectedPiece(Piece piece) {
        selectedPiece = piece;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }
}
