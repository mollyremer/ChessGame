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

public class KnightPiece extends ChessPieceImpl {
    public KnightPiece(ChessGame.TeamColor pieceColor) {
        super(pieceColor, PieceType.KNIGHT);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition start) {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | |6| |5| | |
		| | |7| | | |4| |
		| | | | |N| | | |
		| | |8| | | |3| |
		| | | |1| |2| | |
		| | | | | | | | |
         */

        Set<ChessPosition> possiblePositions = getChessPositions(start);
        Set<ChessMove> validMoves = new HashSet<>();

        for (ChessPosition position : possiblePositions){
            //if square is empty, or if square has enemy, create move and add to set
            if (board.getPiece(position) == null
            ||  board.getPiece(position).getTeamColor() != getTeamColor()) {
                ChessMove move = new ChessMoveImpl(start, position, null);
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    private static Set<ChessPosition> getChessPositions(ChessPosition start) {
        int row = start.getRow();
        int col = start.getColumn();

        int[] rowOffSet = {-2, -2, -1, 1, 2, 2, 1, -1};
        int[] colOffSet = {-1, 1, 2, 2, 1, -1, -2, -2};

        Set<ChessPosition> possiblePositions = new HashSet<>();

        for (int i = 0; i < rowOffSet.length; ++i) {
            if (isPositionOnBoard(row + rowOffSet[i], col + colOffSet[i])) {
                ChessPosition position = new ChessPositionImpl(row + rowOffSet[i], col + colOffSet[i]);
                possiblePositions.add(position);
            }
        }
        return possiblePositions;
    }
}
