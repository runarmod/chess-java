package sjakk.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import sjakk.ChessBoard;
import sjakk.PieceColor;
import sjakk.Player;
import sjakk.Position;
import sjakk.pieces.Piece;

/**
 * A utility class for saving, loading, parsing, generating FEN strings to and
 * from files and {@link ChessBoard}s. The FEN format is described here: <a
 * href=https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation>Forsyth–Edwards
 * Notation</a>. All methods in this class are static, and it is not possible to
 * create an instance FENparser.
 */
public abstract class FENParser {
    /**
     * The file-extention for fen-files.
     */
    public static final String FEN_EXTENSION = "fen";

    /**
     * The default FEN string.
     */
    public static final String DEFAULT_STRING = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    /**
     * Read a FEN string from a file.
     * 
     * @param file The file to read from.
     * @return The FEN string.
     * @throws FileNotFoundException If the file does not exist.
     */
    public static String readFENFromFile(File file) throws FileNotFoundException {
        String FENString = "";
        final Scanner scanner = new Scanner(file);
        FENString = scanner.nextLine();
        scanner.close();
        return FENString;
    }

    /**
     * Get a {@link ChessBoard} from a FEN string.
     * 
     * @param input The FEN string.
     * @return The {@link ChessBoard} represented by the FEN string.
     * @throws IllegalFENException If the FEN string is invalid.
     */
    public static ChessBoard getBoardFromFEN(String input) throws IllegalFENException {
        final Player white = new Player(PieceColor.WHITE);
        final Player black = new Player(PieceColor.BLACK);
        final ChessBoard board = new ChessBoard(white, black);
        final String[] data = input.split(" ");
        if (data.length != 6) {
            throw new IllegalFENException("Invalid FEN string");
        }
        final String[] rows = data[0].split("/");
        if (rows.length != 8) {
            throw new IllegalFENException("Invalid FEN string");
        }

        for (int i = 0; i < 8; i++) {
            final String row = rows[i];

            int stringIndex = 0;
            int boardIndex = 0;
            while (boardIndex < 8) {
                final char pieceCharacter = row.charAt(stringIndex);
                if (Character.isDigit(pieceCharacter)) {
                    boardIndex += Character.getNumericValue(pieceCharacter);
                } else {
                    final Player player = Character.isUpperCase(pieceCharacter) ? white : black;
                    Piece.placePiece(player, new Position(boardIndex, 7 - i), board, pieceCharacter);
                    boardIndex++;
                }
                stringIndex++;
            }
        }

        // Starting player
        board.setTurn(data[1].equals("w") ? PieceColor.WHITE : PieceColor.BLACK);

        // Castling rights
        board.disableCastling();
        for (final char c : data[2].toCharArray()) {
            switch (c) {
                case 'K':
                    white.setCastling(true, false);
                    break;
                case 'Q':
                    white.setCastling(true, true);
                    break;
                case 'k':
                    black.setCastling(true, false);
                    break;
                case 'q':
                    black.setCastling(true, true);
                    break;
            }
        }

        // TODO: possible en passant target
        // board.setLastPieceMoved(data[3].equals("-") ? null :
        // Piece.getPiece(data[3].charAt(0), new Position(0, 0), board));

        board.setHalfMoves(Integer.parseInt(data[4]));

        board.setFullMoves(Integer.parseInt(data[5]));

        return board;
    }

    /**
     * Get a the default {@link ChessBoard} (retreived using the default fen-string)
     * 
     * @return The default {@link ChessBoard}.
     */
    public static ChessBoard getBoardFromDefaultFEN() {
        try {
            return getBoardFromFEN(DEFAULT_STRING);
        } catch (final IllegalFENException e) {
            // Wont happen since the above string is valid
            return null;
        }
    }

    /**
     * Returns whether or not a FENString is legal for a Fischer random position.
     * 
     * @param FENString The FENString to check.
     * @return Whether or not the FENString is legal for a Fischer random position.
     */
    public static boolean legalFischerRandom(String FENString) {
        if (FENString.split("/").length != 8) {
            return false;
        }

        final String whiteSide = FENString.split(" ")[0].split("/")[7];
        final String blackSide = FENString.split("/")[0];

        if (!legalFirstRowFischerRandom(blackSide))
            return false;

        if (!legalFirstRowFischerRandom(whiteSide.toLowerCase()))
            return false;

        return blackSide.equals(whiteSide.toLowerCase());
    }

    /**
     * Generates a FEN-string for a Fischer random position.
     * 
     * @return the random FEN-string.
     */
    public static String generateFischerRandomFEN() {
        final String defaultPlacement = "rnbqkbnr";
        String blackSide = defaultPlacement;

        // Have a max-shuffle-count in case the shuffle method somehow doesn't create a
        // legal string for a "long" time
        int i = 0;
        while (i++ < 1000) {
            blackSide = shuffleString(blackSide);
            if (legalFirstRowFischerRandom(blackSide))
                break;
        }

        // If we didn't find a legal string in 1000 tries, just use the default.
        // Microscopic chance of this happening.
        if (i >= 1000)
            blackSide = defaultPlacement;

        final String whiteSide = blackSide.toUpperCase();
        final String FENString = blackSide + "/pppppppp/8/8/8/8/PPPPPPPP/" + whiteSide + " w - - 0 1";
        return FENString;
    }

