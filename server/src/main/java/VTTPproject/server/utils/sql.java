package VTTPproject.server.utils;

public class sql {
    public static final String sql_getAllUsers = "SELECT * FROM users";
    public static final String sql_getCredentialByEmail = "SELECT * from credentials where users_email=?";
    public static final String sql_getNameByEmail = "SELECT name from users where email=?";

    public static final String sql_insertIntoUser = "INSERT INTO users (email, name) values (?, ?)";
    public static final String sql_insertIntoCredentials = "INSERT INTO credentials (users_email, password) values (?, ?)";

    public static final String sql_insert = "INSERT INTO stocks (user_email, ticker, company_name) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE company_name = VALUES(company_name)";
    public static final String sql_delete = "DELETE FROM stocks WHERE user_email = ? AND ticker = ?";
    public static final String sql_selectByEmail = "SELECT ticker, company_name FROM stocks WHERE user_email = ?";


    
}
