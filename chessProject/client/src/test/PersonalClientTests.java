import models.AuthToken;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Scanner;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonalClientTests {
    private Connector connector;

    private PreSession preLogin;

    @BeforeEach
    void setupEach() {
        connector = new Connector("http://localhost:8080");
        preLogin = new PreSession(connector);
        try {
            HttpURLConnection http = connector.makeRequest("/db", "DELETE", null, null);
            if (http.getResponseCode() != 200) {
                System.out.println("Could not clear database");
                System.exit(1);
            }
        }
        catch (IOException | URISyntaxException ex) {
            System.out.println("Could not clear database");
            System.exit(1);
        }
    }
    @AfterAll
    public static void close() {
        Connector connector = new Connector("http://localhost:8080");
        try {
            HttpURLConnection http = connector.makeRequest("/db", "DELETE", null, null);
            if (http.getResponseCode() != 200) {
                System.out.println("Could not clear database");
                System.exit(1);
            }
        }
        catch (IOException | URISyntaxException ex) {
            System.out.println("Could not clear database");
            System.exit(1);
        }
    }


    @Test
    @Order(1)
    @DisplayName("Good Register")
    void goodRegister() {
        String [] args = {"register","Josh", "Katzenbach", "joshkatzenbach@gmail.com"};

        Assertions.assertDoesNotThrow( () -> {
            Assertions.assertTrue(preLogin.register(args), "Registration was not successful.");
        });
    }

    @Test
    @Order(2)
    @DisplayName("Duplicate Register")
    void badRegister() {
        String [] args1 = {"register","Josh", "Katzenbach", "joshkatzenbach@gmail.com"};
        String [] args2 = {"register","Josh", "Smith", "joshSmith@gmail.com"};

        Assertions.assertDoesNotThrow( () -> {
            preLogin.register(args1);
            Assertions.assertFalse(preLogin.register(args2), "Registration was successful.");
        });
    }

    @Test
    @Order(3)
    @DisplayName("Good Login")
    void goodLogin() {
        String [] args1 = {"register","Josh", "Katzenbach", "joshkatzenbach@gmail.com"};
        String [] args2 = {"login", "Josh", "Katzenbach"};

        Assertions.assertDoesNotThrow( () -> {
            preLogin.register(args1);
            Assertions.assertTrue(preLogin.login(args2), "Login was not successful");
        });
    }

    @Test
    @Order(4)
    @DisplayName("Bad Login")
    void badLogin() {
        String [] args1 = {"register","Josh", "Katzenbach", "joshkatzenbach@gmail.com"};
        String [] args2 = {"login", "Josh", "Smith"};

        Assertions.assertDoesNotThrow( () -> {
            preLogin.register(args1);
            Assertions.assertFalse(preLogin.login(args2), "Login was not successful");
        });
    }

    @Test
    @Order(5)
    @DisplayName("Good Logout")
    void goodLogout() {
        String [] args1 = {"register","Josh", "Katzenbach", "joshkatzenbach@gmail.com"};
        String [] args2 = {"logout"};

        Assertions.assertDoesNotThrow( () -> {
            preLogin.register(args1);
            AuthToken authToken = preLogin.getAuthToken();
            LoginSession session = new LoginSession(authToken.getAuthToken(), authToken.getUsername(), new Scanner(System.in), connector);
            Assertions.assertTrue(session.logout(args2), "Could not logout");
        });
    }

    @Test
    @Order(6)
    @DisplayName("Bad Logout")
    void badLogout() {
        String[] args1 = {"register", "Josh", "Katzenbach", "joshkatzenbach@gmail.com"};
        String[] args2 = {"logout", "Josh"};

        Assertions.assertDoesNotThrow(() -> {
            preLogin.register(args1);
            AuthToken authToken = preLogin.getAuthToken();
            LoginSession session = new LoginSession(authToken.getAuthToken(), authToken.getUsername(), new Scanner(System.in), connector);
            Assertions.assertFalse(session.logout(args2), "Could not logout");
        });
    }

    @Test
    @Order(7)
    @DisplayName("Good Create Game")
    void goodCreate() {
        String[] args1 = {"register", "Josh", "Katzenbach", "joshkatzenbach@gmail.com"};
        String[] args2 = {"Create", "FirstGame"};

        Assertions.assertDoesNotThrow(() -> {
            preLogin.register(args1);
            AuthToken authToken = preLogin.getAuthToken();
            LoginSession session = new LoginSession(authToken.getAuthToken(), authToken.getUsername(), new Scanner(System.in), connector);
            Assertions.assertTrue(session.createGame(args2));
        });
    }
    @Test
    @Order(8)
    @DisplayName("Bad Create")
    void badCreate() {
        String[] args1 = {"register", "Josh", "Katzenbach", "joshkatzenbach@gmail.com"};
        String[] args2 = {"create", "FirstGame"};

        Assertions.assertDoesNotThrow(() -> {
            preLogin.register(args1);
            AuthToken authToken = new AuthToken();
            authToken.setUsername("Josh");
            authToken.setAuthToken("Bad Auth Token");
            LoginSession session = new LoginSession(authToken.getAuthToken(), authToken.getUsername(), new Scanner(System.in), connector);
            Assertions.assertFalse(session.createGame(args2));
        });
    }
    @Test
    @Order(9)
    @DisplayName("Good List")
    void goodList() {
        String[] args1 = {"register", "Josh", "Katzenbach", "joshkatzenbach@gmail.com"};
        String[] args2 = {"create", "FirstGame"};
        String[] args3 = {"create", "SecondGame"};
        String[] args4 = {"create", "Third"};
        String[] args5 = {"list"};

        Assertions.assertDoesNotThrow(() -> {
            preLogin.register(args1);
            AuthToken authToken = preLogin.getAuthToken();
            LoginSession session = new LoginSession(authToken.getAuthToken(), authToken.getUsername(), new Scanner(System.in), connector);
            session.createGame(args2);
            session.createGame(args3);
            session.createGame(args4);
            Assertions.assertTrue(session.listGames(args5), "Could not list games");
            Map<String, Integer> games = session.getGames();
            Assertions.assertEquals(3, games.size(), "Did not list all games");
        });
    }

    @Test
    @Order(10)
    @DisplayName("Bad List")
    void badList() {
        String[] args1 = {"register", "Josh", "Katzenbach", "joshkatzenbach@gmail.com"};
        String[] args2 = {"create", "FirstGame"};
        String[] args3 = {"create", "SecondGame"};
        String[] args4 = {"create", "Third"};
        String[] args5 = {"list"};

        Assertions.assertDoesNotThrow(() -> {
            preLogin.register(args1);
            AuthToken authToken = new AuthToken();
            authToken.setUsername("Josh");
            authToken.setAuthToken("Bad Auth Token");
            LoginSession session = new LoginSession(authToken.getAuthToken(), authToken.getUsername(), new Scanner(System.in), connector);
            session.createGame(args2);
            session.createGame(args3);
            session.createGame(args4);
            Assertions.assertFalse(session.listGames(args5), "Could not list games");
        });
    }

    @Test
    @Order(11)
    @DisplayName("Good Join")
    void goodJoin() {
        String[] args1 = {"register", "Josh", "Katzenbach", "joshkatzenbach@gmail.com"};
        String[] args2 = {"create", "FirstGame"};
        String[] args3 = {"create", "SecondGame"};
        String[] args4 = {"create", "Third"};
        String[] args5 = {"list"};
        String[] args6 = {"join", "2", "white"};

        Assertions.assertDoesNotThrow(() -> {
            preLogin.register(args1);
            AuthToken authToken = preLogin.getAuthToken();
            LoginSession session = new LoginSession(authToken.getAuthToken(), authToken.getUsername(), new Scanner(System.in), connector);
            session.createGame(args2);
            session.createGame(args3);
            session.createGame(args4);
            session.listGames(args5);

            Assertions.assertTrue(session.joinGame(args6));
        });
    }

    @Test
    @Order(12)
    @DisplayName("Bad Join")
    void badJoin() {
        String[] args1 = {"register", "Josh", "Katzenbach", "joshkatzenbach@gmail.com"};
        String[] args2 = {"create", "FirstGame"};
        String[] args3 = {"create", "SecondGame"};
        String[] args4 = {"create", "ThirdGame"};
        String[] args5 = {"list"};
        String[] args6 = {"join", "2", "white"};

        Assertions.assertDoesNotThrow(() -> {
            preLogin.register(args1);
            AuthToken authToken = preLogin.getAuthToken();
            LoginSession session = new LoginSession(authToken.getAuthToken(), authToken.getUsername(), new Scanner(System.in), connector);
            session.createGame(args2);
            session.createGame(args3);
            session.createGame(args4);
            session.listGames(args5);

            Assertions.assertTrue(session.joinGame(args6));
            Assertions.assertFalse(session.joinGame(args6));
        });
    }

    @Test
    @Order(13)
    @DisplayName("Observe Join")
    void observeJoin() {
        String[] args1 = {"register", "Josh", "Katzenbach", "joshkatzenbach@gmail.com"};
        String[] args2 = {"create", "FirstGame"};
        String[] args3 = {"create", "SecondGame"};
        String[] args4 = {"create", "ThirdGame"};
        String[] args5 = {"list"};
        String[] args6 = {"join", "2"};

        Assertions.assertDoesNotThrow(() -> {
            preLogin.register(args1);
            AuthToken authToken = preLogin.getAuthToken();
            LoginSession session = new LoginSession(authToken.getAuthToken(), authToken.getUsername(), new Scanner(System.in), connector);
            session.createGame(args2);
            session.createGame(args3);
            session.createGame(args4);
            session.listGames(args5);

            Assertions.assertTrue(session.joinGame(args6));
        });
    }

    @Test
    @Order(13)
    @DisplayName("Bad Color Join")
    void badColorJoin() {
        String[] args1 = {"register", "Josh", "Katzenbach", "joshkatzenbach@gmail.com"};
        String[] args2 = {"create", "FirstGame"};
        String[] args3 = {"create", "SecondGame"};
        String[] args4 = {"create", "ThirdGame"};
        String[] args5 = {"list"};
        String[] args6 = {"join", "2", "GREEN"};

        Assertions.assertDoesNotThrow(() -> {
            preLogin.register(args1);
            AuthToken authToken = preLogin.getAuthToken();
            LoginSession session = new LoginSession(authToken.getAuthToken(), authToken.getUsername(), new Scanner(System.in), connector);
            session.createGame(args2);
            session.createGame(args3);
            session.createGame(args4);
            session.listGames(args5);

            Assertions.assertFalse(session.joinGame(args6));
        });
    }
}
