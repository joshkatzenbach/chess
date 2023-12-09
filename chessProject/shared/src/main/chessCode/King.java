package chessCode;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessPiece.PieceType.KING;

public class King implements ChessPiece {

    private ChessGame.TeamColor color;
    private final PieceType type = KING;

    public King(ChessGame.TeamColor color) {
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

        //Up
        ChessPosition newSquare = new ChessSquare(myPosition.getRow() + 1, myPosition.getColumn());
        if ((newSquare.getRow() <= 8) && (newSquare.getColumn() <= 8)) {
            if (board.getPiece(newSquare) == null)  {
                moves.add(new Move(myPosition, newSquare, null));
            }
            else if (board.getPiece(newSquare).getTeamColor() != color) {
                moves.add(new Move(myPosition, newSquare, null));
            }
        }

        //Up Right
        newSquare = new ChessSquare(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        if ((newSquare.getRow() <= 8) && (newSquare.getColumn() <= 8)) {
            if (board.getPiece(newSquare) == null)  {
                moves.add(new Move(myPosition, newSquare, null));
            }
            else if (board.getPiece(newSquare).getTeamColor() != color) {
                moves.add(new Move(myPosition, newSquare, null));
            }
        }

        //Right
        newSquare = new ChessSquare(myPosition.getRow(), myPosition.getColumn() + 1);
        if ((newSquare.getRow() <= 8) && (newSquare.getColumn() <= 8)) {
            if (board.getPiece(newSquare) == null)  {
                moves.add(new Move(myPosition, newSquare, null));
            }
            else if (board.getPiece(newSquare).getTeamColor() != color) {
                moves.add(new Move(myPosition, newSquare, null));
            }
        }

        //Down Right
        newSquare = new ChessSquare(myPosition.getRow() - 1, myPosition.getColumn() + 1);
        if ((newSquare.getRow() >= 1) && (newSquare.getColumn() <= 8)) {
            if (board.getPiece(newSquare) == null)  {
                moves.add(new Move(myPosition, newSquare, null));
            }
            else if (board.getPiece(newSquare).getTeamColor() != color) {
                moves.add(new Move(myPosition, newSquare, null));
            }
        }

        //Down
        newSquare = new ChessSquare(myPosition.getRow() - 1, myPosition.getColumn());
        if ((newSquare.getRow() >= 1) && (newSquare.getColumn() <= 8)) {
            if (board.getPiece(newSquare) == null)  {
                moves.add(new Move(myPosition, newSquare, null));
            }
            else if (board.getPiece(newSquare).getTeamColor() != color) {
                moves.add(new Move(myPosition, newSquare, null));
            }
        }

        //Down left
        newSquare = new ChessSquare(myPosition.getRow() - 1, myPosition.getColumn() - 1);
        if ((newSquare.getRow() >= 1) && (newSquare.getColumn() >= 1)) {
            if (board.getPiece(newSquare) == null)  {
                moves.add(new Move(myPosition, newSquare, null));
            }
            else if (board.getPiece(newSquare).getTeamColor() != color) {
                moves.add(new Move(myPosition, newSquare, null));
            }
        }

        //Left
        newSquare = new ChessSquare(myPosition.getRow(), myPosition.getColumn() - 1);
        if ((newSquare.getRow() >= 1) && (newSquare.getColumn() >= 1)) {
            if (board.getPiece(newSquare) == null)  {
                moves.add(new Move(myPosition, newSquare, null));
            }
            else if (board.getPiece(newSquare).getTeamColor() != color) {
                moves.add(new Move(myPosition, newSquare, null));
            }
        }

        //Left Up
        newSquare = new ChessSquare(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        if ((newSquare.getRow() <= 8) && (newSquare.getColumn() >= 1)) {
            if (board.getPiece(newSquare) == null)  {
                moves.add(new Move(myPosition, newSquare, null));
            }
            else if (board.getPiece(newSquare).getTeamColor() != color) {
                moves.add(new Move(myPosition, newSquare, null));
            }
        }
        return moves;
    }
}
