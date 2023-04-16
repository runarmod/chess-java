package sjakk.pieces;

import java.util.Collection;

import sjakk.ChessBoard;
import sjakk.Player;
import sjakk.Position;

/**
 * This abstract class represents pieces on a board. It can be used to
 * move pieces on the board, get the possible moves for a piece, see whether or
 * not a position is threatened by the piece, and more.
 * 
 * @author Runar Saur Modahl
 * @see King
 * @see Queen
 * @see Rook
 * @see Bishop
 * @see Knight
 * @see Pawn
 */
public abstract class Piece {
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

    protected Position pos;
    protected ChessBoard board;
    protected Player owner;
    protected String name;

    protected int moveCount = 0;

    /**
     * Creates a new piece at the given position on the given board. The pice is
     * automatically placed on the board if its not already in position.
     * 
     * @param position the position of the piece
     * @param board    the board the piece is on
     * @param color    the color of the piece
     */
    public Piece(Position position, ChessBoard board, Player owner, String name) {
        this.pos = position;
        this.board = board;
        this.owner = owner;
        this.name = name;
        if (board.getPosition(position) != this) {
            board.setPosition(position, this);
        }
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Returns a char representation of the piece. Lowercase is black, uppercase is
     * white. K = King, Q = Queen, R = Rook, B = Bishop, N = Knight, and P = Pawn.
     * 
     * @return a char representation of the piece.
     */
    public char toChar() {
        char out = name.toLowerCase().toCharArray()[0];
        if (this instanceof Knight)
            out = 'n';

        if (owner.isWhite())
            out = Character.toUpperCase(out);
        return out;
    }

    /**
     * Returns a collection of all legal moves for this piece.
     * 
     * @return a collection of all legal moves for this piece
     */
    public abstract Collection<Position> getLegalMoves();

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
     * @return {@code true} if the given position is a legal move for this piece.
     */
    public boolean isValidMove(Position to) {
        return getLegalMoves().contains(to);
    }

    /**
     * Return the color of the piece as boolean.
     * 
     * @return the color of the piece ({@code true} if white, {@code false} if
     *         black)
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
        if (pos.getX() < 0 || pos.getX() > 7 || pos.getY() < 0 || pos.getY() > 7)
            throw new IllegalArgumentException("Not a valid position");
        this.pos = pos;
    }

    /**
     * Moves the piece to the given position on the board.
     * 
     * @param to the position to move to
     * @throws IllegalArgumentException if the move is not legal
     */
    public void move(Position to) throws IllegalArgumentException {
        if (!isValidMove(to)) {
            throw new IllegalArgumentException("Illegal move");
        }
        board.move(this, to);
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
    public void addMoveCount() {
        moveCount++;
    }

    /**
     * Get the owner of the piece
     * 
     * @return the owner of the piece
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Returns whether or not the piece is threatening the given position.
     * 
     * @param position the position
     * @return {@code true} if the piece is threatening the given position
     */
    protected abstract boolean threatening(Position position);

    /**
     * Returns whether or not a given move for the piece will set the king in check.
     * Will <b>not</b> modify the board.
     * 
     * @param to the position to move the piece to
     * @return {@code true} if the king is in check after the move, {@code false}
     *         otherwise
     */
    protected boolean messesUpcheck(Position to) {
        if (!to.insideBoard())
            return false;

        final Piece tmp = board.getPosition(to);
        board.setPosition(to, this);
        board.setPosition(pos, null);

        final boolean messesUp = board.inCheck(owner);
        board.setPosition(pos, this);
        board.setPosition(to, tmp);
        return messesUp;
    }
}
