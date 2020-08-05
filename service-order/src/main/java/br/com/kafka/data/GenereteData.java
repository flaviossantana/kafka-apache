package br.com.kafka.data;

import java.util.Random;

public class GenereteData {

    public static String email() {
        String saltchars = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder salt = new StringBuilder();

        while (salt.length() < 10) {
            int index = (int) (new Random().nextFloat() * saltchars.length());
            salt.append(saltchars.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr.concat("@store.com");
    }

}
