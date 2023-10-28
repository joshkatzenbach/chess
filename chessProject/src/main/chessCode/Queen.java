package chessCode;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessPiece.PieceType.QUEEN;

public class Queen implements ChessPiece {

    private ChessGame.TeamColor color;

    private final PieceType type = QUEEN;

    public Queen(ChessGame.TeamColor color) {
        this.color = color;
    }
    @Override
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    @Override
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        // Up Moves
        for (int row = myPosition.getRow() + 1; row <= 8; row++) {
            ChessSquare newSquare = new ChessSquare(row, myPosition.getColumn());
            if (board.getPiece(newSquare) == null) {
                moves.add(new Move(myPosition, newSquare, null));
            }
            else {
                if (board.getPiece(newSquare).getTeamColor() != color) {
                    moves.add(new Move(myPosition, newSquare, null));
                }
                break;
            }
        }

        // Down Moves
        for (int row = myPosition.getRow() - 1; row >= 1; row--) {
            ChessSquare newSquare = new ChessSquare(row, myPosition.getColumn());
            if (board.getPiece(newSquare) == null) {
                moves.add(new Move(myPosition, newSquare, null));
            }
            else {
                if (board.getPiece(newSquare).getTeamColor() != color) {
                    moves.add(new Move(myPosition, newSquare, null));
                }
                break;
            }
        }

        //Right Moves
        for (int column = myPosition.getColumn() + 1; column <= 8; column++) {
            ChessSquare newSquare = new ChessSquare(myPosition.getRow(), column);
            if (board.getPiece(newSquare) == null) {
                moves.add(new Move(myPosition, newSquare, null));
            }
            else {
                if (board.getPiece(newSquare).getTeamColor() != color) {
                    moves.add(new Move(myPosition, newSquare, null));
                }
                break;
            }
        }

        //Left Moves
        for (int column = myPosition.getColumn() - 1; column >= 1; column--) {
            ChessSquare newSquare = new ChessSquare(myPosition.getRow(), column);
            if (board.getPiece(newSquare) == null) {
                moves.add(new Move(myPosition, newSquare, null));
            }
            else {
                if (board.getPiece(newSquare).getTeamColor() != color) {
                    moves.add(new Move(myPosition, newSquare, null));
                }
                break;
            }
        }

        for (int i = 1; i <= 8; i++) {
            ChessPosition newSquare = new ChessSquare(myPosition.getRow() + i, myPosition.getColumn() + i);
            if ( (newSquare.getRow() > 8) || (newSquare.getColumn() > 8) ) {
                break;
            }

            if (board.getPiece(newSquare) == null) {
                moves.add(new Move(myPosition, newSquare, null));
            }
            else {
                if (board.getPiece(newSquare).getTeamColor() != color) {
                    moves.add(new Move(myPosition, newSquare, null));
                }
                break;
            }
        }
        //Up Left
        for (int i = 1; i <= 8; i++) {
            ChessPosition newSquare = new ChessSquare(myPosition.getRow() + i, myPosition.getColumn() - i);
            if ( (newSquare.getRow() > 8) || (newSquare.getColumn() < 1) ) {
                break;
            }

            if (board.getPiece(newSquare) == null) {
                moves.add(new Move(myPosition, newSquare, null));
            }
            else {
                if (board.getPiece(newSquare).getTeamColor() != color) {
                    moves.add(new Move(myPosition, newSquare, null));
                }
                break;
            }
        }

        // Down Left
        for (int i = 1; i <= 8; i++) {
            ChessPosition newSquare = new ChessSquare(myPosition.getRow() - i, myPosition.getColumn() - i);
            if ( (newSquare.getRow() < 1) || (newSquare.getColumn() < 1) ) {
                break;
            }

            if (board.getPiece(newSquare) == null) {
                moves.add(new Move(myPosition, newSquare, null));
            }
            else {
                if (board.getPiece(newSquare).getTeamColor() != color) {
                    moves.add(new Move(myPosition, newSquare, null));
                }
                break;
            }
        }

        //Down Right
        for (int i = 1; i <= 8; i++) {
            ChessPosition newSquare = new ChessSquare(myPosition.getRow() - i, myPosition.getColumn() + i);
            if ( (newSquare.getRow() < 1) || (newSquare.getColumn() > 8) ) {
                break;
            }

            if (board.getPiece(newSquare) == null) {
                moves.add(new Move(myPosition, newSquare, null));
            }
            else {
                if (board.getPiece(newSquare).getTeamColor() != color) {
                    moves.add(new Move(myPosition, newSquare, null));
                }
                break;
            }
        }
        return moves;
    }
}
