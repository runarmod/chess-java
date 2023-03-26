package sjakk.utils;

import java.io.InputStream;
import java.util.Scanner;

import sjakk.ChessBoard;
import sjakk.PieceColor;
import sjakk.Position;
import sjakk.pieces.Piece;

public class FENParser {
    private InputStream stream;

    public FENParser() {

    }

    public FENParser(InputStream stream) {
        this.stream = stream;
    }

    public ChessBoard readFENFromStream() {
        if (stream == null) {
            throw new NullPointerException("Stream is null");
        }
        Scanner scanner = new Scanner(stream);
        ChessBoard board = readFEN(scanner.nextLine());
        System.out.println(board);
        scanner.close();
        return board;
    }

    public ChessBoard readFEN(String input) {
        ChessBoard board = new ChessBoard();
        String[] data = input.split(" ");
        String[] rows = data[0].split("/");

        for (int i = 0; i < 8; i++) {
            String row = rows[i];

            int j = 0;
            while (j < 8) {
                char c = row.charAt(j);
                if (Character.isDigit(c)) {
                    j += Character.getNumericValue(c);
                } else {
                    Piece.getPiece(c, new Position(j, 7 - i), board);
                    j++;
                }
            }
        }

        board.setTurn(data[1].equals("w") ? PieceColor.WHITE : PieceColor.BLACK);

        // TODO: data[2] castling rights

        // board.setLastPieceMoved(data[3].equals("-") ? null :
        // Piece.getPiece(data[3].charAt(0), new Position(0, 0), board));

        return board;
    }
}
