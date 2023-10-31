package chess.impl;

import chess.*;

import java.util.Collection;

public abstract class ChessPieceImpl implements ChessPiece {
    private PieceType type;
    protected ChessGame.TeamColor pieceColor;
    public ChessPieceImpl(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type){
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    @Override
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    public static boolean isPositionOnBoard(int row, int col){
        int boardSize = 8;
        return row > 0 && row <= boardSize && col > 0 && col <= boardSize;
    }
}
