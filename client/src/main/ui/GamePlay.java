package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.impl.ChessBoardImpl;
import chess.impl.ChessGameImpl;
import chess.impl.ChessPositionImpl;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;

public class GamePlay {

    private static final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private static final int BOARD_SIZE = 8;

    List<ChessPosition> allChessPositions = new ArrayList<>();
    private ChessGame.TeamColor teamColor;

    public GamePlay(ChessGame chessGame, ChessGame.TeamColor teamColor){
        this.teamColor = teamColor;

        for (int i = 1; i < BOARD_SIZE + 1; i++){
            for (int j = 1; j < BOARD_SIZE + 1; j++){
                ChessPosition position = new ChessPositionImpl(i, j);
                allChessPositions.add(position);
            }
        }

        createWhiteBoard(chessGame.getBoard());
        out.println();
        createBlackBoard(chessGame.getBoard());

    }

    public void createBlackBoard(ChessBoard chessBoard){
        int bkgIndex = 0;
        int positionIndex = 0;

        drawBlackHeaders();

        for (int j = BOARD_SIZE ; j > 0 ; j--) {
            rowLabel(j);
            for (int i = 1; i < BOARD_SIZE + 1; i += 2) {
                if (chessBoard.getPiece(allChessPositions.get(positionIndex)) == null) {
                    printBlankTile(bkgIndex);
                } else {
                    ChessPiece piece = chessBoard.getPiece(allChessPositions.get(positionIndex));
                    printChessPiece(piece, bkgIndex);
                }
                positionIndex++;
                if (bkgIndex == 1) bkgIndex = 0;
                else bkgIndex = 1;

                if (chessBoard.getPiece(allChessPositions.get(positionIndex)) == null) {
                    printBlankTile(bkgIndex);
                } else {
                    ChessPiece piece = chessBoard.getPiece(allChessPositions.get(positionIndex));
                    printChessPiece(piece, bkgIndex);
                }
                positionIndex++;
                if (bkgIndex == 1) bkgIndex = 0;
                else bkgIndex = 1;
            }
            rowLabel(j);
            resetBG();
            out.println();

            if (bkgIndex == 1) bkgIndex = 0;
            else bkgIndex = 1;
        }

        drawBlackHeaders();
    }

    public void createWhiteBoard(ChessBoard chessBoard){
        int bkgIndex = 0;
        int positionIndex = 63;

        drawWhiteHeaders();

        for (int j = 1; j < BOARD_SIZE + 1; j++) {
            rowLabel(j);
            for (int i = 1; i < BOARD_SIZE + 1; i += 2) {
                if (chessBoard.getPiece(allChessPositions.get(positionIndex)) == null) {
                    printBlankTile(bkgIndex);
                } else {
                    ChessPiece piece = chessBoard.getPiece(allChessPositions.get(positionIndex));
                    printChessPiece(piece, bkgIndex);
                }
                positionIndex--;
                if (bkgIndex == 1) bkgIndex = 0;
                else bkgIndex = 1;

                if (chessBoard.getPiece(allChessPositions.get(positionIndex)) == null) {
                    printBlankTile(bkgIndex);
                } else {
                    ChessPiece piece = chessBoard.getPiece(allChessPositions.get(positionIndex));
                    printChessPiece(piece, bkgIndex);
                }
                positionIndex--;
                if (bkgIndex == 1) bkgIndex = 0;
                else bkgIndex = 1;
            }
            rowLabel(j);
            resetBG();
            out.println();

            if (bkgIndex == 1) bkgIndex = 0;
            else bkgIndex = 1;
        }

        drawWhiteHeaders();
    }

    private void printChessPiece(ChessPiece piece, int bkg){
        if (piece.getPieceType() == ChessPiece.PieceType.KING){
            printKing(bkg, piece.getTeamColor());
        } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            printQueen(bkg, piece.getTeamColor());
        } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            printRook(bkg, piece.getTeamColor());
        } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            printBishop(bkg, piece.getTeamColor());
        } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            printKnight(bkg, piece.getTeamColor());
        } else if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            printPawn(bkg, piece.getTeamColor());
        }
    }

    private void drawBlackHeaders(){
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_BOLD);
        String[] headers = {"   ", " h", "   g", "   f", "  e", "   d", "   c", "  b", "   a", "    "};
        for (int i = 0; i < BOARD_SIZE + 2; i++){
            out.print(headers[i]);
        }
        resetBG();
        out.println();
    }

    private void drawWhiteHeaders(){
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_BOLD);
        String[] headers = {"   ", " a", "   b", "   c", "  d", "   e", "   f", "  g", "   h", "    "};
        for (int i = 0; i < BOARD_SIZE + 2; i++){
            out.print(headers[i]);
        }
        resetBG();
        out.println();
    }

    private void rowLabel(int index){
        setGrey();
        out.print(SET_TEXT_BOLD);
        out.print(" ");
        out.print(index);
        out.print(" ");
        resetBG();
    }

    private void printRook(int bkg, ChessGame.TeamColor txt){
        colorPicker(bkg, txt);
        if (txt == ChessGame.TeamColor.WHITE) out.print(WHITE_ROOK);
        else out.print(BLACK_ROOK);
    }

    private void printKnight(int bkg, ChessGame.TeamColor txt){
        colorPicker(bkg, txt);
        if (txt == ChessGame.TeamColor.WHITE) out.print(WHITE_KNIGHT);
        else out.print(BLACK_KNIGHT);
    }

    private void printBishop(int bkg, ChessGame.TeamColor txt){
        colorPicker(bkg, txt);
        if (txt == ChessGame.TeamColor.WHITE) out.print(WHITE_BISHOP);
        else out.print(BLACK_BISHOP);
    }

    private void printKing(int bkg, ChessGame.TeamColor txt){
        colorPicker(bkg, txt);
        if (txt == ChessGame.TeamColor.WHITE) out.print(WHITE_KING);
        else out.print(BLACK_KING);
    }

    private void printQueen(int bkg, ChessGame.TeamColor txt){
        colorPicker(bkg, txt);
        if (txt == ChessGame.TeamColor.WHITE) out.print(WHITE_QUEEN);
        else out.print(BLACK_QUEEN);
    }

    private void printPawn(int bkg, ChessGame.TeamColor txt){
        colorPicker(bkg, txt);
        if (txt == ChessGame.TeamColor.WHITE) out.print(WHITE_PAWN);
        else out.print(BLACK_PAWN);
    }

    private void colorPicker(int bkg, ChessGame.TeamColor txt){
        if (bkg == 0) whiteBG();
        else blackBG();

        if (txt == ChessGame.TeamColor.WHITE) whiteText();
        else blackText();
    }

    private void printBlankTile(int bkg){
        if (bkg == 0) whiteBG();
        else blackBG();
        out.print(EMPTY);
    }

    private static void whiteText() {
        out.print(SET_TEXT_COLOR_WHITE);
    }
    private static void blackText() {
        out.print(SET_TEXT_COLOR_BLACK);
    }
    private static void whiteBG() {
        out.print(SET_BG_COLOR_LIGHT_GREY);
    }
    private static void blackBG() {
        out.print(SET_BG_COLOR_DARK_GREEN);
    }

    private static void setGrey() {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void resetBG() {
        out.print(RESET_BG_COLOR);
    }
}
