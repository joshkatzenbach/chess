package chessCode;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessPiece.PieceType.BISHOP;

public class Bishop implements ChessPiece {

    private final PieceType type = BISHOP;

    private ChessGame.TeamColor color;

    public Bishop(ChessGame.TeamColor color) {
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

        //Up Right
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
