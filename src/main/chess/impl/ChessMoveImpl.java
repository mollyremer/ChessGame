package chess.impl;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Objects;

public class ChessMoveImpl implements ChessMove {
    private ChessPosition start;
    private ChessPosition end;

    private ChessPiece.PieceType promotionType;

    public ChessMoveImpl(ChessPosition start, ChessPosition end, ChessPiece.PieceType promotionType){
        this.start = start;
        this.end = end;
        this.promotionType = promotionType;
    }
    @Override
    public ChessPosition getStartPosition() {
        return start;
    }

    @Override
    public ChessPosition getEndPosition() {
        return end;
    }

    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMoveImpl chessMove = (ChessMoveImpl) o;
        if (this.start == ((ChessMoveImpl) o).start && this.end == ((ChessMoveImpl) o).end) return true;
        return Objects.equals(start, chessMove.start) && Objects.equals(end, chessMove.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
