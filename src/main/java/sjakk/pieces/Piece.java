package sjakk.pieces;

import java.util.Collection;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import sjakk.ChessBoard;
import sjakk.Player;
import sjakk.Position;

/**
 * This abstract class represents all pieces on the board. It can be used to
 * move pieces on the board, get the possible moves for a piece, get the image
 * of the piece, see whether or not a position is threatened by the piece, and
 * more.
 * 
 * @author Runar Saur Modahl
 * @version 1.0
 * @see King
 * @see Queen
 * @see Rook
 * @see Bishop
 * @see Knight
 * @see Pawn
 */
public abstract class Piece {
    protected Position pos;
    protected ChessBoard board;
    protected Player owner;
    protected ImageView imageView;
    protected String name;

    protected int moveCount = 0;

    /**
     * Creates a new piece at the given position on the given board. The pice is
     * automatically places on the board if its not already in position.
     * 
     * @param position the position of the piece
     * @param board    the board the piece is on
     * @param color    the color of the piece
     */
    public Piece(Position position, ChessBoard board, Player owner, String name) {
        this.pos = position;
        this.board = board;
        this.name = name;
        if (board.getPosition(position) != this) {
            board.setPosition(position, this);
        }
        this.owner = owner;

        if (!board.isTest())
            cropPieceImage(name);
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Returns a collection of all legal moves for this piece.
     * 
     * @return a collection of all legal moves for this piece
     */
    public abstract Collection<Position> getLegalMoves();

    /**
     * Returns whether or not the piece is threatening the given position.
     * 
     * @param position the position
     * @return {@code true} if the piece is threatening the given position
     */
    protected abstract boolean threatening(Position position);

    /**
     * Gets the image of the piece.
     * 
     * @return the image of the piece
     */
    public ImageView getImage() {
        return imageView;
    }

    /**
     * Returns the x coordinate of the piece.
     * 
     * @return the x coordinate of the piece
     */
    public int getX() {
        return pos.getX();
    }

    /**
     * Returns the y coordinate of the piece.
     * 
     * @return the y coordinate of the piece
     */
    public int getY() {
        return pos.getY();
    }

    /**
     * Returns whether or not the given position is a legal move for this piece.
     * 
     * @param to the position to check
     * @return true if the given position is a current legal move for this piece
     */
    public boolean isValidMove(Position to) {
        return getLegalMoves().contains(to);
    }

    /**
     * Returns the color of the piece.
     * 
     * @return the color of the piece (1 if white, 0 if black)
     */
    public boolean isWhite() {
        return owner.isWhite();
    }

    /**
     * Returns the position of the piece.
     * 
     * @return the position of the piece
     */
    public Position getPos() {
        return new Position(pos);
    }

    /**
     * Sets the position of the piece.
     * 
     * @param pos the new position of the piece
     */
    public void setPos(Position pos) {
        this.pos = pos;
    }

    /**
     * Moves the piece to the given position on the board.
     * 
     * @throws IllegalArgumentException if the move is not legal
     * @param to the position to move to
     */
    public void move(Position to) throws IllegalArgumentException {
        if (!isValidMove(to)) {
            throw new IllegalArgumentException("Illegal move");
        }
        board.move(this, to);
    }

    /**
     * Sets the background of all legal moves to light green.
     */
    public void setLegalMovesBackground() {
        for (Position pos : getLegalMoves()) {
            board.setGridBackgroundColor(pos.getX(), pos.getY(), Color.LIGHTGREEN);
        }
    }

    /**
     * Gets the number of times the piece has moved.
     */
    public int getMoveCount() {
        return moveCount;
    }

    /**
     * Adds one to the move count.
     */
    public void addMovedCount() {
        moveCount++;
    }

    /**
     * Returns whether or not a given move for the piece will set the king in check.
     * 
     * @param position the position to move the piece to
     * @return {@code true} if the king is in check after the move, {@code false}
     *         otherwise
     */
    protected boolean messesUpcheck(Position to) {
        if (!to.insideBoard())
            return false;

        Piece tmp = board.getPosition(to);
        board.setPosition(to, this);
        board.setPosition(pos, null);
        boolean messesUp = board.inCheck(owner);
        board.setPosition(pos, this);
        board.setPosition(to, tmp);
        return messesUp;
    }

    /**
     * Crops the image of the piece from the image of all pieces and sets it as the
     * image of this piece.
     * 
     * @param pieceType the type of the piece (ex. "King")
     */
    private void cropPieceImage(String pieceType) {
        this.imageView = new ImageView(ChessBoard.getAllPiecesImg());

        int imgY = (owner.isWhite() ? 0 : 1);
        int coloumnIndex = ChessBoard.getPieceImageIndex(pieceType);
        final int size = 50;

        this.imageView.setViewport(new Rectangle2D(coloumnIndex * size, imgY * size, size, size));

        // Shrink to have border visible
        this.imageView.setFitWidth(48);
        this.imageView.setPreserveRatio(true);
    }

    /**
     * Places a piece for a given player on a postion on the map. The piece placed
     * is decided by the parameter {@value c}. If c is uppercase, it will be white,
     * otherwise it will be black. K = King, Q = Queen, R = Rook, B = Bishop, N =
     * Knight, and P = Pawn.
     * 
     * @param player the player to place the piece for
     * @param pos    the position to place the piece on
     * @param board  the board to place the piece on
     * @param c      the character representing the piece to place
     * @return the piece placed
     */
    public static Piece placePiece(Player player, Position pos, ChessBoard board, char c) {
        Piece piece;

        switch (c) {
            case 'K':
            case 'k':
                piece = new King(pos, board, player);
                break;
            case 'Q':
            case 'q':
                piece = new Queen(pos, board, player);
                break;
            case 'R':
            case 'r':
                piece = new Rook(pos, board, player);
                break;
            case 'B':
            case 'b':
                piece = new Bishop(pos, board, player);
                break;
            case 'N':
            case 'n':
                piece = new Knight(pos, board, player);
                break;
            case 'P':
            case 'p':
                piece = new Pawn(pos, board, player);
                break;
            default:
                piece = null;
        }
        return piece;
    }

    /**
     * Get the owner of the piece
     * 
     * @return the owner of the piece
     */
    public Player getOwner() {
        return owner;
    }

}
