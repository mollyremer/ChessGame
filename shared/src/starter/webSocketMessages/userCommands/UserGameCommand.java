package webSocketMessages.userCommands;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 */
public class UserGameCommand {

    public UserGameCommand(String authToken, Integer gameID) {
        this.authToken = authToken;
        this.gameID = gameID;
    }

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;

    private final String authToken;

    private final Integer gameID;

    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() { return this.commandType; }

    public Integer getGameID() { return gameID; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserGameCommand)) return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}