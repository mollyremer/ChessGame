package chess.impl;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.chessPieces.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChessBoardImpl implements ChessBoard {
    private Map<ChessPosition, ChessPiece> board = new HashMap<ChessPosition, ChessPiece>();
    static final int BOARD_SIZE = 9;

    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board.put(position, piece);
    }

    public void removePiece(ChessPosition position) {
        if (!(board.get(position) == null)){
            board.remove(position, board.get(position));
        }
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return board.get(position);
    }

    public Set<Map.Entry<ChessPosition, ChessPiece>> getAllPieces(ChessGame.TeamColor color){
        Set<Map.Entry<ChessPosition, ChessPiece>> piecesToReturn = new HashSet<>();
        for (Map.Entry<ChessPosition, ChessPiece> entry : board.entrySet()){
            if (entry.getValue().getTeamColor() == color){
                piecesToReturn.add(entry);
            }
        }
        return piecesToReturn;
    }

    @Override
    public void resetBoard() {
        board.clear();

        //WHITE
        ChessPiece wRook1 = new RookPiece(ChessGame.TeamColor.WHITE);
        ChessPosition pos11 = new ChessPositionImpl(1, 1);
        addPiece(pos11, wRook1);

        ChessPiece wKnight1 = new KnightPiece(ChessGame.TeamColor.WHITE);
        ChessPosition pos12 = new ChessPositionImpl(1, 2);
        addPiece(pos12, wKnight1);

        ChessPiece wBishop1 = new BishopPiece(ChessGame.TeamColor.WHITE);
        ChessPosition pos13 = new ChessPositionImpl(1, 3);
        addPiece(pos13, wBishop1);

        ChessPiece wQueen = new QueenPiece(ChessGame.TeamColor.WHITE);
        ChessPosition pos14 = new ChessPositionImpl(1, 4);
        addPiece(pos14, wQueen);

        ChessPiece wKing = new KingPiece(ChessGame.TeamColor.WHITE);
        ChessPosition pos15 = new ChessPositionImpl(1, 5);
        addPiece(pos15, wKing);

        ChessPiece wBishop2 = new BishopPiece(ChessGame.TeamColor.WHITE);
        ChessPosition pos16 = new ChessPositionImpl(1, 6);
        addPiece(pos16, wBishop2);

        ChessPiece wKnight2 = new KnightPiece(ChessGame.TeamColor.WHITE);
        ChessPosition pos17 = new ChessPositionImpl(1, 7);
        addPiece(pos17, wKnight2);

        ChessPiece wRook2 = new RookPiece(ChessGame.TeamColor.WHITE);
        ChessPosition pos18 = new ChessPositionImpl(1, 8);
        addPiece(pos18, wRook2);

        //BLACK
        ChessPiece bRook1 = new RookPiece(ChessGame.TeamColor.BLACK);
        ChessPosition pos81 = new ChessPositionImpl(8, 1);
        addPiece(pos81, bRook1);

        ChessPiece bKnight1 = new KnightPiece(ChessGame.TeamColor.BLACK);
        ChessPosition pos82 = new ChessPositionImpl(8, 2);
        addPiece(pos82, bKnight1);

        ChessPiece bBishop1 = new BishopPiece(ChessGame.TeamColor.BLACK);
        ChessPosition pos83 = new ChessPositionImpl(8, 3);
        addPiece(pos83, bBishop1);

        ChessPiece bQueen = new QueenPiece(ChessGame.TeamColor.BLACK);
        ChessPosition pos84 = new ChessPositionImpl(8, 4);
        addPiece(pos84, bQueen);

        ChessPiece bKing = new KingPiece(ChessGame.TeamColor.BLACK);
        ChessPosition pos85 = new ChessPositionImpl(8, 5);
        addPiece(pos85, bKing);

        ChessPiece bBishop2 = new BishopPiece(ChessGame.TeamColor.BLACK);
        ChessPosition pos86 = new ChessPositionImpl(8, 6);
        addPiece(pos86, bBishop2);

        ChessPiece bKnight2 = new KnightPiece(ChessGame.TeamColor.BLACK);
        ChessPosition pos87 = new ChessPositionImpl(8, 7);
        addPiece(pos87, bKnight2);

        ChessPiece bRook2 = new RookPiece(ChessGame.TeamColor.BLACK);
        ChessPosition pos88 = new ChessPositionImpl(8, 8);
        addPiece(pos88, bRook2);

        //PAWNS
        for (int i = 1; i < BOARD_SIZE; ++i){
            ChessPiece pawn = new PawnPiece(ChessGame.TeamColor.WHITE);
            ChessPosition pos2 = new ChessPositionImpl(2, i);
            addPiece(pos2, pawn);
        }

        for (int i = 1; i < BOARD_SIZE; ++i){
            ChessPiece pawn = new PawnPiece(ChessGame.TeamColor.BLACK);
            ChessPosition pos7 = new ChessPositionImpl(7, i);
            addPiece(pos7, pawn);
        }

        /*
        |r|n|b|q|k|b|n|r|
		|p|p|p|p|p|p|p|p|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P|P|P|P|P|P|P|P|
		|R|N|B|Q|K|B|N|R|
         */

    }


}
