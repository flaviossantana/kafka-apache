package br.com.kafka.constants;

public class DBConfig {

    private DBConfig() {
        super();
    }

    public static final String URL_DB = "jdbc:sqlite:";
    public static final String URL_DB_STORE = URL_DB+"store_db.db";
    public static final String SELECT_TB_USERS_ALL = "SELECT UUID FROM USERS";
    public static final String INSERT_TB_USERS = "INSERT INTO USERS (UUID, EMAIL) VALUES (?,?)";
    public static final String SELECT_TB_USERS_BY_EMAIL = "SELECT UUID FROM USERS WHERE EMAIL = ?";
    public static final String CREATE_TB_USERS = "CREATE TABLE IF NOT EXISTS USERS (UUID VARCHAR(200) PRIMARY KEY, EMAIL VARCHAR(200))";


    public static final String URL_DB_FRAUDS = URL_DB+"frauds_db.db";
    public static final String INSERT_TB_ORDER_FRAUDS = "INSERT INTO ORDERS (UUID, IS_FRAUD) VALUES (?,?)";
    public static final String SELECT_TB_ORDER_FRAUDS_BY_ID = "SELECT UUID FROM ORDERS WHERE UUID = ? LIMIT 1";
    public static final String CREATE_TB_ORDER_FRAUDS = "CREATE TABLE IF NOT EXISTS ORDERS (UUID VARCHAR(200) PRIMARY KEY, EMAIL VARCHAR(200), IS_FRAUD BOOLEAN)";

}
