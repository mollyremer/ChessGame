package chess.gsonAdapters;

import chess.ChessMove;
import chess.impl.ChessMoveImpl;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ChessMoveAdapter implements JsonDeserializer<ChessMove> {
    @Override
    public ChessMove deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return jsonDeserializationContext.deserialize(jsonElement, ChessMoveImpl.class);
    }
}
