package chessCode;

import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ChessPieceAdapter extends TypeAdapter<ChessPiece> {
    @Override
    public void write(JsonWriter jsonWriter, ChessPiece chessPiece) throws IOException {
        Gson gson = new Gson();

        if (chessPiece == null) {
            jsonWriter.nullValue();
        }
        else {
            switch (chessPiece.getPieceType()) {
                case ROOK -> gson.getAdapter(Rook.class).write(jsonWriter, (Rook) chessPiece);
                case PAWN -> gson.getAdapter(Pawn.class).write(jsonWriter, (Pawn) chessPiece);
                case KING -> gson.getAdapter(King.class).write(jsonWriter, (King) chessPiece);
                case QUEEN -> gson.getAdapter(Queen.class).write(jsonWriter, (Queen) chessPiece);
                case KNIGHT -> gson.getAdapter(Knight.class).write(jsonWriter, (Knight) chessPiece);
                case BISHOP -> gson.getAdapter(Bishop.class).write(jsonWriter, (Bishop) chessPiece);
            }
        }
    }

    @Override
    public ChessPiece read(JsonReader jsonReader) throws IOException {

        JsonToken token = jsonReader.peek();
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        jsonReader.beginObject();

        ChessGame.TeamColor color = null;
        ChessPiece.PieceType type = null;

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "color" -> color = ChessGame.TeamColor.valueOf(jsonReader.nextString());
                case "type" -> type = ChessPiece.PieceType.valueOf(jsonReader.nextString());
            }
        }

        jsonReader.endObject();

        if (type == null) {
            return null;
        }
        else {
            return switch(type) {
                case ROOK -> new Rook(color);
                case KING -> new King(color);
                case QUEEN -> new Queen(color);
                case PAWN -> new Pawn(color);
                case KNIGHT -> new Knight(color);
                case BISHOP -> new Bishop(color);
            };
        }
    }

}
