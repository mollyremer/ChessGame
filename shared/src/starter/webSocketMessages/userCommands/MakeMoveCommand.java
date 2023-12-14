package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    ChessMove move;
    public MakeMoveCommand(String authToken, Integer gameID, ChessMove move) {
        super(authToken, gameID);
        this.move = move;
        commandType = CommandType.MAKE_MOVE;
    }

    public ChessMove getMove() {
        return move;
    }

    public void setMove(ChessMove move) {
        this.move = move;
    }
}
