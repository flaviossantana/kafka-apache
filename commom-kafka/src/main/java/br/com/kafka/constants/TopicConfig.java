package br.com.kafka.constants;

public class TopicConfig {

    public static final String STORE_ALL_TOPICS = "STORE.*";
    public static final String STORE_NEW_ORDER = "STORE_NEW_ORDER";
    public static final String STORE_SEND_EMAIL = "STORE_SEND_EMAIL";
    public static final String STORE_REPORT_USER = "STORE_REPORT_USER";
    public static final String STORE_ORDER_APPROVED = "STORE_ORDER_APPROVED";
    public static final String STORE_ORDER_REJECTED = "STORE_ORDER_REJECTED";

    private TopicConfig() {
        super();
    }
}
