import UserMessages.JoinPlayer;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chessCode.AChessBoard;
import chessCode.ChessSquare;
import models.Game;
import com.google.gson.Gson;
import java.util.Objects;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Gameplay {

    static ChessGame.TeamColor color;

    static AChessBoard board;

    String authToken;

    public void play(int gameID, String username, ChessGame.TeamColor color, String authToken) throws Exception {
        Gameplay.color = color;
        this.authToken = authToken;
        WSClient webSocket = new WSClient();
        Scanner scanner = new Scanner(System.in);


        JoinPlayer joinRequest = new JoinPlayer(this.authToken, gameID, color);
        webSocket.send(new Gson().toJson(joinRequest));

        System.out.print("[In Game as " + username + "] >>> ");
        while (true) {
            String line = scanner.nextLine();
            String [] tokens = line.split(" ");

            if (Objects.equals(tokens[0], "redraw")) {
                redraw();
            }
            else if (Objects.equals(tokens[0], "leave")) {
                break;
            }
            else if (Objects.equals(tokens[0], "moves")) {
                highlightMoves();
            }
        }
    }


    public void highlightMoves() {

    }

    static void updateBoard(AChessBoard board) {
        System.out.println("Updating board");
        Gameplay.board = board;
        redraw();
    }
    static public void redraw() {
        if (color == ChessGame.TeamColor.BLACK) {
            drawBoardBlack(board);
        }
        else {
            drawBoardWhite(board);
        }
    }


    private static void drawBoardWhite(AChessBoard board) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {

                //Background Colors
                if (row == 0 || row == 9 || col == 0 || col == 9) {
                    System.out.print(SET_BG_COLOR_LIGHT_GREY);
                }
                else if (((row + col) % 2) == 0) {
                    System.out.print(SET_BG_COLOR_WHITE);
                }
                else {
                    System.out.print(SET_BG_COLOR_BLACK);
                }

                //Border Letters
                if (((col == 0) || (col == 9)) && (row > 0 ) && (row < 9)) {
                    System.out.print(SET_TEXT_COLOR_BLACK);
                    System.out.printf(" %d ", 9 - row);
                    continue;
                }
                else if (((row == 0) || (row == 9)) && (col > 0) && (col < 9)){
                    System.out.print(SET_TEXT_COLOR_BLACK);
                    System.out.printf(" %c ", col + 64);
                    continue;
                }
                else if (row == 0 || col == 0 || row == 9 || col == 9) {
                    System.out.print("   ");
                    continue;
                }

                //Draw Pieces
                if ((row > 0) && (col > 0) && (row < 9) && (col < 9)) {
                    ChessSquare square = new ChessSquare(9 - row, col);
                    ChessPiece piece = board.getPiece(square);

                    if (piece == null) {
                        System.out.print("   ");
                        continue;
                    }

                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        System.out.print(SET_TEXT_COLOR_RED);
                    }
                    else {
                        System.out.print(SET_TEXT_COLOR_BLUE);
                    }


                    if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                        System.out.print(" K ");
                    }
                    else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                        System.out.print(" Q ");
                    }
                    else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                        System.out.print(" R ");
                    }
                    else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                        System.out.print(" B ");
                    }
                    else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                        System.out.print(" N ");;
                    }
                    else if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                        System.out.print(" P ");
                    }
                }


            }
            System.out.print(RESET_BG_COLOR);
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    private static void drawBoardBlack(AChessBoard board) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {

                //Background Colors
                if (row == 0 || row == 9 || col == 0 || col == 9) {
                    System.out.print(SET_BG_COLOR_LIGHT_GREY);
                }
                else if (((row + col) % 2) == 0) {
                    System.out.print(SET_BG_COLOR_WHITE);
                }
                else {
                    System.out.print(SET_BG_COLOR_BLACK);
                }

                //Border Letters
                if (((col == 0) || (col == 9)) && (row > 0 ) && (row < 9)) {
                    System.out.print(SET_TEXT_COLOR_BLACK);
                    System.out.printf(" %d ", row);
                    continue;
                }
                else if (((row == 0) || (row == 9)) && (col > 0) && (col < 9)){
                    System.out.print(SET_TEXT_COLOR_BLACK);
                    System.out.printf(" %c ", 73 - col);
                    continue;
                }
                else if (row == 0 || col == 0 || row == 9 || col == 9) {
                    System.out.print("   ");
                    continue;
                }

                //Draw Pieces
                if ((row > 0) && (col > 0) && (row < 9) && (col < 9)) {
                    ChessSquare square = new ChessSquare(row, 9 - col);
                    ChessPiece piece = board.getPiece(square);

                    if (piece == null) {
                        System.out.print("   ");
                        continue;
                    }

                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        System.out.print(SET_TEXT_COLOR_RED);
                    }
                    else {
                        System.out.print(SET_TEXT_COLOR_BLUE);
                    }


                    if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                        System.out.print(" K ");
                    }
                    else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                        System.out.print(" Q ");
                    }
                    else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                        System.out.print(" R ");
                    }
                    else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                        System.out.print(" B ");
                    }
                    else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                        System.out.print(" N ");;
                    }
                    else if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                        System.out.print(" P ");
                    }
                }


            }
            System.out.print(RESET_BG_COLOR);
            System.out.print("\n");
        }
        System.out.print("\n");
    }


}
