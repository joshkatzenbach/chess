package chessCode;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;

public class Pawn implements ChessPiece {

    private ChessGame.TeamColor color;

    private final PieceType type = PAWN;

    public Pawn(ChessGame.TeamColor color) {
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

        ChessPosition newSquare;
        if (color == WHITE) {
            newSquare = new ChessSquare(myPosition.getRow() + 1, myPosition.getColumn());
            if (board.getPiece(newSquare) == null) {
                promotionHelper(myPosition, newSquare, moves);

                if (myPosition.getRow() == 2) {
                    newSquare = new ChessSquare(myPosition.getRow() + 2, myPosition.getColumn());
                    if (board.getPiece(newSquare) == null) {
                        moves.add(new Move(myPosition, newSquare, null));
                    }
                }
            }

            newSquare = new ChessSquare(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            if (myPosition.getColumn() + 1 <= 8) {
                if (board.getPiece(newSquare) != null) {
                    if (board.getPiece(newSquare).getTeamColor() != color) {
                        promotionHelper(myPosition, newSquare, moves);
                    }
                }
            }

            newSquare = new ChessSquare(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            if (myPosition.getColumn() - 1 >= 1) {
                if (board.getPiece(newSquare) != null) {
                    if (board.getPiece(newSquare).getTeamColor() != color) {
                        promotionHelper(myPosition, newSquare, moves);
                    }
                }
            }
        }
        else {
            newSquare = new ChessSquare( myPosition.getRow() - 1, myPosition.getColumn());
            if (board.getPiece(newSquare) == null) {
                promotionHelper(myPosition, newSquare, moves);
                if (myPosition.getRow() == 7) {
                    newSquare = new ChessSquare(myPosition.getRow() - 2, myPosition.getColumn());
                    if (board.getPiece(newSquare) == null) {
                        moves.add(new Move(myPosition, newSquare, null));
                    }
                }
            }

            newSquare = new ChessSquare( myPosition.getRow() - 1, myPosition.getColumn() + 1);
            if (myPosition.getColumn() + 1 <= 8) {
                if (board.getPiece(newSquare) != null) {
                    if (board.getPiece(newSquare).getTeamColor() != color) {
                        promotionHelper(myPosition, newSquare, moves);
                    }
                }
            }

            newSquare = new ChessSquare(myPosition.getRow() -1, myPosition.getColumn() - 1);
            if (myPosition.getColumn() - 1 >= 1) {
                if (board.getPiece(newSquare) != null) {
                    if (board.getPiece(newSquare).getTeamColor() != color) {
                        promotionHelper(myPosition, newSquare, moves);
                    }
                }
            }
        }

        return moves;
    }

    private void promotionHelper(ChessPosition myPosition, ChessPosition newSquare, ArrayList<ChessMove> moves) {
        if (((newSquare.getRow() == 8) && (color == WHITE)) || ((newSquare.getRow() == 1) && (color == BLACK))) {
            moves.add(new Move(myPosition, newSquare, QUEEN));
            moves.add(new Move(myPosition, newSquare, BISHOP));
            moves.add(new Move(myPosition, newSquare, KNIGHT));
            moves.add(new Move(myPosition, newSquare, ROOK));
        }
        else {
            moves.add(new Move(myPosition, newSquare, null));
        }
    }
}
