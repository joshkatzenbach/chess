package personalTests;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import services.*;
import results.*;
import requests.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonalServerTests {

    private static final int HTTP_OK = 200;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_UNAUTHORIZED = 401;
    private static final int HTTP_FORBIDDEN = 403;



    @BeforeEach
    public void setup() {
        new ClearService().clear();
    }

    @Test
    @Order(1)
    @DisplayName("Normal User Registration")
    public void NormalRegistration() {
        RegisterRequest request = new RegisterRequest();
        RegisterService service = new RegisterService();

        request.setUsername("Josh");
        request.setPassword("Katzenbach");
        request.setEmail("joshkatzenbach@gmail.com");

        RegisterResult result = service.register(request);

        Assertions.assertEquals(HTTP_OK, result.getErrorCode(), "Error code was incorrect.");
        Assertions.assertEquals("Josh", result.getUsername(), "Username was incorrect");
        Assertions.assertNotNull(result.getAuthToken(), "AuthToken was not returned");
    }

    @Test
    @Order(2)
    @DisplayName("Repeat Registration)")
    public void RepeatRegistration() {
        RegisterRequest request = new RegisterRequest();
        RegisterRequest request2 = new RegisterRequest();
        RegisterService service = new RegisterService();

        request.setUsername("Josh");
        request.setPassword("Katzenbach");
        request.setEmail("joshkatzenbach@gmail.com");

        request2.setUsername("Josh");
        request2.setPassword("Johnson");
        request2.setEmail("josh@gmail.com");

        service.register(request);
        RegisterResult result = service.register(request2);

        Assertions.assertEquals(HTTP_FORBIDDEN, result.getErrorCode(), "Wrong error code returned");
        Assertions.assertNull(result.getAuthToken(), "No AuthToken should be included.");
        Assertions.assertNull(result.getUsername(), "No Username should be included");
    }

    @Test
    @Order(3)
    @DisplayName("Normal Login")
    public void NormalLogin() {
        RegisterRequest registerRequest = new RegisterRequest();
        RegisterService service = new RegisterService();
        LoginRequest loginRequest = new LoginRequest();

        registerRequest.setUsername("Josh");
        registerRequest.setPassword("Katzenbach");
        registerRequest.setEmail("joshkatzenbach@gmail.com");

        service.register(registerRequest);

        loginRequest.setPassword("Katzenbach");
        loginRequest.setUsername("Josh");

        LoginResult result = new LoginService().login(loginRequest);

        Assertions.assertEquals(HTTP_OK, result.getErrorCode(), "Incorrect error code");
        Assertions.assertEquals("Josh", result.getUsername(), "Incorrect username");
        Assertions.assertNotNull(result.getAuthToken(), "AuthToken not included");
    }

    @Test
    @Order(4)
    @DisplayName("Bad Login")
    public void IncorrectPassword() {
        RegisterRequest registerRequest = new RegisterRequest();
        RegisterService service = new RegisterService();
        LoginRequest loginRequest = new LoginRequest();

        registerRequest.setUsername("Josh");
        registerRequest.setPassword("Katzenbach");
        registerRequest.setEmail("joshkatzenbach@gmail.com");

        service.register(registerRequest);

        loginRequest.setPassword("Johnson");
        loginRequest.setUsername("Josh");

        LoginResult result = new LoginService().login(loginRequest);

        Assertions.assertEquals(HTTP_UNAUTHORIZED, result.getErrorCode(), "Incorrect error code");
    }

    @Test
    @Order(5)
    @DisplayName("Normal Logout")
    public void NormalLogout() {
        RegisterRequest registerRequest = new RegisterRequest();
        RegisterService service = new RegisterService();
        LoginRequest loginRequest = new LoginRequest();

        registerRequest.setUsername("Josh");
        registerRequest.setPassword("Katzenbach");
        registerRequest.setEmail("joshkatzenbach@gmail.com");

        service.register(registerRequest);

        loginRequest.setUsername("Josh");
        loginRequest.setPassword("Katzenbach");


        String existingToken = new LoginService().login(loginRequest).getAuthToken();

        LogoutResult result = new LogoutService().logout(existingToken);

        Assertions.assertEquals(HTTP_OK, result.getErrorCode(), "Wrong Error Code");
    }

    @Test
    @Order(6)
    @DisplayName("Bad Logout")
    public void BadLogout() {
        RegisterRequest registerRequest = new RegisterRequest();
        RegisterService service = new RegisterService();
        LoginRequest loginRequest = new LoginRequest();

        registerRequest.setUsername("Josh");
        registerRequest.setPassword("Katzenbach");
        registerRequest.setEmail("joshkatzenbach@gmail.com");

        service.register(registerRequest);

        loginRequest.setUsername("Josh");
        loginRequest.setPassword("Katzenbach");


        String existingToken = new LoginService().login(loginRequest).getAuthToken();
        String badToken = "BadLoginToken";

        LogoutResult result = new LogoutService().logout(badToken);

        Assertions.assertEquals(HTTP_UNAUTHORIZED, result.getErrorCode(), "Wrong Error Code");
    }

    @Test
    @Order(7)
    @DisplayName("Normal Create Game")
    public void NormalCreateGame() {
        RegisterRequest registerRequest = new RegisterRequest();
        RegisterService service = new RegisterService();
        LoginRequest loginRequest = new LoginRequest();
        CreateGameRequest createGameRequest = new CreateGameRequest();

        registerRequest.setUsername("Josh");
        registerRequest.setPassword("Katzenbach");
        registerRequest.setEmail("joshkatzenbach@gmail.com");

        service.register(registerRequest);

        loginRequest.setUsername("Josh");
        loginRequest.setPassword("Katzenbach");


        String existingToken = new LoginService().login(loginRequest).getAuthToken();

        createGameRequest.setGameName("Josh's Game");
        CreateGameResult result = new CreateGameService().createGame(createGameRequest, existingToken);

        Assertions.assertEquals(HTTP_OK, result.getErrorCode(), "Wrong Error Code");
        Assertions.assertNotEquals(0, result.getGameID(), "Invalid Game ID");
    }

    @Test
    @Order(8)
    @DisplayName("Missing Game Name")
    public void MissingGameName() {
        RegisterRequest registerRequest = new RegisterRequest();
        RegisterService service = new RegisterService();
        LoginRequest loginRequest = new LoginRequest();
        CreateGameRequest createGameRequest = new CreateGameRequest();

        registerRequest.setUsername("Josh");
        registerRequest.setPassword("Katzenbach");
        registerRequest.setEmail("joshkatzenbach@gmail.com");

        service.register(registerRequest);

        loginRequest.setUsername("Josh");
        loginRequest.setPassword("Katzenbach");


        String existingToken = new LoginService().login(loginRequest).getAuthToken();

        CreateGameResult result = new CreateGameService().createGame(createGameRequest, existingToken);

        Assertions.assertEquals(HTTP_BAD_REQUEST, result.getErrorCode(), "Wrong Error Code");
        Assertions.assertEquals(0, result.getGameID(), "Should not return valid gameID");
    }

    @Test
    @Order(9)
    @DisplayName("Normal Game List")
    public void NormalGameList() {
        RegisterRequest registerRequest = new RegisterRequest();
        CreateGameRequest createGameRequest = new CreateGameRequest();

        registerRequest.setUsername("Josh");
        registerRequest.setPassword("Katzenbach");
        registerRequest.setEmail("joshkatzenbach@gmail.com");

        String existingToken = new RegisterService().register(registerRequest).getAuthToken();

        createGameRequest.setGameName("Game 1");
        new CreateGameService().createGame(createGameRequest, existingToken);
        createGameRequest.setGameName("Game 2");
        new CreateGameService().createGame(createGameRequest, existingToken);

        ListGamesResult result = new ListGamesService().listGames(existingToken);



        Assertions.assertEquals(HTTP_OK, result.getErrorCode(), "Wrong Error Code");
        Assertions.assertNotNull(result.getGames(), "Games not returned");
    }

    @Test
    @Order(10)
    @DisplayName("Game List Bad Token")
    public void GameListBadToken() {
        RegisterRequest registerRequest = new RegisterRequest();
        CreateGameRequest createGameRequest = new CreateGameRequest();

        registerRequest.setUsername("Josh");
        registerRequest.setPassword("Katzenbach");
        registerRequest.setEmail("joshkatzenbach@gmail.com");

        String existingToken = new RegisterService().register(registerRequest).getAuthToken();

        createGameRequest.setGameName("Game 1");
        new CreateGameService().createGame(createGameRequest, existingToken);
        createGameRequest.setGameName("Game 2");
        new CreateGameService().createGame(createGameRequest, existingToken);

        String badToken = "InvalidToken";

        ListGamesResult result = new ListGamesService().listGames(badToken);



        Assertions.assertEquals(HTTP_UNAUTHORIZED, result.getErrorCode(), "Wrong Error Code");
        Assertions.assertNull(result.getGames(), "Games should not be returned");
    }

    @Test
    @Order(11)
    @DisplayName("Normal Join Game")
    public void NormalJoinGame() {
        RegisterRequest registerRequest = new RegisterRequest();
        CreateGameRequest createGameRequest = new CreateGameRequest();
        JoinGameRequest joinGameRequest = new JoinGameRequest();

        registerRequest.setUsername("Josh");
        registerRequest.setPassword("Katzenbach");
        registerRequest.setEmail("joshkatzenbach@gmail.com");

        String existingToken = new RegisterService().register(registerRequest).getAuthToken();

        createGameRequest.setGameName("Game 1");
        int gameID = new CreateGameService().createGame(createGameRequest, existingToken).getGameID();

        joinGameRequest.setGameID(gameID);
        joinGameRequest.setPlayerColor(ChessGame.TeamColor.WHITE);
        JoinGameResult result = new JoinGameService().joinGame(joinGameRequest, existingToken);


        Assertions.assertEquals(HTTP_OK, result.getErrorCode(), "Wrong Error Code");
    }

    @Test
    @Order(12)
    @DisplayName("Repeat Join Game")
    public void RepeatJoinGame() {
        RegisterRequest registerRequest = new RegisterRequest();
        CreateGameRequest createGameRequest = new CreateGameRequest();
        JoinGameRequest joinGameRequest = new JoinGameRequest();

        registerRequest.setUsername("Josh");
        registerRequest.setPassword("Katzenbach");
        registerRequest.setEmail("joshkatzenbach@gmail.com");

        String existingToken = new RegisterService().register(registerRequest).getAuthToken();

        createGameRequest.setGameName("Game 1");
        int gameID = new CreateGameService().createGame(createGameRequest, existingToken).getGameID();

        joinGameRequest.setGameID(gameID);
        joinGameRequest.setPlayerColor(ChessGame.TeamColor.WHITE);
        new JoinGameService().joinGame(joinGameRequest, existingToken);
        JoinGameResult result = new JoinGameService().joinGame(joinGameRequest, existingToken);


        Assertions.assertEquals(HTTP_FORBIDDEN, result.getErrorCode(), "Wrong Error Code");
    }

    @Test
    @Order(12)
    @DisplayName("Clear Test")
    public void ClearTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        CreateGameRequest createGameRequest = new CreateGameRequest();
        JoinGameRequest joinGameRequest = new JoinGameRequest();

        registerRequest.setUsername("Josh");
        registerRequest.setPassword("Katzenbach");
        registerRequest.setEmail("joshkatzenbach@gmail.com");

        String existingToken = new RegisterService().register(registerRequest).getAuthToken();

        createGameRequest.setGameName("Game 1");
        int gameID = new CreateGameService().createGame(createGameRequest, existingToken).getGameID();

        ClearResult result = new ClearService().clear();


        Assertions.assertEquals(HTTP_OK, result.getErrorCode(), "Wrong Error Code");
    }















}
