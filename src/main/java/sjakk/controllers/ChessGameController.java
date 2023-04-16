package sjakk.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sjakk.ChessBoard;
import sjakk.Position;
import sjakk.pieces.*;
import sjakk.utils.FENParser;
import sjakk.utils.IllegalFENException;
import sjakk.utils.PopUp;

/**
 * This class is the controller for the chess game scene. It handles the chess
 * game scene, including the images for the pieces, all keypresses, restarting
 * the game, switching to the title scene and exporting position.
 * 
 * @see SceneSwitcher
 */
public class ChessGameController extends SceneSwitcher {
    @FXML
    private TextArea moves;

    @FXML
    private GridPane grid, backgroundBoard;

    @FXML
    private Text player1time, player2time;

    private ChessBoard chessboard;
    private String FENString = "";
    private boolean hasShownGameOver = false;
    private ArrayList<ArrayList<Color>> gridBackgroundColor;

    private static Image allPiecesImg;
    private final static Map<Character, Integer> imgColIdx = Map.of('k', 0, 'q', 1, 'b', 2, 'n', 3, 'r', 4, 'p', 5);

    /**
     * This constructor is used when the user wants to play a new game with default
     * setup.
     */
    public ChessGameController() {
        this.FENString = FENParser.DEFAULT_STRING;
    }

    /**
     * This constructor is used when the user wants to load a game from a FEN
     * string.
     * 
     * @param FENString the FEN string to load the game from
     */
    public ChessGameController(String FENString) {
        this.FENString = FENString;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        resetGridBackgroundMatrix();
        handleRestartGame();
    }

    /**
     * Handles the needed logic for restarting a game, using the FEN string if it is
     * saved. If not, it will use the default FEN string.
     */
    @FXML
    private void handleRestartGame() {
        try {
            chessboard = FENParser.getBoardFromFEN(FENString);
        } catch (NullPointerException | IllegalFENException e) {
            chessboard = FENParser.getBoardFromDefaultFEN();
            PopUp popup = new PopUp("Error loading FEN", true);
            popup.addNode(new Text("Could not read FEN, using default"));
            popup.display();
        }

        hasShownGameOver = false;
        allPiecesImg = new Image(ChessBoard.class.getResource("images/pieces.png").toString());
        drawBoard();
    }

    /**
     * Handles what should happen when the user clicks on the chessboard.
     * 
     * @param event The mouse event that triggered this method.
     */
    @FXML
    private void handleGridPaneClicked(MouseEvent event) {
        positionPressed(new Position(event));
        drawBoard();
        showGameOver();
    }

    /**
     * Draws the board. This includes placing pieces, regenerating the background,
     * coloring the relevant extra tiles and updating the moves.
     */
    @FXML
    private void drawBoard() {
        placePieces();
        regenerateBackgroundBoard();
        colorBackgrounds();
        updateMoves();
    }

    /**
     * Changes screen to the title screen.
     */
    @FXML
    private void toTitleScreen() {
        insertPane("TitleScreen.fxml", baseAnchor, new TitleController());
    }

