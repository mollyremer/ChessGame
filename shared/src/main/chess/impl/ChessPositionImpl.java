package chess.impl;

import chess.ChessPosition;

import java.util.Objects;

public class ChessPositionImpl implements ChessPosition {
    private int row;
    private int column;
    public ChessPositionImpl(int row, int column){
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
        if (o == null || getClass() != o.getClass()) return false;
        ChessPositionImpl that = (ChessPositionImpl) o;
        return row == that.row && column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    public String toString(){
        return "" + intToString(column) + "" + row;
    }

    private char intToString(int v){
        switch (v) {
            case 1 -> { return 'h';}
            case 2 -> { return 'g';}
            case 3 -> { return 'f';}
            case 4 -> { return 'e';}
            case 5 -> { return 'd';}
            case 6 -> { return 'c';}
            case 7 -> { return 'b';}
            case 8 -> { return 'a';}
        }
        return 0;
    }
}
