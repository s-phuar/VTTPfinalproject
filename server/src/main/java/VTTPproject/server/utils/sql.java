package VTTPproject.server.utils;

public class sql {
    public static final String sql_getAllUsers = "SELECT * FROM users";
    public static final String sql_getCredentialByEmail = "SELECT * from credentials where users_email=?";
    public static final String sql_getNameByEmail = "SELECT name from users where email=?";

    public static final String sql_insertIntoUser = "INSERT INTO users (email, name) values (?, ?)";
    public static final String sql_insertIntoCredentials = "INSERT INTO credentials (users_email, password) values (?, ?)";



    
}