    /**
     * Export the game. A popup appears with export options. The file will contain
     * the FEN position of the game.
     */
    @FXML
    private void exportGame() {
        String position = chessboard.getFEN();
        PopUp popUp = new PopUp("Export game", true);

        TextField textField = new TextField(position);
        textField.setEditable(false);
        popUp.addNode(textField);

        Button toFileButton = new Button("Export to file");
        toFileButton.setOnAction(e -> {
            FENParser.saveToFile(position);
        });
        popUp.addNode(toFileButton);

        popUp.display();
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
    private void positionPressed(Position position) {
        if (chessboard.getGameFinished())
            return;

        resetGridBackgroundMatrix();
        if (getSelectedPiece() == null) {
            chessboard.setSelectedPiece(chessboard.getPosition(position));
            // If the selected piece is not the correct color, deselect it.
            Piece selected = getSelectedPiece();
            if (selected != null && selected.getOwner() != chessboard.getPlayerTurn()) {
                chessboard.setSelectedPiece(null);
                return;
            }
            selected = getSelectedPiece();
            if (selected != null) {
                setGridBackgroundColor(position.getX(), position.getY(), Color.YELLOW);
                setLegalMovesBackground(getSelectedPiece());
            }
            return;
        }

        if (chessboard.getPlayerTurn() != getSelectedPiece().getOwner())
            return;

        try {
            getSelectedPiece().move(position);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid move");
            chessboard.setSelectedPiece(null);
            return;
        }

        chessboard.setSelectedPiece(null);
        checkPawnPromotion();
    }

    /**
     * Checks if a pawn should be upgraded. If it should, it will be.
     */
    private void checkPawnPromotion() {
        Pawn pawn = chessboard.getUpgradablePawn();
        if (pawn != null) {
            promotePawn(pawn);
        }
    }

    /**
     * Create a popup to promote a pawn.
     * 
     * @param piece The pawn to promote.
     */
    private void promotePawn(Pawn piece) {
        Piece newPiece;
        try {
            final URL url = getClass().getResource("/sjakk/UpgradePawn.fxml");
            final FXMLLoader loader = new FXMLLoader(url);
            loader.setController(new UpgradePawnController());

            final Node node = loader.load();
            final PopUp popUp = new PopUp("Promote pawn", false);
            popUp.addNode(node);
            popUp.display();
            switch (UpgradePawnController.getUpgradeChoice()) {
                case "rook":
                    newPiece = new Rook(piece.getPos(), chessboard, piece.getOwner());
                    break;
                case "bishop":
                    newPiece = new Bishop(piece.getPos(), chessboard, piece.getOwner());
                    break;
                case "knight":
                    newPiece = new Knight(piece.getPos(), chessboard, piece.getOwner());
                    break;
                case "queen":
                default:
                    newPiece = new Queen(piece.getPos(), chessboard, piece.getOwner());
                    break;
            }

        } catch (IOException e) {
            System.out.println("Could not load fxml file, using queen as default promotion");
            newPiece = new Queen(piece.getPos(), chessboard, piece.getOwner());
        }

        chessboard.promotePawn(piece, newPiece);
    }

    /**
     * Crops the image of the piece from the image of all pieces and sets it as the
     * image of this piece.
     * 
     * @param pieceType the type of the piece (ex. "King")
     */
    private ImageView cropPieceImage(Character pieceType, boolean white) {
        ImageView img = new ImageView(getAllPiecesImg());

        int imgY = (white ? 0 : 1);
        int coloumnIndex = getPieceImageIndex(pieceType);
        final int size = 50;

        img.setViewport(new Rectangle2D(coloumnIndex * size, imgY * size, size, size));

        // Shrink to have border visible
        img.setFitWidth(48);
        img.setPreserveRatio(true);
        return img;
    }

    /**
     * Gets the selected piece from the chessboard.
     */
    private Piece getSelectedPiece() {
        return chessboard.getSelectedPiece();
    }

    /**
     * Resets the matrix used to store the backgroundcolors for the tiles to every
     * other white and gray.
     */
    private void resetGridBackgroundMatrix() {
        gridBackgroundColor = new ArrayList<ArrayList<Color>>();
        for (int i = 0; i < 8; i++) {
            gridBackgroundColor.add(new ArrayList<Color>());
            for (int j = 0; j < 8; j++) {
                gridBackgroundColor.get(i).add((i + j) % 2 == 0 ? Color.GRAY : Color.WHITE);
            }
        }
    }

    /**
     * Show a popupscreen with the game over message.
     */
    private void showGameOver() {
        if (hasShownGameOver)
            return;

        if (chessboard.getGameFinished()) {
            System.out.println(chessboard.getGameMessage());
            PopUp popUp = new PopUp("Game Over", true);
            popUp.addNode(new Text(chessboard.getGameMessage()));
            popUp.display();
            hasShownGameOver = true;
        }
    }

    /**
     * Places the pieces on the board. First clearing the entire board, then
     * replacing gridlines and then placing the pieces.
     */
    private void placePieces() {
        Node gridLines = grid.getChildren().get(0);
        grid.getChildren().clear();
        grid.getChildren().add(0, gridLines);
        for (Piece piece : chessboard) {
            grid.add(getImage(piece), piece.getX(), 7 - piece.getY());
        }
    }

    /**
     * Gets the image for a piece.
     * 
     * @param piece The piece to get the image for.
     * @return The image for the piece.
     */
    private ImageView getImage(Piece piece) {
        final char pieceChar = Character.toLowerCase(piece.toChar());
        return cropPieceImage(pieceChar, piece.isWhite());
    }

    /**
     * Regenerates the background board, the matrix with rectangle elements.
     */
    private void regenerateBackgroundBoard() {
        if (backgroundBoard.getChildren().size() != 64) {
            backgroundBoard.getChildren().clear();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    backgroundBoard.add(new Rectangle(50, 50), j, i);
                }
            }
        }
    }

    /**
     * Colors the background of the tiles.
     */
    private void colorBackgrounds() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ((Rectangle) backgroundBoard.getChildren().get((7 - i) * 8 + j))
                        .setFill(getGridBackgroundColor(j, i));
            }
        }
    }

    /**
     * Updates the moves text-section.
     */
    private void updateMoves() {
        moves.setText("    WHITE | BLACK\n" + chessboard.getMoves());
    }

    /**
     * Get the image containing all the pieces.
     * 
     * @return the image containing all the pieces
     */
    public static Image getAllPiecesImg() {
        return allPiecesImg;
    }

    /**
     * Get the coloumn-index of the image containing all the pieces.
     * 
     * @param pieceType The type of piece to get the index for.
     * @return The index of the image containing all the pieces.
     */
    public static int getPieceImageIndex(Character pieceType) {
        return imgColIdx.get(pieceType);
    }

    /**
     * Gets the backgraund color of a tile.
     * 
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     * @return The background color of the tile.
     */
    public Color getGridBackgroundColor(int x, int y) {
        return gridBackgroundColor.get(y).get(x);
    }

    /**
     * Sets the background color of a tile.
     * 
     * @param x     The x-coordinate of the tile.
     * @param y     The y-coordinate of the tile.
     * @param color The color to set the tile to.
     */
    public void setGridBackgroundColor(int x, int y, Color color) {
        gridBackgroundColor.get(y).set(x, color);
    }

    /**
     * Sets the background of all legal moves for a piece to light green.
     * 
     * @param piece The piece to set the legal moves for.
     */
    public void setLegalMovesBackground(Piece piece) {
        for (Position pos : piece.getLegalMoves()) {
            setGridBackgroundColor(pos.getX(), pos.getY(), Color.LIGHTGREEN);
        }
    }
}
