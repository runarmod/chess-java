package sjakk;

import java.util.ArrayList;
import java.util.Iterator;

import sjakk.pieces.*;
import sjakk.utils.FENParser;

/**
 * This class represents the chess board. It contains a 2D array of pieces, and
 * have all sorts methods to use.
 * 
 * @see Piece
 * @see Player
 */
public class ChessBoard implements Iterable<Piece> {

    private final ArrayList<ArrayList<Piece>> board;
    private final ArrayList<String> moves = new ArrayList<String>();
    private Piece selectedPiece;
    private Piece lastMovedPiece;
    private Player white;
    private Player black;
    private Player turn;
    private int halfMoves = 0;
    private int fullMoves = 1;
    private boolean gameFinished = false;
    private String gameMessage = "";
    private Pawn upgradablePawn = null;

    /**
     * Creates a new chess board with two new players.
     */
    public ChessBoard() {
        this(new Player(PieceColor.WHITE), new Player(PieceColor.BLACK));
    }

    /**
     * Creates a new chess board with the two given players.
     */
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

    /**
     * Gets the piece at the given position.
     * 
     * @param position The position to get the piece from.
     * @return The piece at the given position, or null if the position is out of
     *         bounds.
     */
    public Piece getPosition(Position position) {
        if (position.getX() < 0 || position.getX() > 7 || position.getY() < 0 || position.getY() > 7)
            return null;
        return board.get(position.getY()).get(position.getX());
    }

    /**
     * Sets the piece at the given position.
     * 
     * @param position The position to set the piece at.
     * @param piece    The piece to set at the given position.
     */
    public void setPosition(Position position, Piece piece) {
        if (getPosition(position) == piece)
            return;
        board.get(position.getY()).set(position.getX(), piece);
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

        final Position originalPos = piece.getPos();
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
            setPromotePawn((Pawn) piece);
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

    /**
     * Checks if the player is in check.
     * 
     * @param player The player to check.
     * @return {@code true} if the player is in check, {@code false} otherwise.
     */
    public boolean inCheck(Player player) {
        for (final Piece piece : this) {
            if (piece.getOwner() == player && piece instanceof King) {
                final King king = (King) piece;
                return king.inCheck();
            }
        }
        return false;
    }

    /**
     * Gets the moves made in the game in string format (example
     * {@code "e2e4 | e7e5\n d2d3 | ..."}).
     * 
     * @return The moves made in the game.
     */
    public String getMoves() {
        String movesString = "";
        for (int i = 0; i < moves.size(); i++) {
            if (i % 2 == 0) {
                final int moveNr = (i / 2 + 1);
                final String moveNrString = String.format("%2s", moveNr);
                movesString += moveNrString + ". ";
                movesString += moves.get(i);
            } else {
                movesString += "  | " + moves.get(i) + "\n";
            }
        }
        return movesString;
    }

    /**
     * Gets an iterator for the chess board.
     * 
     * @return An iterator for the chess board.
     */
    @Override
    public Iterator<Piece> iterator() {
        return new ChessBoardIterator(this);
    }

    /**
     * Gets the piece which moved last.
     * 
     * @return The piece which moved last.
     */
    public Piece getLastPieceMoved() {
        return lastMovedPiece;
    }

    /**
     * Gets the string representation of the chess board.
     */
    @Override
    public String toString() {
        return "ChessBoard [board=" + FENParser.generateFEN(this) + "]";
    }

    /**
     * Sets the turn to the given color.
     * 
     * @param color The color to set the turn to.
     */
    public void setTurn(PieceColor color) {
        this.turn = (color == PieceColor.WHITE ? white : black);
    }

    /**
     * Sets the last piece moved.
     * 
     * @param piece The piece to set as last moved.
     */
    public void setLastPieceMoved(Piece piece) {
        this.lastMovedPiece = piece;
    }

    /**
     * Disables castling for both players.
     */
    public void disableCastling() {
        white.disableCastling();
        black.disableCastling();
    }

    /**
     * Gets the castling rights for both players.
     * 
     * @return The castling rights for both players.
     */
    public String getCastlingRights() {
        String rights = white.getCastlingRights() + black.getCastlingRights();
        if (rights.length() == 0)
            rights = "-";
        return rights;
    }

    /**
     * Gets the FEN representation of the chess board.
     * 
     * @return The FEN representation of the chess board.
     */
    public String getFEN() {
        return FENParser.generateFEN(this);
    }

    /**
     * Gets the player whos turn it is.
     * 
     * @return The player.
     */
    public Player getPlayerTurn() {
        return turn;
    }

    /**
     * Sets half moves.
     * 
     * @param moves The number of half moves.
     */
    public void setHalfMoves(int moves) {
        if (moves < 0)
            throw new IllegalArgumentException("Number of half moves must be positive.");
        halfMoves = moves;
    }

    /**
     * Gets half moves.
     * 
     * @return The number of half moves.
     */
    public int getHalfMoves() {
        return halfMoves;
    }

    /**
     * Sets full moves.
     * 
     * @param moves The number of full moves.
     */
    public void setFullMoves(int moves) {
        if (moves < 0)
            throw new IllegalArgumentException("Number of full moves must be positive.");
        fullMoves = moves;
    }

    /**
     * Gets full moves.
     * 
     * @return The number of full moves.
     */
    public int getFullMoves() {
        return fullMoves;
    }

    /**
     * Gets whether the game is finished.
     * 
     * @return {@code true} if the game is finished, {@code false} otherwise.
     */
    public boolean getGameFinished() {
        return gameFinished;
    }

    /**
     * Gets the game message. This would be something like "Draw", "Stalemate"...
     * 
     * @return The game message.
     */
    public String getGameMessage() {
        return gameMessage;
    }

    /**
     * Sets the selected piece.
     * 
     * @param piece The piece to set as selected.
     */
    public void setSelectedPiece(Piece piece) {
        selectedPiece = piece;
    }

    /**
     * Gets the selected piece.
     * 
     * @return The selected piece.
     */
    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    /**
     * Gets the pawn that can be upgraded.
     * 
     * @return The pawn that can be upgraded.
     */
    public Pawn getUpgradablePawn() {
        return this.upgradablePawn;
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
            piece.getOwner().setCastling(false, false);
        } else if (originalPos.getX() == 0) {
            piece.getOwner().setCastling(false, true);
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
            System.out.println("Pawn has made en passant");
            final Position pos = new Position(piece.getX(), piece.getY() - piece.getOwner().getDir());
            setPosition(pos, null);
            return true;
        }
        return false;
    }

