package personalTests;

import chess.ChessGame;
import dao.*;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.Game;
import models.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataBaseTests {

    private Connection conn;

    @BeforeEach
    public void setup() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "sqlKatz1!1");
            var createDbStatement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS testDatabase");
            createDbStatement.executeUpdate();
            conn.setCatalog("testDatabase");

            String createAuthTable = """
                    CREATE TABLE IF NOT EXISTS authTable (
                        username varChar(255) NOT NULL,
                        authToken varChar(255) NOT NULL,
                        PRIMARY KEY (authToken)
                    )""";

            String createGameTable = """
                    CREATE TABLE IF NOT EXISTS gameTable (
                        gameID int NOT NULL,
                        whiteUsername varChar(255),
                        blackUsername varChar(255),
                        gameName varChar(255) NOT NULL,
                        game varChar(2048) NOT NULL,
                        PRIMARY KEY (gameID)
                    )""";

            String createUserTable = """
                    CREATE TABLE IF NOT EXISTS userTable (
                        username varChar(255) NOT NULL,
                        password varChar(255) NOT NULL,
                        email varChar(255) NOT NULL,
                        PRIMARY KEY (username)
                    )""";

            var createAuthTableStatement = conn.prepareStatement(createAuthTable);
            createAuthTableStatement.executeUpdate();

            var createGameTableStatement = conn.prepareStatement(createGameTable);
            createGameTableStatement.executeUpdate();

            var createUserTableStatement = conn.prepareStatement(createUserTable);
            createUserTableStatement.executeUpdate();


            AuthDao.setConn(conn);
            GameDao.setConn(conn);
            UserDao.setConn(conn);

            try {
                new AuthDao().clear();
                new GameDao().clear();
                new UserDao().clear();
            } catch (DataAccessException ex) {
                System.out.println("Database was unable to be initialized");
                System.exit(1);
            }

        } catch (SQLException ex) {
            System.out.println("Database was unable to be initialized");
            System.exit(1);
        }
    }

    @Test
    @Order(1)
    @DisplayName("AuthDao Generate Token Success")
    public void generateTokenGood() {
        String username = "Josh";
        Assertions.assertDoesNotThrow(() -> {
            AuthToken authToken = new AuthDao().generateToken(username);
            Assertions.assertNotNull(authToken);
        });
    }

    @Test
    @Order(2)
    @DisplayName("AuthDao Generate Token Failure ")
    public void generateTokenBad() {
        Assertions.assertThrows(DataAccessException.class, () -> new AuthDao().generateToken(null),
                "Null username input should result in DataAccessException");
    }


    @Test
    @Order(3)
    @DisplayName("AuthDao Remove Token Success")
    public void removeTokenGood() {
        Assertions.assertDoesNotThrow(() -> {
            String username = "Josh";
            AuthToken authToken = new AuthDao().generateToken(username);
            new AuthDao().removeToken(authToken.getAuthToken());

            var queryStatement = conn.prepareStatement("SELECT * FROM authTable");
            var rs = queryStatement.executeQuery();
            Assertions.assertFalse(rs.next(), "AuthToken was not removed");
        });
    }


    @Test
    @Order(4)
    @DisplayName("AuthDao Remove Token Failure")
    public void removeTokenBad() {
        try {
        String username = "Josh";
        AuthToken authToken = new AuthDao().generateToken(username);
            Assertions.assertDoesNotThrow( () -> {
                new AuthDao().removeToken("Ben");
                var queryStatement = conn.prepareStatement("SELECT * FROM authTable");
                var rs = queryStatement.executeQuery();
                Assertions.assertTrue(rs.next(), "Record was removed erroneously");

            }, "Threw an exception it shouldn't have");
        }
        catch (DataAccessException ex) {
            System.out.println("Problem with function setup");
        }
    }

    @Test
    @Order(5)
    @DisplayName("AuthDao Authorize Token Success")
    public void authorizeTokenGood() {
        Assertions.assertDoesNotThrow(() -> {
            String username = "Josh";
            AuthToken authToken = new AuthDao().generateToken(username);
            Assertions.assertTrue(new AuthDao().authorizeToken(authToken.getAuthToken()));
        });
    }

    @Test
    @Order(6)
    @DisplayName("AuthDao Authorize Token Failure")
    public void authorizeTokenBad() {
        Assertions.assertDoesNotThrow(() -> {
            String username = "Josh";
            AuthToken authToken = new AuthDao().generateToken(username);
            Assertions.assertFalse(new AuthDao().authorizeToken("Bad Auth Token"));
        });
    }

    @Test
    @Order(7)
    @DisplayName("AuthDao Get Username Success")
    public void getUsernameGood() {
        Assertions.assertDoesNotThrow(() -> {
            String username = "Josh";
            AuthToken authToken = new AuthDao().generateToken(username);
            Assertions.assertEquals(username, new AuthDao().getUsernameFromToken(authToken.getAuthToken()));
        });
    }

    @Test
    @Order(8)
    @DisplayName("AuthDao Get Username Failure")
    public void getUsernameBad() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            String username = "Josh";
            AuthToken authToken = new AuthDao().generateToken(username);
            Assertions.assertEquals(username, new AuthDao().getUsernameFromToken("BadAuthToken"));
        });
    }

    @Test
    @Order(9)
    @DisplayName("AuthDao Get Username Clear")
    public void authDaoClear() {
        Assertions.assertDoesNotThrow(() -> {
            String username = "Josh";
            new AuthDao().generateToken(username);
            new AuthDao().clear();
            var queryStatement = conn.prepareStatement("SELECT * FROM authTable");
            var rs = queryStatement.executeQuery();
            Assertions.assertFalse(rs.next(), "Record wasn't cleared");
        });
    }

    @Test
    @Order(10)
    @DisplayName("UserDao Add user Success")
    public void addUserGood() {

        Assertions.assertDoesNotThrow(() -> {
            User user = new User();
            user.setUsername("Josh");
            user.setPassword("Katzenbach");
            user.setEmail("joshkatzenbach@gmail.com");

            new UserDao().addUser(user);
            var queryStatement = conn.prepareStatement("SELECT * FROM userTable");
            var rs = queryStatement.executeQuery();
            Assertions.assertTrue(rs.next(), "Record wasn't added");
        });
    }

    @Test
    @Order(11)
    @DisplayName("UserDao Add user Failure")
    public void addUserBad() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            User user = new User();
            user.setUsername("Josh");
            user.setPassword(null);
            user.setEmail("joshkatzenbach@gmail.com");

            new UserDao().addUser(user);
        });
    }

    @Test
    @Order(12)
    @DisplayName("UserDao Authenticate Success")
    public void authenticateGood() {
        Assertions.assertDoesNotThrow(() -> {
            User user = new User();
            user.setUsername("Josh");
            user.setPassword("Katzenbach");
            user.setEmail("joshkatzenbach@gmail.com");
            new UserDao().addUser(user);

            Assertions.assertTrue(new UserDao().authenticate(user.getUsername(), user.getPassword()));
        });
    }

    @Test
    @Order(13)
    @DisplayName("UserDao Authenticate Success")
    public void authenticateBad() {
        Assertions.assertDoesNotThrow(() -> {
            User user = new User();
            user.setUsername("Josh");
            user.setPassword("Katzenbach");
            user.setEmail("joshkatzenbach@gmail.com");
            new UserDao().addUser(user);

            Assertions.assertFalse(new UserDao().authenticate(user.getUsername(), "Smith"));
        });
    }

    @Test
    @Order(14)
    @DisplayName("UserDao Clear")
    public void userDaoClear() {
        Assertions.assertDoesNotThrow(() -> {
            User user = new User();
            user.setUsername("Josh");
            user.setPassword("Katzenbach");
            user.setEmail("joshkatzenbach@gmail.com");
            new UserDao().addUser(user);
            new UserDao().clear();

            var queryStatement = conn.prepareStatement("SELECT * FROM userTable");
            var rs = queryStatement.executeQuery();
            Assertions.assertFalse(rs.next(), "Record wasn't cleared");
        });
    }

    @Test
    @Order(15)
    @DisplayName("gameDao Create Game Success")
    public void createGameGood() {
        Assertions.assertDoesNotThrow(() -> {
            int gameID = new GameDao().createGame("MyGame");

            var queryStatement = conn.prepareStatement("SELECT * FROM gameTable");
            var rs = queryStatement.executeQuery();
            Assertions.assertTrue(rs.next(), "Game wasn't created");
            Assertions.assertNotEquals(0, gameID);
        });
    }

    @Test
    @Order(16)
    @DisplayName("GameDao Create Game Failure")
    public void createGameBad() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            int gameID = new GameDao().createGame(null);
        });
    }

    @Test
    @Order(17)
    @DisplayName("GameDao Return All Success")
    public void returnAllGood() {
        Assertions.assertDoesNotThrow(() -> {
            new GameDao().createGame("Game 1");
            new GameDao().createGame("Game 2");

            ArrayList<Game> games = new GameDao().returnAll();
            Assertions.assertEquals(2, games.size());
        });
    }

    @Test
    @Order(18)
    @DisplayName("GameDao Join Success")
    public void joinGood() {
        Assertions.assertDoesNotThrow(() -> {
            int gameID = new GameDao().createGame("MyGame");
            Assertions.assertEquals(1, new GameDao().joinGame(gameID, "Josh", ChessGame.TeamColor.WHITE));
        });
    }

    @Test
    @Order(19)
    @DisplayName("GameDao Join Failure")
    public void joinBad() {
        Assertions.assertDoesNotThrow(() -> {
            int gameID = new GameDao().createGame("MyGame");
            new GameDao().joinGame(gameID, "Ben", ChessGame.TeamColor.WHITE);
            Assertions.assertEquals(0, new GameDao().joinGame(gameID, "Josh", ChessGame.TeamColor.WHITE ));
        });
    }

    @Test
    @Order(20)
    @DisplayName("GameDao Clear")
    public void gameDaoClear() {
        Assertions.assertDoesNotThrow(() -> {
            int gameID = new GameDao().createGame("MyGame");
            new GameDao().clear();


            var queryStatement = conn.prepareStatement("SELECT * FROM gameTable");
            var rs = queryStatement.executeQuery();
            Assertions.assertFalse(rs.next(), "Record wasn't cleared");
        });
    }

}




