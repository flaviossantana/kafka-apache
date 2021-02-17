package br.com.kafka.constants;

public class DBConfig {

    public static final String URL_DB = "jdbc:sqlite:store_db.db";
    public static final String CREATE_TB_USERS = "CREATE TABLE IF NOT EXISTS USERS (UUID VARCHAR(200) PRIMARY KEY, EMAIL VARCHAR(200))";
    public static final String INSERT_TB_USERS = "INSERT INTO USERS (UUID, EMAIL) VALUES (?,?)";
    public static final String SELECT_TB_USERS_BY_EMAIL = "SELECT UUID FROM USERS WHERE EMAIL = ?";
    public static final String SELECT_TB_USERS_ALL = "SELECT UUID FROM USERS";

}
