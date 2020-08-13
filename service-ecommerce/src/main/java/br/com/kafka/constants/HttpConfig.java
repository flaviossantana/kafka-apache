package br.com.kafka.constants;

public class HttpConfig {

    public static final int PORT = 8080;
    public static final String PATH_CONTEXT = "/store";
    public static final String PATH_NEW_ORDER = "/new";
    public static final String PATH_SEND_REPORT = "/admin/report";

    private HttpConfig() {
        super();
    }
}
