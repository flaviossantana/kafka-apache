package br.com.kafka.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class IO {

    public static void copyTo(String source, File target) throws IOException {
        target.getParentFile().mkdir();
        Files.copy(Paths.get(source), target.toPath(), REPLACE_EXISTING);
    }

    public static File newResourceFile(String fileName) {
        return new File("target/" + fileName);
    }

    public static void append(File target, String content) throws IOException {
        Files.write(target.toPath(), content.getBytes(), StandardOpenOption.APPEND);
    }

    public static String getResourcePath(String fileName){
        return IO.class.getClassLoader().getResource(fileName).getPath();
    }

}
