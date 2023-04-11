package sjakk.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
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
import sjakk.pieces.Piece;
import sjakk.utils.FENParser;
import sjakk.utils.IllegalFENException;
import sjakk.utils.PopUp;

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

    public ChessGameController(String FENString) {
        this.FENString = FENString;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        resetGridBackgroundMatrix();
        handleRestartGame();
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

    @FXML
    private void handleRestartGame() {
        try {
            if (FENString.length() > 0) {
                chessboard = FENParser.getBoardFromFEN(FENString);
                System.out.println("Read FEN from string");
            } else {
                String sep = System.getProperty("file.separator");
                File inFile = new File(System.getProperty("user.dir") + sep + "games" + sep + "defaultStart.fen");
                String FENString = FENParser.readFENFromFile(inFile);
                chessboard = FENParser.getBoardFromFEN(FENString);

                System.out.println("Read FEN from file");
            }
        } catch (NullPointerException | IllegalFENException | FileNotFoundException e) {
            chessboard = FENParser.getBoardFromDefaultFEN();
            System.out.println("Could not read FEN, using default");
            System.out.println(e.getMessage());
        }
        allPiecesImg = new Image(ChessBoard.class.getResource("pieces.png").toString());
        drawBoard();
    }

    @FXML
    private void handleGridPaneClicked(MouseEvent event) {
        positionPressed(new Position(event));
        drawBoard();
        showGameOver();
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
    }

    private Piece getSelectedPiece() {
        return chessboard.getSelectedPiece();
    }

    private void resetGridBackgroundMatrix() {
        gridBackgroundColor = new ArrayList<ArrayList<Color>>();
        for (int i = 0; i < 8; i++) {
            gridBackgroundColor.add(new ArrayList<Color>());
            for (int j = 0; j < 8; j++) {
                gridBackgroundColor.get(i).add((i + j) % 2 == 0 ? Color.GRAY : Color.WHITE);
            }
        }
    }

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

    @FXML
    private void drawBoard() {
        placePieces();
        regenerateBackgroundBoard();
        colorBackgrounds();
        updateMoves();
    }

    private void placePieces() {
        Node gridLines = grid.getChildren().get(0);
        grid.getChildren().clear();
        grid.getChildren().add(0, gridLines);
        for (Piece piece : chessboard) {
            grid.add(getImage(piece), piece.getX(), 7 - piece.getY());
        }
    }

    private ImageView getImage(Piece piece) {
        final char pieceChar = Character.toLowerCase(piece.toChar());
        return cropPieceImage(pieceChar, piece.isWhite());
    }

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

    private void colorBackgrounds() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ((Rectangle) backgroundBoard.getChildren().get((7 - i) * 8 + j))
                        .setFill(getGridBackgroundColor(j, i));
            }
        }
    }

    private void updateMoves() {
        moves.setText("    WHITE | BLACK\n" + chessboard.getMoves());
    }

    @FXML
    private void toTitleScreen() {
        insertPane("TitleScreen.fxml", baseAnchor, new TitleController());
    }

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
     * Get the image containing all the pieces.
     * 
     * @return the image containing all the pieces
     */
    public static Image getAllPiecesImg() {
        return allPiecesImg;
    }

    /**
     * Get the coloumn-index of the image containing all the pieces.
     */
    public static int getPieceImageIndex(Character pieceType) {
        return imgColIdx.get(pieceType);
    }

    public Color getGridBackgroundColor(int x, int y) {
        return gridBackgroundColor.get(y).get(x);
    }

    public void setGridBackgroundColor(int x, int y, Color color) {
        gridBackgroundColor.get(y).set(x, color);
    }

    /**
     * Sets the background of all legal moves to light green.
     */
    public void setLegalMovesBackground(Piece piece) {
        for (Position pos : piece.getLegalMoves()) {
            setGridBackgroundColor(pos.getX(), pos.getY(), Color.LIGHTGREEN);
        }
    }
}
