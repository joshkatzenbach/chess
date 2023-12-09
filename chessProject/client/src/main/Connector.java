import com.google.gson.Gson;

import java.io.IOException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import models.*;


public class Connector {
    private final String baseURL;

    public Connector(String baseURL) {
        this.baseURL = baseURL;
    }
    public HttpURLConnection makeRequest(String extendURL, String method, Object object, AuthToken authToken) throws IOException, URISyntaxException {
        URI uri = new URI(baseURL + extendURL);
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod(method);

        if (authToken != null) {
            http.setDoOutput(true);
            http.addRequestProperty("authorization", authToken.getAuthToken());
        }

        if (object != null) {
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");

            var outputStream = http.getOutputStream();
            var jsonBody = new Gson().toJson(object);
            outputStream.write(jsonBody.getBytes());
        }

        http.connect();

        return http;
    }

    public Object getResponseBody(HttpURLConnection http, Class<?> classType) throws IOException{
        Object responseBody = "";
        InputStream respBody = http.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(respBody);
        responseBody = new Gson().fromJson(inputStreamReader, classType);
        return responseBody;
    }

}
