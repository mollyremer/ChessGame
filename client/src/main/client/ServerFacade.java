package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.AuthToken;
import requests.*;
import results.*;

import java.io.*;
import java.net.*;
import java.util.Objects;

import static client.Client.clientAuthToken;
import static java.lang.System.out;

public class ServerFacade {
    private final String serverURL;

    public ServerFacade(String serverURL){ this.serverURL = serverURL; }

    public RegisterResult register(RegisterRequest request){
        try{
            URI uri = new URI(serverURL + "/user");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");

            try (OutputStream outputStream = http.getOutputStream()){
                String requestBody = new Gson().toJson(request);
                outputStream.write(requestBody.getBytes());
            }

            //check response
            int responseCode = http.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                try (InputStream inputStream = http.getInputStream()) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder input = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null){
                        input.append(line);
                    }
                    Gson gson = new Gson();
                    RegisterResult registerResult = gson.fromJson(input.toString(), RegisterResult.class);
                    clientAuthToken.setAuthToken(registerResult.getAuthToken());
                    clientAuthToken.setUsername(registerResult.getUsername());
                    return registerResult;
                }
            } else {
                try (InputStream errorStream = http.getErrorStream()) {
                    if (errorStream != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(errorStream));
                        StringBuilder errorMessage = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            errorMessage.append(line);
                        }
                        JsonObject errorJson = JsonParser.parseString(errorMessage.toString()).getAsJsonObject();
                        String errorMessageText = errorJson.has("message") ? errorJson.get("message").getAsString() : "An error occurred, try again";
                        out.println(Objects.requireNonNullElse(errorMessageText, "An error occurred, try again"));
                        out.println();
                    }
                    return null;
                }
            }
        } catch (ConnectException e){
            out.println("Cannot connect to server");
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public LoginResult login(LoginRequest request){
        try{
            URI uri = new URI(serverURL + "/session");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");

            try (OutputStream outputStream = http.getOutputStream()){
                String requestBody = new Gson().toJson(request);
                outputStream.write(requestBody.getBytes());
            }

            //check response
            int responseCode = http.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                try (InputStream inputStream = http.getInputStream()) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder input = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null){
                        input.append(line);
                    }
                    Gson gson = new Gson();
                    LoginResult loginResult = gson.fromJson(input.toString(), LoginResult.class);
                    clientAuthToken.setAuthToken(loginResult.getAuthToken());
                    clientAuthToken.setUsername(loginResult.getUsername());
                    return loginResult;
                }
            } else {
                try (InputStream errorStream = http.getErrorStream()) {
                    if (errorStream != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(errorStream));
                        StringBuilder errorMessage = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            errorMessage.append(line);
                        }
                        JsonObject errorJson = JsonParser.parseString(errorMessage.toString()).getAsJsonObject();
                        String errorMessageText = errorJson.has("message") ? errorJson.get("message").getAsString() : "An error occurred, try again";
                        out.println(Objects.requireNonNullElse(errorMessageText, "An error occurred, try again"));
                        out.println();
                    }
                    return null;
                }
            }
        } catch (ConnectException e){
            out.println("Cannot connect to server");
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    //FOR TESTING PURPOSES ONLY - CANNOT BE ACCESSED BY CLIENT
    public void clear(){
        try {
            URI uri = new URI(serverURL + "/db");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("DELETE");

            int responseCode = http.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                out.println("Successfully cleared databases");
            } else {
                out.println("There was an error");
            }
        } catch (ConnectException e){
            out.println("Cannot connect to server");
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DefaultResult logout(){
        try{
            URI uri = new URI(serverURL + "/session");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestProperty("Authorization", clientAuthToken.getAuthToken());
            http.setRequestMethod("DELETE");

            int responseCode = http.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream resBody = http.getInputStream()) {
                    return new DefaultResult();
                }
            } else {
                try (InputStream errorStream = http.getErrorStream()) {
                    if (errorStream != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(errorStream));
                        StringBuilder errorMessage = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            errorMessage.append(line);
                        }
                        JsonObject errorJson = JsonParser.parseString(errorMessage.toString()).getAsJsonObject();
                        String errorMessageText = errorJson.has("message") ? errorJson.get("message").getAsString() : "An error occurred, try again";
                        out.println(Objects.requireNonNullElse(errorMessageText, "An error occurred, try again"));
                        out.println();
                    }
                }
                return null;
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DefaultResult createGame(CreateRequest request){
        try{
            URI uri = new URI(serverURL + "/game");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestProperty("Authorization", clientAuthToken.getAuthToken());
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");

            try (OutputStream outputStream = http.getOutputStream()){
                String requestBody = new Gson().toJson(request);
                outputStream.write(requestBody.getBytes());
            }

            int responseCode = http.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream resBody = http.getInputStream()) {
                    out.println("Successfully created " + request.getGameName());
                    out.println();
                    return new DefaultResult();
                }
            } else {
                try (InputStream errorStream = http.getErrorStream()) {
                    if (errorStream != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(errorStream));
                        StringBuilder errorMessage = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            errorMessage.append(line);
                        }
                        JsonObject errorJson = JsonParser.parseString(errorMessage.toString()).getAsJsonObject();
                        String errorMessageText = errorJson.has("message") ? errorJson.get("message").getAsString() : "An error occurred, try again";
                        out.println(Objects.requireNonNullElse(errorMessageText, "An error occurred, try again"));
                        out.println();
                    }
                }
                return null;
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    public ListResult listGames(){
        try{
            URI uri = new URI(serverURL + "/game");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestProperty("Authorization", clientAuthToken.getAuthToken());
            http.setRequestMethod("GET");

            int responseCode = http.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = http.getInputStream()) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder input = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null){
                        input.append(line);
                    }
                    Gson gson = new Gson();
                    return gson.fromJson(input.toString(), ListResult.class);
                }
            } else {
                try (InputStream errorStream = http.getErrorStream()) {
                    if (errorStream != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(errorStream));
                        StringBuilder errorMessage = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            errorMessage.append(line);
                        }
                        JsonObject errorJson = JsonParser.parseString(errorMessage.toString()).getAsJsonObject();
                        String errorMessageText = errorJson.has("message") ? errorJson.get("message").getAsString() : "An error occurred, try again";
                        out.println(Objects.requireNonNullElse(errorMessageText, "An error occurred, try again"));
                        out.println();
                    }
                }
                return null;
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DefaultResult joinGame(JoinRequest request){
        try{
            URI uri = new URI(serverURL + "/game");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestProperty("Authorization", clientAuthToken.getAuthToken());
            http.setRequestMethod("PUT");
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");

            try (OutputStream outputStream = http.getOutputStream()){
                String requestBody = new Gson().toJson(request);
                outputStream.write(requestBody.getBytes());
            }

            int responseCode = http.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream resBody = http.getInputStream()) {
                    return new DefaultResult();
                }
            } else {
                try (InputStream errorStream = http.getErrorStream()) {
                    if (errorStream != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(errorStream));
                        StringBuilder errorMessage = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            errorMessage.append(line);
                        }
                        JsonObject errorJson = JsonParser.parseString(errorMessage.toString()).getAsJsonObject();
                        String errorMessageText = errorJson.has("message") ? errorJson.get("message").getAsString() : "An error occurred, try again";
                        out.println(Objects.requireNonNullElse(errorMessageText, "An error occurred, try again"));
                        out.println();
                    }
                }
                return null;
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}