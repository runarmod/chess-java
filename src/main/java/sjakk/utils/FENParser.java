package sjakk.utils;

import java.io.InputStream;
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

        // board.setLastPieceMoved(data[3].equals("-") ? null :
        // Piece.getPiece(data[3].charAt(0), new Position(0, 0), board));

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
}
