package com.mcreater.canimation.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mcreater.canimation.utils.FormatUtils;

import java.io.File;
import java.io.IOException;

public abstract class AbstractConfig {
    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public String PATH;
    public AbstractConfig(String PATH) {
        this.PATH = PATH;
        FormatUtils.getDir(PATH).mkdirs();
    }
    public abstract void checkConfig();
    public abstract void writeConfig();
    public abstract void readConfig();
    public boolean createFile() throws IOException {
        return new File(PATH).createNewFile();
    }
    public File getConfigFile() {
        return new File(PATH);
    }
}
