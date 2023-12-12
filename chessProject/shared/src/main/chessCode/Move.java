package chessCode;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Objects;

public class Move implements ChessMove {

    ChessPosition startPosition;
    ChessPosition endPosition;
    ChessPiece.PieceType promotionType;

    public Move(ChessPosition start, ChessPosition end, ChessPiece.PieceType promotionType) {
        startPosition = start;
        endPosition = end;
        this.promotionType = promotionType;
    }
    @Override
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    @Override
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move move)) return false;
        return Objects.equals(getStartPosition(), move.getStartPosition()) && Objects.equals(getEndPosition(), move.getEndPosition()) && promotionType == move.promotionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartPosition(), getEndPosition(), promotionType);
    }

    @Override
    public String toString() {
        String output = "";


        output += (char) (startPosition.getColumn() + 'a' - 1);
        output += (char) (startPosition.getRow() + 48);
        output += " -> ";
        output += (char) (endPosition.getColumn()  + 'a' - 1);
        output += (char) (endPosition.getRow() + 48);
        output += "\n";
        return output;
    }
}
