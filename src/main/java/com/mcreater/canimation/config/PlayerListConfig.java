package com.mcreater.canimation.config;

import com.mcreater.canimation.utils.ChatLogUtils;
import com.mcreater.canimation.utils.FileHelper;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class PlayerListConfig extends AbstractConfig {
    private static final Logger logger = ChatLogUtils.getLogger();
    public boolean enable_player_list_background = DEFAULT_player_list_background;

    public static final boolean DEFAULT_player_list_background = true;
    public PlayerListConfig() {
        super(FileHelper.getConfigPath("canimation/canimation-player-list.json"));
        readConfig();
        checkConfig();
    }

    public void checkConfig() {
        writeConfig();
    }

    public void writeConfig() {
        try {
            PlayerListConfigModel model = new PlayerListConfigModel();
            model.enable_player_list_background = enable_player_list_background;
            if (!createFile() && !getConfigFile().exists()) throw new IOException("Cannot create file");
            FileHelper.write(getConfigFile(), AbstractConfig.GSON.toJson(model));
        }
        catch (Exception e){
            logger.error("failed to apply config!", e);
        }
    }

    public void readConfig() {
        try {
            PlayerListConfig.PlayerListConfigModel model = AbstractConfig.GSON.fromJson(FileHelper.read(getConfigFile()), PlayerListConfig.PlayerListConfigModel.class);
            enable_player_list_background = model.enable_player_list_background;
        }
        catch (Exception e){
            logger.error("failed to read config!", e);
        }
    }
    public static class PlayerListConfigModel {
        public boolean enable_player_list_background;
    }
}
