package com.mcreater.canimation.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileHelper {
    public static void write(File file, String content) throws IOException {
        Files.writeString(file.toPath(), content);
    }

    public static String read(File file) throws IOException {
        return Files.readString(file.toPath());
    }
}
