import UserMessages.JoinPlayerMessage;
import UserMessages.LeaveGameMessage;
import UserMessages.MakeMoveMessage;
import UserMessages.ResignMessage;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chessCode.ChessBoardLayout;
import chessCode.ChessSquare;
import chessCode.GameInstance;
import chessCode.Move;
import com.google.gson.Gson;
import models.Game;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class Gameplay {

    static ChessGame.TeamColor color;

    static GameInstance game;

    int gameID;

    private String authToken;

    private WSClient webSocket;

    static String username;

    public void play(int gameID, String username, ChessGame.TeamColor color, String authToken) throws Exception {
        Gameplay.color = color;
        this.authToken = authToken;
        Gameplay.username = username;
        this.gameID = gameID;
        webSocket = new WSClient();
        Scanner scanner = new Scanner(System.in);


        JoinPlayerMessage joinRequest = new JoinPlayerMessage(this.authToken, gameID, color);
        webSocket.send(new Gson().toJson(joinRequest));

        while (true) {
            try {
                System.out.print(SET_TEXT_COLOR_WHITE);
                System.out.print("[In Game as " + username + "] >>> ");
                String line = scanner.nextLine();
                String[] tokens = line.split(" ");

                if (Objects.equals(tokens[0], "redraw")) {
                    redraw();
                } else if (Objects.equals(tokens[0], "leave")) {
                    leave();
                    break;
                } else if (Objects.equals(tokens[0], "legal")) {
                    highlightMoves(tokens);
                } else if (Objects.equals(tokens[0], "resign")) {
                    resign();
                    break;
                } else if (Objects.equals(tokens[0], "move")) {
                    makeMove(tokens);
                } else if (Objects.equals(tokens[0], "help")) {
                    help();
                }else {
                    System.out.print(SET_TEXT_COLOR_RED);
                    System.out.println("Option Unavailable: " + tokens[0]);
                }
            }
            catch (Exception ex) {
                System.out.print(SET_TEXT_COLOR_RED);
                System.out.println(ex.toString());
            }
        }

    }

    private void help() {
        System.out.print(SET_TEXT_COLOR_MAGENTA);
        System.out.print("\tleave");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print(" - the game\n");
        System.out.print(SET_TEXT_COLOR_MAGENTA);
        System.out.print("\tredraw");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print(" - the chessboard\n");
        System.out.print(SET_TEXT_COLOR_MAGENTA);
        System.out.print("\tlegal <column|row>");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print(" - show legal moves");
        System.out.print(SET_TEXT_COLOR_GREEN);
        System.out.print(" - legal e2\n");
        System.out.print(SET_TEXT_COLOR_MAGENTA);
        System.out.print("\tresign");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print(" - from game\n");
        System.out.print(SET_TEXT_COLOR_MAGENTA);
        System.out.print("\thelp\n");
        System.out.print("\tmove <startColumn|startRow> <endColumn|endRow> <promotionPieceLetter>");
        System.out.print(SET_TEXT_COLOR_GREEN);
        System.out.print(" - move e2 e4 - move e7 e8 q \n");
        System.out.print("\t\t- Omit promotion piece letter unless promoting\n");

    }
    private void makeMove(String [] tokens) throws Exception {
        if (tokens.length < 3 || tokens.length > 4) {
            throw new Exception("Invalid Command");
        }
        String startSquare = tokens[1];
        String endSquare = tokens[2];
        if (startSquare.length() != 2) {
            throw new Exception("Invalid Command");
        }
        else if (endSquare.length() != 2) {
            throw new Exception("Invalid Command");
        }

        int startColumn = startSquare.charAt(0) - 'a' + 1;
        int startRow = startSquare.charAt(1) - 48;
        int endColumn = endSquare.charAt(0) - 'a' + 1;
        int endRow = endSquare.charAt(1) - 48;

        checkIndices(startColumn);
        checkIndices(startRow);
        checkIndices(endColumn);
        checkIndices(endRow);

        ChessPiece.PieceType promotionType = getPromotionType(tokens);

        ChessSquare startPosition = new ChessSquare(startRow, startColumn);
        ChessSquare endPosition = new ChessSquare(endRow, endColumn);
        Move move = new Move(startPosition, endPosition, promotionType);

        MakeMoveMessage makeMoveMessage = new MakeMoveMessage(authToken, move, gameID);
        webSocket.send(new Gson().toJson(makeMoveMessage));
    }

    public static void outputPrompt() {
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.print("[In Game as " + username + "] >>> ");
    }
    private static ChessPiece.PieceType getPromotionType(String[] tokens) throws Exception {
        ChessPiece.PieceType promotionType;
        if (tokens.length == 4) {
            switch (tokens[3]) {
                case "q" -> promotionType = ChessPiece.PieceType.QUEEN;
                case "r" -> promotionType = ChessPiece.PieceType.ROOK;
                case "b" -> promotionType = ChessPiece.PieceType.BISHOP;
                case "n" -> promotionType = ChessPiece.PieceType.KNIGHT;
                default -> throw new Exception("Invalid promotion type");
            }

        }
        else {
            promotionType = null;
        }
        return promotionType;
    }

    private void checkIndices (int index) throws Exception {
        if ((index > 8) || (index < 1)) {
            throw new Exception("Chosen index out of range");
        }
        return;
    }

    public void resign() throws Exception {
        if (color == null) {
            throw new Exception("Observers cannot resign");
        }
        ResignMessage resignMessage = new ResignMessage(authToken, gameID);
        webSocket.send(new Gson().toJson(resignMessage));
    }
    public void leave() throws Exception {
        LeaveGameMessage leaveGameMessage = new LeaveGameMessage(authToken, gameID);
        webSocket.send(new Gson().toJson(leaveGameMessage));
    }
    public static void updateGame(GameInstance game) {
        Gameplay.game = game;
        redraw();
    }
    static public void redraw() {
        if (color == ChessGame.TeamColor.BLACK) {
            drawBoardBlack(game.getBoard(), false, null, null);
        }
        else {
            drawBoardWhite(game.getBoard(), false, null, null);
        }
        System.out.print(SET_TEXT_COLOR_WHITE);
    }
    public void highlightMoves(String [] tokens) throws Exception {
        if (tokens.length != 2) {
            throw new Exception("Invalid Syntax");
        }

        int column = tokens[1].charAt(0) - 'a' + 1;
        int row = tokens[1].charAt(1) - 48;

        checkIndices(row);
        checkIndices(column);
        ChessSquare square = new ChessSquare(row, column);
        ChessGame.TeamColor turnColor = game.getTeamTurn();
        ChessPiece piece = game.getBoard().getPiece(square);
        if (piece.getTeamColor() != turnColor) {
            throw new Exception("Can only show moves for current color's turn");
        }

        ArrayList<ChessMove> moves = (ArrayList<ChessMove>) game.validMoves(square);

        if (color == WHITE || color == null) {
            drawBoardWhite(game.getBoard(), true, moves, square);
        }
        else {
            drawBoardBlack(game.getBoard(), true, moves, square);
        }
    }

    private static void chooseBackgroundColor(int row, int column, ArrayList <ChessMove> validMoves, boolean dark, ChessSquare startPosition) {
        //Is starting space
        if (startPosition.getRow() == row && startPosition.getColumn() == column) {
            System.out.print(SET_BG_COLOR_YELLOW);
            return;
        }

        //For potential valid moves
        for (ChessMove move : validMoves) {
            if (move.getEndPosition().getRow() == row && move.getEndPosition().getColumn() == column) {
                if (dark) {
                    System.out.print(SET_BG_COLOR_DARK_GREEN);
                }
                else {
                    System.out.print(SET_BG_COLOR_GREEN);
                }
                return;
            }
        }
        //If no valid move fits and is not starting space
        if (dark) {
            System.out.print(SET_BG_COLOR_BLACK);
        }
        else {
            System.out.print(SET_BG_COLOR_WHITE);
        }
    }

    private static void drawBoardWhite(ChessBoardLayout board, boolean highlight, ArrayList<ChessMove> validMoves, ChessSquare startPosition) {
        System.out.print("\n");
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {

                //Background Colors
                if (row == 0 || row == 9 || col == 0 || col == 9) {
                    System.out.print(SET_BG_COLOR_LIGHT_GREY);
                }
                else if (((row + col) % 2) == 0) {
                    if (highlight) {
                        chooseBackgroundColor(9 - row, col, validMoves, false, startPosition);
                    }
                    else {
                        System.out.print(SET_BG_COLOR_WHITE);
                    }
                }
                else {
                    if (highlight) {
                        chooseBackgroundColor(9 - row, col, validMoves, true, startPosition);
                    }
                    else {
                        System.out.print(SET_BG_COLOR_BLACK);
                    }
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

                    if (piece.getTeamColor() == WHITE) {
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
                        System.out.print(" N ");
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

    private static void drawBoardBlack(ChessBoardLayout board, boolean highlight, ArrayList<ChessMove> validMoves, ChessSquare startPosition) {
        System.out.print("\n");
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {

                //Background Colors
                if (row == 0 || row == 9 || col == 0 || col == 9) {
                    System.out.print(SET_BG_COLOR_LIGHT_GREY);
                }
                else if (((row + col) % 2) == 0) {
                    if (highlight) {
                        chooseBackgroundColor(row, 9 - col, validMoves, false, startPosition);
                    } else {
                        System.out.print(SET_BG_COLOR_WHITE);
                    }
                }
                else {
                    if (highlight) {
                        chooseBackgroundColor(row, 9 - col, validMoves, true, startPosition);
                    } else {
                        System.out.print(SET_BG_COLOR_BLACK);
                    }
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

                    if (piece.getTeamColor() == WHITE) {
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
                        System.out.print(" N ");
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
