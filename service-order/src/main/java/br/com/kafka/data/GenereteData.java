package br.com.kafka.data;

import com.github.javafaker.Faker;

import java.util.Locale;

public class GenereteData {

    private static final Faker faker = new Faker(new Locale("pt-BR"));

    public static String email() {
        return faker.internet().emailAddress();
    }

}
