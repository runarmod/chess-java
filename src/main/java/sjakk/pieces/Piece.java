package sjakk.pieces;

import java.util.Collection;
import java.util.Map;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sjakk.ChessBoard;
import sjakk.PieceColor;
import sjakk.Position;

public abstract class Piece {
    private static final Image allPiecesImg = new Image(
            ChessBoard.class.getResource("pieces.png").toString());
    private static Map<String, Integer> imgColIdx = Map.of("King", 0, "Queen", 1, "Bishop", 2, "Knight", 3, "Rook",
            4, "Pawn", 5);

    protected Position pos;
    protected ChessBoard board;
    protected PieceColor color;
    protected ImageView imageView;

    /**
     * Creates a new piece at the given position on the given board. The pice is
     * automatically places on the board if its not already in position.
     * 
     * @param position the position of the piece
     * @param board    the board the piece is on
     * @param color    the color of the piece
     */
    public Piece(Position position, ChessBoard board, PieceColor color, String name) {
        this.pos = position;
        this.board = board;
        if (board.getPosition(position) != this) {
            board.setPosition(position, this);
        }
        this.color = color;

        cropPieceImage(name);
    }

    /**
     * Returns a collection of all legal moves for this piece.
     * 
     * @return a collection of all legal moves for this piece
     */
    public abstract Collection<Position> getLegalMoves();

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
     * @return the color of the piece
     */
    public PieceColor getColor() {
        return color;
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
    public void move(Position to) {
        if (!isValidMove(to)) {
            throw new IllegalArgumentException("Illegal move");
        }
        board.move(this, to);
    }

    /**
     * Crops the image of the piece from the image of all pieces and sets it as the
     * image of this piece.
     * 
     * @param pieceType the type of the piece (ex. "King")
     */
    private void cropPieceImage(String pieceType) {
        this.imageView = new ImageView(allPiecesImg);

        int imgY = (color == PieceColor.WHITE ? 0 : 1);
        int coloumnIndex = imgColIdx.get(pieceType);
        final int size = 50;

        this.imageView.setViewport(new Rectangle2D(coloumnIndex * size, imgY * size, size, size));

        // Shrink to have border visible
        this.imageView.setFitWidth(48);
        this.imageView.setPreserveRatio(true);
    }

}
