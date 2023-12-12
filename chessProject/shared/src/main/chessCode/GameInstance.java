package chessCode;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.KING;

public class GameInstance implements ChessGame {

    //CHANGED THIS
    private final ChessBoardLayout board;
    private TeamColor turnColor;
    private boolean gameIsActive;

    public GameInstance() {
        board = new ChessBoardLayout();
        turnColor = WHITE;
        gameIsActive = true;
    }

    public void endGame() {
        gameIsActive = false;
    }
    public boolean gameActive() {
        return gameIsActive;
    }
    @Override
    public TeamColor getTeamTurn() {
        return turnColor;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        turnColor = team;
    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new ArrayList<ChessMove>();
        Collection<ChessMove> moves;
        if (board.getPiece(startPosition) == null) {
            return validMoves;
        }

        ChessBoardLayout oldBoard = new ChessBoardLayout();
        oldBoard.copyBoard(board);
        TeamColor color = board.getPiece(startPosition).getTeamColor();
        moves = board.getPiece(startPosition).pieceMoves(board, startPosition);
        for (ChessMove move : moves) {
            board.potentialMove(move);
            if (!isInCheck(color)) {
                validMoves.add(move);
            }
            setBoard(oldBoard);
        }

        return validMoves;
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
            if (board.getPiece(move.getStartPosition()) == null) {
                throw new InvalidMoveException("Empty Square selected");
            }
            else if (board.getPiece(move.getStartPosition()).getTeamColor() != turnColor) {
                throw new InvalidMoveException("Wrong Piece Color");
            }
            Collection<ChessMove> validMoves = validMoves(move.getStartPosition());

            for (ChessMove potentialMove : validMoves) {
                if (move.equals(potentialMove)) {
                    if (board.getPiece(move.getStartPosition()).getTeamColor() == WHITE) {
                        setTeamTurn(BLACK);
                    } else {
                        setTeamTurn(WHITE);
                    }
                    board.potentialMove(move);
                    return;
                }
            }
            throw new InvalidMoveException("Invalid move");
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        // Find King
        ChessPosition kingSquare = null;
        ChessPosition newSquare;
        Collection<ChessMove> moves;
        for (int row = 1; row <= 8; row++) {
            for (int column = 1; column <= 8; column++) {
                newSquare = new ChessSquare(row, column);
                if (board.getPiece(newSquare) == null) {
                    continue;
                }
                if (board.getPiece(newSquare).getPieceType() == KING) {
                    if (board.getPiece(newSquare).getTeamColor() == teamColor) {
                        kingSquare = newSquare;
                        break;
                    }
                }
            }
        }

        if (kingSquare == null) {
            //System.out.println("No King for checkmate function");
            return false;
        }

        for (int row = 1; row <= 8; row++) {
            for (int column = 1; column <= 8; column++) {
                newSquare = new ChessSquare(row, column);
                if (board.getPiece(newSquare) == null)  {
                    continue;
                }
                if (board.getPiece(newSquare).getTeamColor() == teamColor) {
                    continue;
                }

                moves = board.getPiece(newSquare).pieceMoves(board, newSquare);
                for (ChessMove move : moves) {
                    if (move.getEndPosition().equals(kingSquare)) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        System.out.println(board);
        if (!isInCheck(teamColor)) {
            return false;
        }

        ChessPosition newSquare;
        for (int row = 1; row <= 8; row++) {
            for (int column = 1; column <= 8; column++) {
                newSquare = new ChessSquare(row,column);
                if (board.getPiece(newSquare) != null){
                    if (board.getPiece(newSquare).getTeamColor() != teamColor) {
                        continue;
                    }

                    if (!validMoves(newSquare).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }

        ChessPosition newSquare;
        for (int row = 1; row <= 8; row++) {
            for (int column = 1; column <= 8; column++) {
                newSquare = new ChessSquare(row,column);
                if (board.getPiece(newSquare) != null){
                    if (board.getPiece(newSquare).getTeamColor() != teamColor) {
                        continue;
                    }

                    if (!validMoves(newSquare).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void setBoard(ChessBoard board) {
        this.board.copyBoard(board);
    }

    //CHANGED THIS
    @Override
    public ChessBoardLayout getBoard() {
        return board;
    }
}
