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

public class PawnPiece extends ChessPieceImpl {

    public PawnPiece(ChessGame.TeamColor pieceColor) {
        super(pieceColor, PieceType.PAWN);
    }

//    public ChessPiece.PieceType promotionType(ChessBoard board, ChessPosition position){
//        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE){
//
//        }
//    }

    public Set<ChessMove> addMove (ChessPosition start, ChessPosition end, ChessBoard board){
        Set<ChessMove> movesToAdd = new HashSet<>();

        if (promotePawn(board, end)){
            ChessMove moveQ = new ChessMoveImpl(start, end, PieceType.QUEEN);
            movesToAdd.add(moveQ);
            ChessMove moveR = new ChessMoveImpl(start, end, PieceType.ROOK);
            movesToAdd.add(moveR);
            ChessMove moveB = new ChessMoveImpl(start, end, PieceType.BISHOP);
            movesToAdd.add(moveB);
            ChessMove moveK = new ChessMoveImpl(start, end, PieceType.KNIGHT);
            movesToAdd.add(moveK);
        }
        else {
            ChessMove move = new ChessMoveImpl(start, end, null);
            movesToAdd.add(move);
        }

        return movesToAdd;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition start) {
        int row = start.getRow();
        int col = start.getColumn();
        int moveDirection = 1;
        if (getTeamColor() == ChessGame.TeamColor.BLACK) { moveDirection = -1; }

        Set<ChessMove> validMoves = new HashSet<>();

        if (isPositionOnBoard(row + moveDirection, col)) {
            ChessPosition oneSpace = new ChessPositionImpl(row + moveDirection, col);
            if (board.getPiece(oneSpace) == null) {
                validMoves.addAll(addMove(start, oneSpace, board));

                //if you could move one space, might be able to move two
                if ((getTeamColor() == ChessGame.TeamColor.WHITE && start.getRow() == 2) ||
                        (getTeamColor() == ChessGame.TeamColor.BLACK && start.getRow() == 7)) {
                    ChessPosition twoSpaces = new ChessPositionImpl(row + (2 * moveDirection), col);
                    if (board.getPiece(twoSpaces) == null) {
                        validMoves.addAll(addMove(start, twoSpaces, board));
                    }
                }
            }
        }

        //attack to the right diagonal
        if (isPositionOnBoard(row + moveDirection, col + 1)){
            ChessPosition attackRight = new ChessPositionImpl(row + moveDirection, col + 1);
            if (!(board.getPiece(attackRight) == null  ||  board.getPiece(attackRight).getTeamColor() == pieceColor)){
                validMoves.addAll(addMove(start, attackRight, board));
            }
        }

        //attack to the left diagonal
        if (isPositionOnBoard(row + moveDirection, col - 1)){
            ChessPosition attackLeft = new ChessPositionImpl(row + moveDirection, col - 1);
            if (!(board.getPiece(attackLeft) == null  ||  board.getPiece(attackLeft).getTeamColor() == pieceColor)){
                validMoves.addAll(addMove(start, attackLeft, board));
            }
        }

        return validMoves;

    }


    private boolean promotePawn(ChessBoard board, ChessPosition position){
        //if row 8 for white and row 1 for black
        if (pieceColor == ChessGame.TeamColor.WHITE && position.getRow() == 8){
            return true;
        } else if (pieceColor == ChessGame.TeamColor.BLACK && position.getRow() == 1) {
            return true;
        }
        return false;
    }

}
