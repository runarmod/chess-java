package sjakk.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import sjakk.ChessBoard;
import sjakk.PieceColor;
import sjakk.Player;
import sjakk.Position;
import sjakk.pieces.Piece;

public class FENParser {
    private InputStream stream;

    public FENParser() {

    }

    public FENParser(InputStream stream) {
        this.stream = stream;
    }

    public ChessBoard readFENFromStream() throws IllegalFENException {
        if (stream == null) {
            throw new NullPointerException("Couldn't find file");
        }
        Scanner scanner = new Scanner(stream);
        ChessBoard board = readFEN(scanner.nextLine());
        System.out.println(board);
        scanner.close();
        return board;
    }

    public ChessBoard readFEN(String input) throws IllegalFENException {
        Player white = new Player(PieceColor.WHITE);
        Player black = new Player(PieceColor.BLACK);
        ChessBoard board = new ChessBoard(white, black);
        String[] data = input.split(" ");
        if (data.length != 6) {
            throw new IllegalFENException("Invalid FEN string");
        }
        String[] rows = data[0].split("/");
        if (rows.length != 8) {
            throw new IllegalFENException("Invalid FEN string");
        }

        for (int i = 0; i < 8; i++) {
            String row = rows[i];

            int stringIndex = 0;
            int boardIndex = 0;
            while (boardIndex < 8) {
                char pieceCharacter = row.charAt(stringIndex);
                if (Character.isDigit(pieceCharacter)) {
                    boardIndex += Character.getNumericValue(pieceCharacter);
                } else {
                    Player player = Character.isUpperCase(pieceCharacter) ? white : black;
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
        for (char c : data[2].toCharArray()) {
            switch (c) {
                case 'K':
                    board.setWhiteKingSideCastle(true);
                    break;
                case 'Q':
                    board.setWhiteQueenSideCastle(true);
                    break;
                case 'k':
                    board.setBlackKingSideCastle(true);
                    break;
                case 'q':
                    board.setBlackQueenSideCastle(true);
                    break;
            }
        }

        // TODO: possible en passant target
        // board.setLastPieceMoved(data[3].equals("-") ? null :
        // Piece.getPiece(data[3].charAt(0), new Position(0, 0), board));

        // TODO: halfmoves
        board.setHalfMoves(Integer.parseInt(data[4]));

        // TODO: fullmoves
        board.setFullMoves(Integer.parseInt(data[5]));

        return board;
    }

    public ChessBoard useDefaultFEN() {
        try {
            return readFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        } catch (IllegalFENException e) {
            // Wont happen since the above string is valid
            return null;
        }
    }

    private static String shuffleString(String string) {
        StringBuilder builder = new StringBuilder(string.length());
        List<Character> chars = new ArrayList<Character>();
        for (char c : string.toCharArray()) {
            chars.add(c);
        }
        while (chars.size() != 0) {
            int randIndex = (int) (Math.random() * chars.size());
            builder.append(chars.remove(randIndex));
        }
        return builder.toString();
    }

    private static boolean legalFirstRow(String string) {
        int king = string.indexOf("k");
        int bishop1 = string.indexOf("b");
        int bishop2 = string.lastIndexOf("b");
        int rook1 = string.indexOf("r");
        int rook2 = string.lastIndexOf("r");

        if ((bishop1 + bishop2) % 2 != 1) {
            return false;
        }

        if (!(rook1 < king && king < rook2)) {
            return false;
        }

        // Make sure we have the right count of every piece
        Map<Character, Integer> row = Map.of('r', 2, 'n', 2, 'b', 2, 'q', 1, 'k', 1);
        for (Character c : string.toCharArray()) {
            if (string.chars().filter(e -> e == c).count() != row.get(c)) {
                return false;
            }
        }
        return true;
    }

    public static String generateFischerRandomFEN() {
        final String defaultPlacement = "rnbqkbnr";
        String blackSide = defaultPlacement;

        // Have a max-shuffle-count in case the shuffle method somehow doesn't create a
        // kegak string for a "long" time
        int i = 0;
        while (i++ < 1000) {
            blackSide = shuffleString(blackSide);
            if (legalFirstRow(blackSide))
                break;
        }

        if (i >= 1000)
            blackSide = defaultPlacement;

        String whiteSide = blackSide.toUpperCase();
        String FENString = blackSide + "/pppppppp/8/8/8/8/PPPPPPPP/" + whiteSide + " w - - 0 1";
        return FENString;
    }

    public static String generateFEN(ChessBoard board) {
        StringBuilder FENString = new StringBuilder();

        // First part of the FEN string
        int emptySpaces = 0;
        for (int y = 7; y >= 0; y--) {
            for (int x = 0; x < 8; x++) {
                Piece piece = board.getPosition(new Position(x, y));
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

        System.out.println(FENString);
        return FENString.toString();
    }
}
