package ui;

import client.ServerFacade;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.exit;
import static ui.EscapeSequences.*;

public class PreLoginUI {
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final ServerFacade server = new ServerFacade("http://localhost:8080");

    public PreLoginUI(){
        Scanner scanner = new Scanner(System.in);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.println("Play Chess!");

        startScreen(scanner);
    }


    public void startScreen(Scanner scanner){
        helpUI();

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if ((Objects.equals(line, "help")) || (Objects.equals(line, "4"))){
                helpUI();
            } else if ((Objects.equals(line, "register")) || (Objects.equals(line, "1"))) {
                registerUI(scanner);
            } else if ((Objects.equals(line, "login")) || (Objects.equals(line, "2"))){
                loginUI(scanner);
            } else if ((Objects.equals(line, "quit")) || (Objects.equals(line, "3"))){
                exit(0);
            }
        }
    }

    private void helpUI(){
        out.println("Type the number to execute the command:");
        out.println("1) register");
        out.println("2) login");
        out.println("3) quit");
        out.println("4) help");
    }

    private void registerUI(Scanner scanner){
        out.print("new username:");
        String username = scanner.nextLine();
        out.print("create password:");
        String password = scanner.nextLine();
        out.print("email:");
        String email = scanner.nextLine();

        RegisterRequest rq = new RegisterRequest(username, password, email);
        RegisterResult result = server.register(rq);
        if (result == null || result.getMessage() != null){
            startScreen(scanner);
        } else{
            out.println("Successfully registered and logged in");
            out.println();
            new PostLoginUI();
        }
    }

    private void loginUI(Scanner scanner){
        out.print("username:");
        String username = scanner.nextLine();
        out.print("password:");
        String password = scanner.nextLine();

        LoginRequest rq = new LoginRequest(username, password);
        LoginResult result = server.login(rq);
        if (result == null || result.getMessage() != null){
            startScreen(scanner);
        } else{
            out.println("Successfully registered and logged in");
            out.println();
            new PostLoginUI();
        }
    }
}
