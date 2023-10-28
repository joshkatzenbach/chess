package chessCode;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessPiece.PieceType.ROOK;

public class Rook implements ChessPiece {

    private final PieceType type = ROOK;
    private ChessGame.TeamColor color;

    public Rook(ChessGame.TeamColor color) {
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

//        for (int i = 0; i < moves.size(); i++) {
//            System.out.println(moves.get(i).toString());
//        }
        return moves;
    }


}
