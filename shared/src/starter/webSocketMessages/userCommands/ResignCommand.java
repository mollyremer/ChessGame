package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand{
    public ResignCommand(String authToken, Integer gameID) {
        super(authToken, gameID);
        commandType = CommandType.RESIGN;
    }
}
