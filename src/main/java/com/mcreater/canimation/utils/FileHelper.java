package com.mcreater.canimation.utils;

import net.fabricmc.loader.impl.FabricLoaderImpl;

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

    public static String getConfigPath(String exa) {
        return new File(FabricLoaderImpl.INSTANCE.getConfigDir().toFile().getAbsolutePath(), exa).getAbsolutePath();
    }
}
