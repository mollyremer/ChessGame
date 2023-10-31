package dataAccess;

import com.sun.source.tree.Tree;
import models.AuthToken;
import models.Game;
import models.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.TreeMap;

public class Database {
    public static TreeMap<String, User> dbUsers = new TreeMap<>();
    public static TreeMap<String, AuthToken> dbAuthTokens = new TreeMap<>();
    public static TreeMap<Integer, Game> dbGames = new TreeMap<>();
}
