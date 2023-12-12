package UserMessages;

import chess.ChessMove;
import chessCode.Move;
import webSocketMessages.userCommands.UserGameCommand;

public class MakeMoveMessage extends UserGameCommand {
    private final int gameID;
    private final Move move;

    public MakeMoveMessage(String authToken, Move move, int gameID) {
        super(authToken);
        commandType = CommandType.MAKE_MOVE;
        this.move = move;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

    public Move getMove() {
        return move;
    }
}
