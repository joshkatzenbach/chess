package chessCode;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessPiece.PieceType.KNIGHT;

public class Knight implements ChessPiece {

    private final PieceType type = KNIGHT;

    private ChessGame.TeamColor color;

    public Knight(ChessGame.TeamColor color) {
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
        //Very right Up
        ChessPosition newSquare = new ChessSquare(myPosition.getRow() + 1, myPosition.getColumn() + 2);
        if ((newSquare.getRow() <= 8) && (newSquare.getColumn() <= 8)) {
            if (board.getPiece(newSquare) == null) {
                moves.add(new Move(myPosition, newSquare, null));
            } else if (board.getPiece(newSquare).getTeamColor() != color) {
                moves.add(new Move(myPosition, newSquare, null));
            }
        }

        //Very Up Right
       newSquare = new ChessSquare(myPosition.getRow() + 2, myPosition.getColumn() + 1);
        if ((newSquare.getRow() <= 8) && (newSquare.getColumn() <= 8)) {
            if (board.getPiece(newSquare) == null) {
                moves.add(new Move(myPosition, newSquare, null));
            } else if (board.getPiece(newSquare).getTeamColor() != color) {
                moves.add(new Move(myPosition, newSquare, null));
            }
        }

        // Very Down Right
        newSquare = new ChessSquare(myPosition.getRow() - 2, myPosition.getColumn() + 1);
        if ((newSquare.getRow() >= 1) && (newSquare.getColumn() <= 8)) {
            if (board.getPiece(newSquare) == null) {
                moves.add(new Move(myPosition, newSquare, null));
            } else if (board.getPiece(newSquare).getTeamColor() != color) {
                moves.add(new Move(myPosition, newSquare, null));
            }
        }

        //Very Right Down
        newSquare = new ChessSquare(myPosition.getRow() - 1, myPosition.getColumn() + 2);
        if ((newSquare.getRow() >= 1) && (newSquare.getColumn() <= 8)) {
            if (board.getPiece(newSquare) == null) {
                moves.add(new Move(myPosition, newSquare, null));
            } else if (board.getPiece(newSquare).getTeamColor() != color) {
                moves.add(new Move(myPosition, newSquare, null));
            }
        }
        // Very Left Down
        newSquare = new ChessSquare(myPosition.getRow() - 1,myPosition.getColumn() - 2);
        if ((newSquare.getRow() >= 1) && (newSquare.getColumn() >= 1)) {
            if (board.getPiece(newSquare) == null) {
                moves.add(new Move(myPosition, newSquare, null));
            } else if (board.getPiece(newSquare).getTeamColor() != color) {
                moves.add(new Move(myPosition, newSquare, null));
            }
        }

        // Very Down Left
        newSquare = new ChessSquare(myPosition.getRow() - 2,myPosition.getColumn() - 1);
        if ((newSquare.getRow() >= 1) && (newSquare.getColumn() >= 1)) {
            if (board.getPiece(newSquare) == null) {
                moves.add(new Move(myPosition, newSquare, null));
            } else if (board.getPiece(newSquare).getTeamColor() != color) {
                moves.add(new Move(myPosition, newSquare, null));
            }
        }

        //Very Up Left
        newSquare = new ChessSquare(myPosition.getRow() + 2,myPosition.getColumn() - 1);
        if ((newSquare.getRow() <= 8) && (newSquare.getColumn() >= 1)) {
            if (board.getPiece(newSquare) == null) {
                moves.add(new Move(myPosition, newSquare, null));
            } else if (board.getPiece(newSquare).getTeamColor() != color) {
                moves.add(new Move(myPosition, newSquare, null));
            }
        }

        //Very Left Up
        newSquare = new ChessSquare(myPosition.getRow() + 1,myPosition.getColumn() - 2);
        if ((newSquare.getRow() <= 8) && (newSquare.getColumn() >= 1)) {
            if (board.getPiece(newSquare) == null) {
                moves.add(new Move(myPosition, newSquare, null));
            } else if (board.getPiece(newSquare).getTeamColor() != color) {
                moves.add(new Move(myPosition, newSquare, null));
            }
        }

        return moves;
    }
}
