package sjakk.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import sjakk.ChessBoard;
import sjakk.PieceColor;
import sjakk.Position;
import sjakk.pieces.Piece;

public class FENParser {
    private File file;

    public FENParser() {

    }

    public FENParser(File file) {
        this.file = file;
    }

    public ChessBoard readFEN() throws FileNotFoundException {
        if (file == null) {
            return null;
        }
        Scanner scanner = new Scanner(file);
        ChessBoard board = readFEN(scanner.nextLine());
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

    public static void main(String[] args) {
        FENParser parser = new FENParser(null);
        ChessBoard board = parser.readFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        System.out.println(board);
    }
}
