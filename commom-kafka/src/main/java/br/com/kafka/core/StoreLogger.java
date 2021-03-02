package br.com.kafka.core;

import java.util.logging.Logger;

public class StoreLogger {

    private StoreLogger() {
        super();
    }

    private static final Logger LOGGER = Logger.getLogger(StoreLogger.class.getName());

    public static void info(String msg){
        LOGGER.info(msg);
    }

    public static void info(Object obj) {
        LOGGER.info(String.valueOf(obj));
    }

    public static void severe(String msg){
        LOGGER.severe(msg);
    }

    public static void severe(Object obj) {
        LOGGER.severe(String.valueOf(obj));
    }

}
