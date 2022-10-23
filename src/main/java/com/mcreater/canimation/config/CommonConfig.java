package com.mcreater.canimation.config;

import com.google.gson.annotations.SerializedName;
import com.mcreater.canimation.utils.ChatLogUtils;
import com.mcreater.canimation.utils.FileHelper;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class CommonConfig extends AbstractConfig {
    public CommonConfigModel model = new CommonConfigModel();
    private final Logger logger = ChatLogUtils.getLogger();
    public CommonConfig() {
        super(new File(FabricLoaderImpl.INSTANCE.getConfigDir().toFile().getAbsolutePath() , "canimation/canimation.json").getAbsolutePath());
        readConfig();
        checkConfig();
    }

    public void checkConfig() {
        writeConfig();
    }

    public void writeConfig() {
        try {
            createFile();
            FileHelper.write(getConfigFile(), AbstractConfig.GSON.toJson(model));
        }
        catch (Exception e){
            logger.error("failed to write config", e);
        }
    }

    public void readConfig() {
        try {
            CommonConfigModel model = AbstractConfig.GSON.fromJson(FileHelper.read(getConfigFile()), CommonConfigModel.class);
            if (model != null) this.model = model;
            else throw new NullPointerException();
        }
        catch (Exception e){
            logger.error("failed to read config", e);
        }
    }

    public static class CommonConfigModel {
        @SerializedName("animation-control")
        public final AnimationConfigModel animationControl = new AnimationConfigModel();
        @SerializedName("command-suggester")
        public final CommandSuggesterConfigModel commandSuggester = new CommandSuggesterConfigModel();
        @SerializedName("player-list")
        public final PlayerListConfigModel playerList = new PlayerListConfigModel();
    }
    public static class AnimationConfigModel {
        @SerializedName("chat-hud")
        public boolean chatHUD = DEFAULT_chatHUD;
        @SerializedName("chat-screen")
        public boolean chatScreen = DEFAULT_chatScreen;
        @SerializedName("subtitle")
        public boolean subtitle = DEFAULT_subtitle;
        @SerializedName("player-list")
        public boolean playerList = DEFAULT_player_list;
        @SerializedName("boss-bar")
        public boolean bossBar = DEFAULT_boss_bar;

        public static final boolean DEFAULT_chatHUD = true;
        public static final boolean DEFAULT_chatScreen = true;
        public static final boolean DEFAULT_subtitle = true;
        public static final boolean DEFAULT_player_list = true;
        public static final boolean DEFAULT_boss_bar = true;
    }
    public static class CommandSuggesterConfigModel {
        @SerializedName("background")
        public int background = DEFAULT_background;
        @SerializedName("text-fill")
        public int textFill = DEFAULT_text_fill;
        @SerializedName("selected-text-fill")
        public int selectedTextFill = DEFAULT_selected_text_fill;

        public static final int DEFAULT_MAX_SUGGESTION_SIZE = 10;
        public static final int DEFAULT_background = -805306368;
        public static final int DEFAULT_text_fill = -5592406;
        public static final int DEFAULT_selected_text_fill = -256;
    }
    public static class PlayerListConfigModel {
        @SerializedName("background")
        public boolean background = DEFAULT_background;
        public static final boolean DEFAULT_background = true;
    }
}
