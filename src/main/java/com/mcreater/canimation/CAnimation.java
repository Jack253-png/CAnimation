package com.mcreater.canimation;

import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;
import java.util.function.Consumer;

public class CAnimation implements ModInitializer {
    public void onInitialize() {

    }
    public static String getCurrentModVer() {
        String currentModVer = null;
        try {
            currentModVer = FabricLoader.getInstance().getModContainer("canimation").get().getMetadata().getVersion().getFriendlyString();
        }
        catch (Exception ignored) {}
        return currentModVer;
    }
    public static String checkUpdate() throws Exception {
        String url = "https://api.modrinth.com/v2/project/canimation/version";
        String currentMcVer = null;
        String currentModVer = null;
        try {
            currentMcVer = FabricLoader.getInstance().getModContainer("minecraft").get().getMetadata().getVersion().getFriendlyString();
        }
        catch (Exception ignored) {}
        try {
            currentModVer = FabricLoader.getInstance().getModContainer("canimation").get().getMetadata().getVersion().getFriendlyString();
        }
        catch (Exception ignored) {}
        if (currentMcVer != null && currentModVer != null) {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("User-Agent", "Chat screen animation");
            connection.connect();
            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            final String[] result = {""};
            reader.lines().forEach(s -> result[0] += s + "\n");
            Vector<Map<String, Object>> v = new Vector<>();
            v = new Gson().fromJson(result[0], v.getClass());

            for (Map<String, Object> map : v) {
                List<String> vers = (List<String>) map.get("game_versions");
                if (vers.contains(currentMcVer)) {
                    return (String) map.get("version_number");
                }
            }
        }
        return null;
    }
}
