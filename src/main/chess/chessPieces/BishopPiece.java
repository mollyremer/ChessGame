package chess.chessPieces;

import chess.impl.ChessMoveImpl;
import chess.impl.ChessPieceImpl;
import chess.impl.ChessPositionImpl;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BishopPiece extends ChessPieceImpl {
    public BishopPiece(ChessGame.TeamColor pieceColor) {
        super(pieceColor, PieceType.BISHOP);
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition start) {
        /*
        | | | | |*| | | |
		| | | | |*| | | |
		| | | | |*| | | |
		| | | | |*| | | |
		|*|*|*|*|R|*|*|*|
		| | | | |*| | | |
		| | | | |*| | | |
		| | | | |*| | | |
         */
        int row = start.getRow();
        int col = start.getColumn();

        int[] noOffSet = {0, 0, 0, 0, 0, 0, 0};
        int[] positiveOffSet = {1, 2, 3, 4, 5, 6, 7};
        int[] negativeOffSet = {-1, -2, -3, -4, -5, -6, -7};

        Set<ChessMove> validMoves = new HashSet<>();
        validMoves.addAll(getMoves(board, start, positiveOffSet, positiveOffSet, row, col));
        validMoves.addAll(getMoves(board, start, negativeOffSet, positiveOffSet, row, col));
        validMoves.addAll(getMoves(board, start, negativeOffSet, negativeOffSet, row, col));
        validMoves.addAll(getMoves(board, start, positiveOffSet, negativeOffSet, row, col));

        return validMoves;
    }

    public Set<ChessMove> getMoves(ChessBoard board, ChessPosition start, int[] rowOffSet, int[] colOffSet, int row, int col){
        Set<ChessMove> moves = new HashSet<>();
        boolean pathClear = true;
        boolean capturedAPiece = false;

        for (int i = 0; i < 8; i++) {
            if (pathClear) {
                try {
                    if (capturedAPiece) {
                        throw new IllegalArgumentException();
                    }

                    ChessPosition position;
                    ChessMove move;
                    if (isPositionOnBoard(row + rowOffSet[i], col + colOffSet[i])) {
                        position = new ChessPositionImpl(row + rowOffSet[i], col + colOffSet[i]);
                    } else {
                        throw new IllegalArgumentException();
                    }

                    if (board.getPiece(position) == null) { //if blank square
                        move = new ChessMoveImpl(start, position, null);
                        moves.add(move);
                    } else if (board.getPiece(position).getTeamColor() != getTeamColor()) { //if square is an enemy
                        move = new ChessMoveImpl(start, position, null);
                        moves.add(move);
                        capturedAPiece = true;
                    } else if (board.getPiece(position).getTeamColor() == getTeamColor()) { //if square is ally
                        throw new IllegalArgumentException();
                    }
                } catch (IllegalArgumentException e) {
                    pathClear = false;
                }
            }
        }

        return moves;
    }

}

