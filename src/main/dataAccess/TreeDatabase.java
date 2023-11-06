package dataAccess;

import models.AuthToken;
import models.Game;
import models.User;

import java.util.TreeMap;

public class TreeDatabase {
    public static TreeMap<String, User> dbUsers = new TreeMap<>();
    public static TreeMap<String, AuthToken> dbAuthTokens = new TreeMap<>();
    public static TreeMap<Integer, Game> dbGames = new TreeMap<>();
}
