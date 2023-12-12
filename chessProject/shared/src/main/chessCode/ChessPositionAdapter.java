package chessCode;

import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ChessPositionAdapter extends TypeAdapter<ChessPosition> {

    @Override
    public void write(JsonWriter jsonWriter, ChessPosition position) throws IOException {
        Gson gson = new Gson();

        if (position == null) {
            jsonWriter.name("type").nullValue();
        }
        else {
            gson.getAdapter(ChessSquare.class).write(jsonWriter, (ChessSquare) position);
        }
    }

    @Override
    public ChessSquare read(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        Integer row = null;
        Integer column = null;

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "row" -> row = jsonReader.nextInt();
                case "column" -> column = jsonReader.nextInt();
            }
        }

        jsonReader.endObject();
        if ((row == null) || (column == null)) {
            throw new IOException("Move did not contain positions");
        }
        return new ChessSquare(row, column);
    }
}
