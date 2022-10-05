package com.mcreater.canimation.config;

import com.mcreater.canimation.utils.FileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class AnimationConfig extends AbstractConfig {
    private static final Logger logger = LogManager.getLogger(AnimationConfig.class);

    public boolean enable_chatHUD_animation = DEFAULT_chatHUD_animation;
    public boolean enable_chatScreen_animation = DEFAULT_chatScreen_animation;

    public static final boolean DEFAULT_chatHUD_animation = true;
    public static final boolean DEFAULT_chatScreen_animation = true;

    public AnimationConfig() {
        super(FileHelper.getConfigPath("canimation/canimation-animation.json"));
        readConfig();
        checkConfig();
    }

    public void checkConfig() {
        writeConfig();
    }

    public void writeConfig() {
        try {
            AnimationConfigModel model = new AnimationConfigModel();
            model.enable_chatHUD_animation = enable_chatHUD_animation;
            model.enable_chatScreen_animation = enable_chatScreen_animation;
            if (!createFile() && !getConfigFile().exists()) throw new IOException("Cannot create file");
            FileHelper.write(getConfigFile(), AbstractConfig.GSON.toJson(model));
        }
        catch (Exception e){
            logger.error("failed to apply config!", e);
        }
    }

    public void readConfig() {
        try {
            AnimationConfigModel model = AbstractConfig.GSON.fromJson(FileHelper.read(getConfigFile()), AnimationConfigModel.class);
            enable_chatHUD_animation = model.enable_chatHUD_animation;
            enable_chatScreen_animation = model.enable_chatScreen_animation;
        }
        catch (Exception e){
            logger.error("failed to read config!", e);
        }
    }
    public static class AnimationConfigModel {
        public boolean enable_chatHUD_animation;
        public boolean enable_chatScreen_animation;
    }
}
