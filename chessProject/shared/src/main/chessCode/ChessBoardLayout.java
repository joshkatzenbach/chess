package chessCode;

import chess.*;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;

public class ChessBoardLayout implements ChessBoard {

    ChessPiece[][] squares;

    public ChessBoardLayout() {
        squares = new ChessPiece[8][8];
    }
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if ((position.getRow() < 1) || (position.getRow()> 8)){
            System.out.println("Invalid Row");
        }
        else if ((position.getColumn() < 1) || (position.getColumn() > 8)){
            System.out.println("Invalid Row");
        }
        else {
            squares[position.getRow() - 1][position.getColumn() - 1] = piece;
        }
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    @Override
    public void resetBoard() {
        // Clear All Squares
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = null;
            }
        }

        //Pawns
        for (int i = 0; i < 8; i++) {
            squares[1][i] = new Pawn(WHITE);
            squares[6][i] = new Pawn(BLACK);
        }

        //Rooks
        squares[0][0] = new Rook(WHITE);
        squares[0][7] = new Rook(WHITE);
        squares[7][0] = new Rook(BLACK);
        squares[7][7] = new Rook(BLACK);

        //Knights
        squares[0][1] = new Knight(WHITE);
        squares[0][6] = new Knight(WHITE);
        squares[7][1] = new Knight(BLACK);
        squares[7][6] = new Knight(BLACK);

        //Bishops
        squares[0][2] = new Bishop(WHITE);
        squares[0][5] = new Bishop(WHITE);
        squares[7][2] = new Bishop(BLACK);
        squares[7][5] = new Bishop(BLACK);

        //Kings
        squares[0][4] = new King(WHITE);
        squares[7][4] = new King(BLACK);

        //Queens
        squares[0][3] = new Queen(WHITE);
        squares[7][3] = new Queen(BLACK);
    }


    public void copyBoard(ChessBoard board) {
        ChessSquare square;
        for (int row = 1; row <= 8; row++) {
            for (int column = 1; column <= 8; column++) {
                square = new ChessSquare(row, column);
                this.addPiece(square, board.getPiece(square));
            }
        }
    }

    public void potentialMove(ChessMove move) {
        if (move.getPromotionPiece() == QUEEN) {
            this.addPiece(move.getEndPosition(), new Queen(this.getPiece(move.getStartPosition()).getTeamColor()));
        }
        else if (move.getPromotionPiece() == BISHOP) {
            this.addPiece(move.getEndPosition(), new Bishop(this.getPiece(move.getStartPosition()).getTeamColor()));
        }
        else if (move.getPromotionPiece() == ROOK) {
            this.addPiece(move.getEndPosition(), new Rook(this.getPiece(move.getStartPosition()).getTeamColor()));
        }
        else if (move.getPromotionPiece() == KNIGHT) {
            this.addPiece(move.getEndPosition(), new Knight(this.getPiece(move.getStartPosition()).getTeamColor()));
        }
        else {
            this.addPiece(move.getEndPosition(), this.getPiece(move.getStartPosition()));
        }
        this.addPiece(move.getStartPosition(), null);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            output.append('|');
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = squares[i][j];
                if (piece == null) {
                    output.append(' ');
                }
                else if (piece.getPieceType() == KING) {
                    if (piece.getTeamColor() == WHITE) {
                        output.append('K');
                    }
                    else {
                        output.append('k');
                    }
                }
                else if (piece.getPieceType() == QUEEN) {
                    if (piece.getTeamColor() == WHITE) {
                        output.append('Q');
                    }
                    else {
                        output.append('q');
                    }
                }
                else if (piece.getPieceType() == ROOK) {
                    if (piece.getTeamColor() == WHITE) {
                        output.append('R');
                    }
                    else {
                        output.append('r');
                    }
                }
                else if (piece.getPieceType() == BISHOP) {
                    if (piece.getTeamColor() == WHITE) {
                        output.append('B');
                    }
                    else {
                        output.append('b');
                    }
                }
                else if (piece.getPieceType() == PAWN) {
                    if (piece.getTeamColor() == WHITE) {
                        output.append('P');
                    }
                    else {
                        output.append('p');
                    }
                }
                else if (piece.getPieceType() == KNIGHT) {
                    if (piece.getTeamColor() == WHITE) {
                        output.append('N');
                    }
                    else {
                        output.append('n');
                    }
                }
                output.append('|');
            }
            output.append("\n");
        }
        return output.toString();
    }
}
