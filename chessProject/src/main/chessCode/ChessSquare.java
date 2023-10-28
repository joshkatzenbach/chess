package chessCode;

import chess.*;

import java.util.Objects;

public class ChessSquare implements ChessPosition {
        private int row;
        private int column;
        public ChessSquare(int row, int column) {
            this.row = row;
            this.column = column;
        }
    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessSquare that)) return false;
        return getRow() == that.getRow() && getColumn() == that.getColumn();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getColumn());
    }

    @Override
    public String toString() {
            String output = "";
            output += "Row: " + row + " Column: " + column + '\n';
            return output;
    }
}

