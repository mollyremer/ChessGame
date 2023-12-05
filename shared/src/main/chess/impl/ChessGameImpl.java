package chess.impl;

import chess.*;
import chess.chessPieces.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChessGameImpl implements ChessGame {
    private TeamColor teamTurn = TeamColor.WHITE;
    private ChessBoard currentBoard = new ChessBoardImpl();

    public String serialize(){
        String s = "";
        if (teamTurn == TeamColor.WHITE) {
            s += "W";
        } else {
            s += "B";
        }

        s += currentBoard.serialize();

        return s;
    }

    public ChessGameImpl(String s){
        if (s.charAt(0) == 'W'){ teamTurn = TeamColor.WHITE;
        } else if (s.charAt(0) == 'B') { teamTurn = TeamColor.BLACK;
        } else {
            System.out.println("Problem deserializing in chess game impl");
        }
        s = s.substring(1);

        currentBoard = new ChessBoardImpl(s);
    }

    public ChessGameImpl(){
        currentBoard.resetBoard();
    }
    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) { teamTurn = team; }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (currentBoard.getPiece(startPosition) == null) {
            return null;
        }

        Set<ChessMove> moves = new HashSet<>((Set<ChessMove>) currentBoard.getPiece(startPosition).pieceMoves(currentBoard, startPosition));
        Set<ChessMove> validMoves = new HashSet<>();

        for (ChessMove move : moves){
            boolean isInCheckAfterMove = false;
            ChessPiece pieceToMove = currentBoard.getPiece(move.getStartPosition());
            ChessPiece pieceToKill = null;
            if (currentBoard.getPiece(move.getEndPosition()) != null){
                pieceToKill = currentBoard.getPiece(move.getEndPosition());
            }

            currentBoard.removePiece(move.getStartPosition());
            currentBoard.removePiece(move.getEndPosition());
            currentBoard.addPiece(move.getEndPosition(), pieceToMove);

            if (isInCheckmate(pieceToMove.getTeamColor())){ isInCheckAfterMove = true; }

            currentBoard.removePiece(move.getEndPosition());
            currentBoard.addPiece(move.getStartPosition(), pieceToMove);
            if (pieceToKill != null) {
                currentBoard.addPiece(move.getEndPosition(), pieceToKill);
            }

            if (!isInCheckAfterMove) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
            Set<ChessMove> validMoves = (Set<ChessMove>) currentBoard.getPiece(move.getStartPosition()).pieceMoves(currentBoard, move.getStartPosition());
            boolean moveIsValid = false;
            for (ChessMove vm : validMoves) {
                if (vm.equals(move)) {
                    moveIsValid = true;
                    break;
                }
            }
            if (!moveIsValid) {
                throw new InvalidMoveException("Move was not in the list of valid moves");
            }

            if (currentBoard.getPiece(move.getStartPosition()).getTeamColor() != teamTurn) {
                throw new InvalidMoveException("It is not their turn to move");
            }

            ChessPiece pieceToMove = currentBoard.getPiece(move.getStartPosition());
            ChessPiece pieceToKill = null;
            if (currentBoard.getPiece(move.getEndPosition()) != null){
                pieceToKill = currentBoard.getPiece(move.getEndPosition());
            }

            currentBoard.removePiece(move.getStartPosition());
            currentBoard.removePiece(move.getEndPosition());
            currentBoard.addPiece(move.getEndPosition(), pieceToMove);
            if (move.getPromotionPiece() != null){
                ChessPiece.PieceType pieceType = move.getPromotionPiece();
                ChessPiece promotedPiece = null;
                if (pieceType == ChessPiece.PieceType.QUEEN){
                    promotedPiece = new QueenPiece(pieceToMove.getTeamColor());
                } else if (pieceType == ChessPiece.PieceType.BISHOP) {
                    promotedPiece = new BishopPiece(pieceToMove.getTeamColor());
                } else if (pieceType == ChessPiece.PieceType.ROOK) {
                    promotedPiece = new RookPiece(pieceToMove.getTeamColor());
                } else if (pieceType == ChessPiece.PieceType.KNIGHT) {
                    promotedPiece = new KnightPiece(pieceToMove.getTeamColor());
                }
                currentBoard.addPiece(move.getEndPosition(), promotedPiece);
            }

            if (isInCheck(pieceToMove.getTeamColor()) || isInCheckmate(pieceToMove.getTeamColor())) {
                currentBoard.removePiece(move.getEndPosition());
                currentBoard.addPiece(move.getStartPosition(), pieceToMove);
                if (pieceToKill != null) {
                    currentBoard.addPiece(move.getEndPosition(), pieceToKill);
                }
                throw new InvalidMoveException("Moving there would put their team into check or checkmate");
            }

            switchTeamTurn();
    }

    public void switchTeamTurn(){
        if (teamTurn == TeamColor.WHITE) { teamTurn = TeamColor.BLACK; }
        else { teamTurn = TeamColor.WHITE; }
    }

    public Set<ChessMove> allPossibleMoves(TeamColor color){
        Set<Map.Entry<ChessPosition, ChessPiece>> enemyPieces = currentBoard.getAllPieces(color);
        Set<ChessMove> validMoves = new HashSet<>();
        for (Map.Entry<ChessPosition, ChessPiece> entry : enemyPieces){
            validMoves.addAll(entry.getValue().pieceMoves(currentBoard, entry.getKey()));
        }
        return validMoves;
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor enemyColor;
        if (teamColor == TeamColor.WHITE) { enemyColor = TeamColor.BLACK;}
        else { enemyColor = TeamColor.WHITE; }

        Set<ChessMove> validMoves = allPossibleMoves(enemyColor);

        for (ChessMove move : validMoves){
            if (getKingPosition(teamColor) != null &&  move.getEndPosition().getRow() == getKingPosition(teamColor).getRow()
                    && move.getEndPosition().getColumn() == getKingPosition(teamColor).getColumn()){
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            boolean isInCheckAfterMove = false;
            for (ChessMove move : allPossibleMoves(teamColor)){
                ChessPiece pieceToMove = currentBoard.getPiece(move.getStartPosition());
                ChessPiece pieceToKill = null;
                if (currentBoard.getPiece(move.getEndPosition()) != null){
                    pieceToKill = currentBoard.getPiece(move.getEndPosition());
                }

                currentBoard.removePiece(move.getStartPosition());
                currentBoard.removePiece(move.getEndPosition());
                currentBoard.addPiece(move.getEndPosition(), pieceToMove);

                if (isInCheck(teamColor)){ isInCheckAfterMove = true; }

                currentBoard.removePiece(move.getEndPosition());
                currentBoard.addPiece(move.getStartPosition(), pieceToMove);
                if (pieceToKill != null) {
                    currentBoard.addPiece(move.getEndPosition(), pieceToKill);
                }

                if (isInCheckAfterMove) {
                    return true;
                }
            }
        }
        return false;
    }

    public ChessPosition getKingPosition(TeamColor teamColor){
        Set<Map.Entry<ChessPosition, ChessPiece>> pieces = currentBoard.getAllPieces(teamColor);
        for (Map.Entry<ChessPosition, ChessPiece> entry : pieces){
            if (entry.getValue().getPieceType() == ChessPiece.PieceType.KING){
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        Set<ChessMove> validMoves = allPossibleMoves(teamColor);
        if (validMoves.isEmpty()){
            return true;
        }

        boolean willBeInCheck = false;
        for (ChessMove move : validMoves){
            ChessPiece pieceToMove = currentBoard.getPiece(move.getStartPosition());
            ChessPiece pieceToKill = null;
            if (currentBoard.getPiece(move.getEndPosition()) != null){
                pieceToKill = currentBoard.getPiece(move.getEndPosition());
            }

            currentBoard.removePiece(move.getStartPosition());
            currentBoard.removePiece(move.getEndPosition());
            currentBoard.addPiece(move.getEndPosition(), pieceToMove);

            if (isInCheckmate(pieceToMove.getTeamColor()) || isInCheckmate(pieceToMove.getTeamColor())) {
                willBeInCheck = true;
            }
            currentBoard.removePiece(move.getEndPosition());
            currentBoard.addPiece(move.getStartPosition(), pieceToMove);
            if (pieceToKill != null) {
                currentBoard.addPiece(move.getEndPosition(), pieceToKill);
            }
        }

        return willBeInCheck;
    }

    @Override
    public void setBoard(ChessBoard board) {
        currentBoard = board;
    }

    @Override
    public ChessBoard getBoard() {
        return currentBoard;
    }
}