    /**
     * Selects the pawn that should be upgraded.
     * 
     * @param piece The pawn to promote.
     */
    private void setPromotePawn(Pawn piece) {
        this.upgradablePawn = piece;
    }

    public void promotePawn(Pawn piece, Piece upgrade) {
        setPosition(piece.getPos(), upgrade);
        checkGameFinished();
        this.upgradablePawn = null;
    }

    /**
     * Handles the half move counter. If the piece is black, the counter is
     * incremented.
     * 
     * @param piece The piece that moved. (Used to get the owner)
     */
    private void handleHalfMove(Piece piece) {
        if (!piece.getOwner().isWhite()) {
            fullMoves++;
        }
    }

    /**
     * Handles the full move counter. If the piece is a pawn or a piece was
     * captured, the counter increments. Otherwise, it resets to 0.
     * 
     * @param piece            The piece that moved.
     * @param pieceWasCaptured Whether a piece was captured.
     */
    private void handleFullMove(Piece piece, boolean pieceWasCaptured) {
        if (!(piece instanceof Pawn || pieceWasCaptured)) {
            halfMoves++;
        } else {
            halfMoves = 0;
        }
    }

    /**
     * Checks if the game is finished. If it is, the gameMessage is updated.
     */
    private void checkGameFinished() {
        if (gameFinished)
            return;

        if (inCheckmate(turn)) {
            gameFinished = true;
            gameMessage = turn + " got checkmated.";
        } else if (inStalemate(turn)) {
            gameFinished = true;
            gameMessage = turn + " got stalemated. Draw!";
        } else if (inDraw()) {
            gameFinished = true;
            gameMessage = "The game resultet in draw.";
        }
    }

    /**
     * Checks if the game is in a state of draw.
     * 
     * @return whether the game is a draw.
     */
    private boolean inDraw() {
        if (halfMoves >= 50)
            return true;

        return false;
    }

    /**
     * Checks if the player is in stalemate.
     * 
     * @param player The player to check.
     * @return {@code true} if the player is in stalemate, {@code false} otherwise.
     */
    private boolean inStalemate(Player player) {
        if (inCheck(player))
            return false;
        for (final Piece piece : this) {
            if (piece.getOwner() == player) {
                for (final Position pos : piece.getLegalMoves()) {
                    if (piece.isValidMove(pos))
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the player is in checkmate.
     * 
     * @param player The player to check.
     * @return {@code true} if the player is in checkmate, {@code false} otherwise.
     */
    private boolean inCheckmate(Player player) {
        if (!inCheck(player))
            return false;
        for (final Piece piece : this) {
            if (piece.getOwner() == player) {
                for (final Position pos : piece.getLegalMoves()) {
                    if (piece.isValidMove(pos))
                        return false;
                }
            }
        }
        return true;
    }
}