    /**
     * Generates the FEN-string for a given {@link ChessBoard}.
     * 
     * @param board the {@link ChessBoard} to generate the FEN-string for.
     * @return the generated FENString.
     */
    public static String generateFEN(ChessBoard board) {
        final StringBuilder FENString = new StringBuilder();

        // First part of the FEN string
        int emptySpaces = 0;
        for (int y = 7; y >= 0; y--) {
            for (int x = 0; x < 8; x++) {
                final Piece piece = board.getPosition(new Position(x, y));
                if (piece == null) {
                    emptySpaces++;
                    continue;
                }
                if (emptySpaces != 0) {
                    FENString.append(Integer.toString(emptySpaces));
                    emptySpaces = 0;
                }
                FENString.append(piece.toChar());
            }
            if (emptySpaces != 0) {
                FENString.append(Integer.toString(emptySpaces));
                emptySpaces = 0;
            }
            if (y != 0)
                FENString.append('/');
        }

        // Whos turn?
        FENString.append(" " + board.getPlayerTurn().toChar());

        // What can be castled?
        FENString.append(" " + board.getCastlingRights());

        // TODO: Can there be made en passant? Where?
        FENString.append(" -");

        // Halfmoves
        FENString.append(" " + board.getHalfMoves());

        // Fullmoves
        FENString.append(" " + board.getFullMoves());

        return FENString.toString();
    }

    /**
     * Saves the given FEN string to a file. If no file location is given, a file
     * chooser UI will be shown.
     * 
     * @param content  The FEN string to save
     * @param location The location to save the FEN string to. If null, a file
     *                 chooser UI will be shown.
     */
    public static void saveToFile(String content, File location) {
        File file = location;
        if (file == null) {
            final File defaultSaveDirectory = new File(
                    System.getProperty("user.dir") + System.getProperty("file.separator") + "games");
            final JFileChooser chooser = getValidChooser(defaultSaveDirectory);

            final FileNameExtensionFilter filter = new FileNameExtensionFilter("FEN files", FEN_EXTENSION);
            chooser.setFileFilter(filter);
            chooser.setDialogTitle("Save State");
            chooser.setSelectedFile(new File("mygame." + FEN_EXTENSION));

            final int returnState = chooser.showSaveDialog(null);
            if (returnState == JFileChooser.APPROVE_OPTION) {
                file = chooser.getSelectedFile();
            }
        }

        // Did not select file
        if (file == null)
            return;

        if (!file.getName().endsWith("." + FEN_EXTENSION)) {
            file = new File(file.getAbsolutePath() + "." + FEN_EXTENSION);
        }

        try {
            final FileOutputStream fw = new FileOutputStream(file);
            fw.write(content.getBytes());
            fw.close();
        } catch (final Exception e) {
        }
    }

    /**
     * Saves the given FEN string to a file. A file chooser UI will be shown.
     * 
     * @param content The FEN string to save
     * @see #saveToFile(String, File)
     */
    public static void saveToFile(String content) {
        saveToFile(content, null);
    }

    /**
     * Get a file from a file chooser UI.
     * 
     * @return the selected file, or null if no file was selected.
     */
    public static File getFileFromChooser() {
        final File defaultDirectory = new File(
                System.getProperty("user.dir") + System.getProperty("file.separator") + "games");
        final JFileChooser chooser = new JFileChooser(defaultDirectory);

        final FileNameExtensionFilter filter = new FileNameExtensionFilter("FEN files", FEN_EXTENSION);
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Load State");

        final int returnState = chooser.showOpenDialog(null);
        if (returnState == JFileChooser.APPROVE_OPTION) {
            final File file = chooser.getSelectedFile();
            return file;
        }

        return null;
    }

    /**
     * Shuffles a string using the Fisher-Yates algorithm. This is used internally
     * by the parser to generate a Fischer random position.
     * 
     * @param string The string to shuffle.
     * @return The shuffled string.
     */
    private static String shuffleString(String string) {
        final StringBuilder builder = new StringBuilder(string.length());
        final List<Character> chars = new ArrayList<Character>();
        for (final char c : string.toCharArray()) {
            chars.add(c);
        }
        while (chars.size() != 0) {
            final int randIndex = (int) (Math.random() * chars.size());
            builder.append(chars.remove(randIndex));
        }
        return builder.toString();
    }

    /**
     * Returns whether or not the first part of a FENString (the board-part) is
     * legal for a Fischer random position.
     * 
     * @param string The FENString to check.
     * @return Whether or not the first part of the FENString is legal for a Fischer
     */
    private static boolean legalFirstRowFischerRandom(String string) {
        final int king = string.indexOf("k");
        final int bishop1 = string.indexOf("b");
        final int bishop2 = string.lastIndexOf("b");
        final int rook1 = string.indexOf("r");
        final int rook2 = string.lastIndexOf("r");

        // Bishops must be on different colors. Same as that the sum of the two
        // x-positions of the bishops must be an odd number.
        if ((bishop1 + bishop2) % 2 != 1) {
            return false;
        }

        // King must be between the two rooks
        if (!(rook1 < king && king < rook2)) {
            return false;
        }

        // Make sure we have the right count of every piece
        final Map<Character, Integer> row = Map.of('r', 2, 'n', 2, 'b', 2, 'q', 1, 'k', 1);
        for (final Character c : string.toCharArray()) {
            if (string.chars().filter(e -> e == c).count() != row.get(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get a valid {@link JFileChooser}, with correct settings already set, for
     * saving a FEN file. This makes sure a confirm dialog is shown if user tries to
     * overwrite a file.
     * 
     * @param defaultSaveDirectory the directory to start the file chooser in.
     * @return a valid {@link JFileChooser}.
     */
    private static JFileChooser getValidChooser(File defaultSaveDirectory) {
        final JFileChooser chooser = new JFileChooser(defaultSaveDirectory) {
            @Override
            public void approveSelection() {
                final File file = getSelectedFile();

                if (file.exists() && getDialogType() == SAVE_DIALOG) {
                    final int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?",
                            "Existing file", JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION)
                        super.approveSelection();

                    return;
                }
                super.approveSelection();
            }
        };
        return chooser;
    }
}
